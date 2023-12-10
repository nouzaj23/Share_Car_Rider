package cz.muni.fi.pv168.project.export;

import cz.muni.fi.pv168.project.business.guidProvider.GuidProvider;
import cz.muni.fi.pv168.project.business.model.Category;
import cz.muni.fi.pv168.project.business.model.Currency;
import cz.muni.fi.pv168.project.business.model.Ride;
import cz.muni.fi.pv168.project.business.service.crud.CrudService;
import cz.muni.fi.pv168.project.export.batch.Batch;
import cz.muni.fi.pv168.project.export.batch.BatchImporter;
import cz.muni.fi.pv168.project.export.format.Format;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class CSVimport implements BatchImporter {

    private final CrudService<Currency> currencyCrudService;
    public CSVimport(CrudService<Currency> currencyCrudService) {
        this.currencyCrudService = currencyCrudService;
    }
    @Override
    public Format getFormat() {
        return new Format("CSV", List.of("csv"));
    }

    @Override
    public Batch importBatch(String filePath) {
        var categoryHashMap = new HashMap<String, Category>();
        var currencyHashMap = new HashMap<String, Currency>(currencyCrudService.findAll().stream()
                .collect(Collectors.toMap(Currency::getCode, currency -> currency)));


        try(var reader = Files.newBufferedReader(Path.of(filePath))) {
            var rides = reader.lines()
                    .map(line -> parseRide(categoryHashMap, currencyHashMap, line))
                    .toList();

            return new Batch(rides, categoryHashMap.values(), Collections.emptyList(), currencyHashMap.values());
        } catch (IOException e) {
            throw new RuntimeException("Unable to read file", e);
        }
    }

    private Ride parseRide(HashMap<String, Category> categories, HashMap<String, Currency> currencies, String line) {
        var fields = line.split(",");
        var category = categories.computeIfAbsent(fields[4], num -> new Category(GuidProvider.newGuid(), fields[4]));
        category.setRides(category.getRides() + 1);
        category.modifyDistanceFluent(Integer.parseInt(fields[7]));

        var currency = currencies.computeIfAbsent(fields[2], cur -> new Currency(GuidProvider.newGuid(), fields[2],0));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MM yy");

        Ride ride = new Ride(
                GuidProvider.newGuid(),
                fields[0],
                Integer.parseInt(fields[1]),
                currency,
                Float.parseFloat(fields[3]),
                category,
                fields[5],
                fields[6],
                Integer.parseInt(fields[7]),
                LocalDate.parse(fields[8], formatter)
        );

        return ride;
    }
}
