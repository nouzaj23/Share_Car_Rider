package cz.muni.fi.pv168.project.export;

import cz.muni.fi.pv168.project.business.guidProvider.GuidProvider;
import cz.muni.fi.pv168.project.business.model.Category;
import cz.muni.fi.pv168.project.business.model.Currency;
import cz.muni.fi.pv168.project.business.model.Ride;
import cz.muni.fi.pv168.project.business.model.Template;
import cz.muni.fi.pv168.project.business.service.crud.CrudService;
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
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Adam Paulen
 */
public class JsonImport implements BatchImporter {

    private final CrudService<Currency> currencyCrudService;
    private final CrudService<Category> categoryCrudService;
    public JsonImport(CrudService<Currency> currencyCrudService, CrudService<Category> categoryCrudService) {
        this.currencyCrudService = currencyCrudService;
        this.categoryCrudService = categoryCrudService;
    }
    @Override
    public Batch importBatch(String filePath) {
        var categoryHashMap = new HashMap<>(categoryCrudService.findAll().stream().collect(Collectors.toMap(Category::getName, cat -> cat)));
        List<Ride> rides = new ArrayList<>();
        List<Template> templates = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        var currencyHashMap = new HashMap<>(currencyCrudService.findAll().stream()
                .collect(Collectors.toMap(Currency::getCode, currency -> currency)));

        try {
            String jsonContent = new String(Files.readAllBytes(Path.of(filePath)));

            JSONObject jsonSystemHelper = new JSONObject(jsonContent);

            JSONArray ridesArray = jsonSystemHelper.getJSONArray("rides");
            JSONArray categoryArray = jsonSystemHelper.getJSONArray("categories");
            JSONArray templateArray = jsonSystemHelper.getJSONArray("templates");
            JSONArray currencyArray = jsonSystemHelper.getJSONArray("currencies");

            for (int i = 0; i < currencyArray.length(); i++) {
                JSONObject currencyObject = currencyArray.getJSONObject(i);
                String name = currencyObject.getString("code");
                float rate = currencyObject.getFloat("conversionRatio");
                String gid = currencyObject.getString("guid");
                var cur = new Currency(gid, name, rate);

                currencyHashMap.putIfAbsent(name, cur);
            }

            for (int i = 0; i < categoryArray.length(); i++) {
                JSONObject categoryObject = categoryArray.getJSONObject(i);
                String guid = categoryObject.getString("guid");
                String name = categoryObject.getString("name");
                var cat = new Category(guid, name);

                categoryHashMap.putIfAbsent(name, cat);
            }

            for (int i = 0; i < ridesArray.length(); i++) {
                JSONObject rideObject = ridesArray.getJSONObject(i);
                String guid = rideObject.getString("guid");
                String name = rideObject.getString("name");
                int passengers = rideObject.getInt("passengers");
                String currencyName = rideObject.getJSONObject("currency").getString("code");
                var currency = currencyHashMap.computeIfAbsent(currencyName, cur -> new Currency(GuidProvider.newGuid(), currencyName, 15));
                Category category = categoryHashMap.get(rideObject.getJSONObject("category").getString("name"));
                String from = rideObject.getString("from");
                String to = rideObject.getString("to");
                int distance = rideObject.getInt("distance");
                float fuel = rideObject.getFloat("fuelExpenses");
                LocalDate localDate = LocalDate.parse(rideObject.getString("date"), formatter);


                Ride ride = new Ride(guid, name, passengers,currency, fuel, category, from, to, distance, localDate);

                rides.add(ride);
            }

            for (int i = 0; i < templateArray.length(); i++) {
                JSONObject templateObject = templateArray.getJSONObject(i);
                String guid = templateObject.getString("guid");
                String name = templateObject.getString("name");
                int passengers = templateObject.getInt("passengers");
                String currencyName = templateObject.getJSONObject("currency").getString("code");
                var currency = currencyHashMap.computeIfAbsent(currencyName, cur -> new Currency(GuidProvider.newGuid(), currencyName, 15));
                Category category = categoryHashMap.get(templateObject.getJSONObject("category").getString("name"));
                String from = templateObject.getString("from");
                String to = templateObject.getString("to");
                int distance = templateObject.getInt("distance");

                Template template = new Template(guid, name, passengers, currency, category, from, to, distance);

                templates.add(template);
            }

            return new Batch(rides, categoryHashMap.values(), templates, currencyHashMap.values());

        } catch (IOException | JSONException e) {
            throw new RuntimeException("Unable to read file", e);
        }
    }

    @Override
    public Format getFormat() {
        return new Format("JSON", List.of("json"));
    }
}
