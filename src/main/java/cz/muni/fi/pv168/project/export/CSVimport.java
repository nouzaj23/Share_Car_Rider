package cz.muni.fi.pv168.project.export;

import cz.muni.fi.pv168.project.export.batch.Batch;
import cz.muni.fi.pv168.project.export.batch.BatchImporter;
import cz.muni.fi.pv168.project.export.format.Format;
import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.model.Currency;
import cz.muni.fi.pv168.project.model.Ride;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class CSVimport implements BatchImporter {

    @Override
    public Format getFormat() {
        return new Format("CSV", List.of("csv"));
    }

    @Override
    public Batch importBatch(String filePath) {
        var categoryHashMap = new HashMap<String, Category>();

        try(var reader = Files.newBufferedReader(Path.of(filePath))) {
            var rides = reader.lines()
                    .map(line -> parseRide(categoryHashMap, line))
                    .toList();

            return new Batch(rides, categoryHashMap.values(), Collections.emptyList());
        } catch (IOException e) {
            throw new RuntimeException("Unable to read file", e);
        }
    }

    private Ride parseRide(HashMap<String, Category> categories, String line) {
        var fields = line.split(",");
        var category = categories.computeIfAbsent(fields[4], num -> new Category(fields[4]));

        Ride ride = new Ride(
                fields[0],
                Integer.parseInt(fields[1]),
                Currency.valueOf(fields[2]),
                Float.parseFloat(fields[3]),
                category,
                fields[5],
                fields[6],
                Integer.parseInt(fields[7])
        );
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MM yy");
        ride.setDate(LocalDate.parse(fields[8], formatter));
        return ride;
    }
}
