package cz.muni.fi.pv168.project.export;

import cz.muni.fi.pv168.project.business.guidProvider.GuidProvider;
import cz.muni.fi.pv168.project.business.model.Category;
import cz.muni.fi.pv168.project.business.model.Currency;
import cz.muni.fi.pv168.project.business.model.Ride;
import cz.muni.fi.pv168.project.business.service.crud.CategoryCrudService;
import cz.muni.fi.pv168.project.business.service.crud.CrudService;
import cz.muni.fi.pv168.project.export.batch.Batch;
import cz.muni.fi.pv168.project.export.batch.BatchImporter;
import cz.muni.fi.pv168.project.export.format.Format;

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
    private final CrudService<Category> categoryCrudService;

    public CSVimport(CrudService<Currency> currencyCrudService, CrudService<Category> categoryCrudService) {
        this.currencyCrudService = currencyCrudService;
        this.categoryCrudService = categoryCrudService;
    }
    @Override
    public Format getFormat() {
        return new Format("CSV", List.of("csv"));
    }

    @Override
    public Batch importBatch(String filePath) {
        var categoryHashMap = new HashMap<>(categoryCrudService.findAll().stream()
                .collect(Collectors.toMap(Category::getName, cat -> cat)));

        var currencyHashMap = new HashMap<>(currencyCrudService.findAll().stream()
                .collect(Collectors.toMap(Currency::getCode, currency -> currency)));


        try(var reader = Files.newBufferedReader(Path.of(filePath))) {
            var rides = reader.lines()
                    .map(line -> parseRide(categoryHashMap, currencyHashMap, line))
                    .toList();

            return new Batch(rides, categoryHashMap.values(), Collections.emptyList(), currencyHashMap.values());
        } catch (Exception e) {
            throw new RuntimeException("Unable to read file", e);
        }
    }

    private Ride parseRide(HashMap<String, Category> categories, HashMap<String, Currency> currencies, String line) {
        var fields = line.split(",");
        var category = categories.computeIfAbsent(fields[4], num -> new Category(GuidProvider.newGuid(), fields[5]));
        category.setRides(category.getRides() + 1);
        category.modifyDistanceFluent(Integer.parseInt(fields[8]));

        var currency = currencies.computeIfAbsent(fields[2], cur -> new Currency(GuidProvider.newGuid(), fields[2],Double.parseDouble(fields[3])));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MM yy");

        return new Ride(
                GuidProvider.newGuid(),
                fields[0],
                Integer.parseInt(fields[1]),
                currency,
                Float.parseFloat(fields[4]),
                category,
                fields[6],
                fields[7],
                Integer.parseInt(fields[8]),
                LocalDate.parse(fields[9], formatter)
        );
    }
}
