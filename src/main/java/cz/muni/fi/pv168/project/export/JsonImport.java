package cz.muni.fi.pv168.project.export;

import cz.muni.fi.pv168.project.export.batch.Batch;
import cz.muni.fi.pv168.project.export.batch.BatchImporter;
import cz.muni.fi.pv168.project.export.format.Format;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.model.Currency;
import cz.muni.fi.pv168.project.model.Ride;
import cz.muni.fi.pv168.project.model.Template;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author Adam Paulen
 */
public class JsonImport implements BatchImporter {

    @Override
    public Batch importBatch(String filePath) {
        var categoryHashMap = new HashMap<String, Category>();
        List<Ride> rides = new ArrayList<>();
        List<Template> templates = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try {
            String jsonContent = new String(Files.readAllBytes(Path.of(filePath)));

            JSONObject jsonSystemHelper = new JSONObject(jsonContent);

            JSONArray ridesArray = jsonSystemHelper.getJSONArray("rides");
            JSONArray categoryArray = jsonSystemHelper.getJSONArray("categories");
            JSONArray templateArray = jsonSystemHelper.getJSONArray("templates");

            for (int i = 0; i < categoryArray.length(); i++) {
                JSONObject categoryObject = categoryArray.getJSONObject(i);
                String name = categoryObject.getString("name");
                categoryHashMap.computeIfAbsent(name, num -> new Category(name));
            }

            for (int i = 0; i < ridesArray.length(); i++) {
                JSONObject rideObject = ridesArray.getJSONObject(i);
                String name = rideObject.getString("name");
                int passengers = rideObject.getInt("passengers");
                Currency currency = Currency.valueOf(rideObject.getString("currency"));
                Category category = categoryHashMap.get(rideObject.getJSONObject("category").getString("name"));
                String from = rideObject.getString("from");
                String to = rideObject.getString("to");
                int distance = rideObject.getInt("distance");
                float fuel = rideObject.getFloat("fuelExpenses");
                LocalDate localDate = LocalDate.parse(rideObject.getString("date"), formatter);

                Ride ride = new Ride(name, passengers,currency, fuel, category, from, to, distance);
                ride.setDate(localDate);

                rides.add(ride);
            }

            for (int i = 0; i < templateArray.length(); i++) {
                JSONObject templateObject = templateArray.getJSONObject(i);
                String name = templateObject.getString("name");
                int passengers = templateObject.getInt("passengers");
                Currency currency = Currency.valueOf(templateObject.getString("currency"));
                Category category = categoryHashMap.get(templateObject.getJSONObject("category").getString("name"));
                String from = templateObject.getString("from");
                String to = templateObject.getString("to");
                int distance = templateObject.getInt("distance");

                Template template = new Template(name, passengers,currency, category, from, to, distance);

                templates.add(template);
            }

            return new Batch(rides, categoryHashMap.values(), templates);

        } catch (IOException e) {
            throw new RuntimeException("Unable to read file", e);
        }
    }

    @Override
    public Format getFormat() {
        return new Format("JSON", List.of("json"));
    }
}
