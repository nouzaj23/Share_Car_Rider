package cz.muni.fi.pv168.project.export.service;

import cz.muni.fi.pv168.project.business.model.Category;
import cz.muni.fi.pv168.project.business.model.Entity;
import cz.muni.fi.pv168.project.business.model.Ride;
import cz.muni.fi.pv168.project.business.model.Template;
import cz.muni.fi.pv168.project.business.model.Currency;
import cz.muni.fi.pv168.project.business.service.crud.CrudService;
import cz.muni.fi.pv168.project.business.service.crud.EntityAlreadyExistsException;
import cz.muni.fi.pv168.project.export.batch.BatchImporter;
import cz.muni.fi.pv168.project.export.format.Format;
import cz.muni.fi.pv168.project.export.format.FormatMapping;

import javax.security.auth.callback.Callback;
import java.util.Collection;
import java.util.function.Function;
import java.util.function.Predicate;

public class GenericImportService implements ImportService {

    private final CrudService<Ride> crudRide;
    private final CrudService<Category> crudCategory;
    private final CrudService<Template> crudTemplate;
    private final CrudService<Currency> crudCurrency;
    private final FormatMapping<BatchImporter> importers;

    public GenericImportService(
            CrudService<Ride> ridesModel,
            CrudService<Template> templateModel,
            CrudService<Category> categoryModel,
            CrudService<Currency> crudCurrency,
            Collection<BatchImporter> importers
    ) {
        this.crudRide = ridesModel;
        this.crudCategory = categoryModel;
        this.crudTemplate = templateModel;
        this.crudCurrency = crudCurrency;
        this.importers = new FormatMapping<>(importers);
    }

    @Override
    public int[] importData(String filePath, ProgressCallback progressCallback) {
        var batch = getImporter(filePath).importBatch(filePath);

        int totalSteps = batch.rides().size() + batch.currencies().size() + batch.categories().size() + batch.templates().size();
        int[] progress = {0};
        int currencies = progressForEach(batch.currencies(), this::createCurrency, progressCallback, progress, totalSteps);
        int cat = progressForEach(batch.categories(), this::createCategory, progressCallback, progress, totalSteps);
        int rides = progressForEach(batch.rides(), this::createRide, progressCallback, progress, totalSteps);
        int template = progressForEach(batch.templates(), this::createTemplate, progressCallback, progress, totalSteps);

        return new int[]{rides, template, cat, currencies};
    }

    private boolean createRide(Ride ride) {
        try {
            crudRide.create(ride);
        } catch (EntityAlreadyExistsException e) {
            return false;
        }
        return true;
    }

    private boolean createCategory(Category category) {
        try {
            crudCategory.create(category);
        } catch (EntityAlreadyExistsException e) {
            return false;
        }
        return true;
    }

    private boolean createTemplate(Template template) {
        try {
            crudTemplate.create(template);
        } catch (EntityAlreadyExistsException e) {
            return false;
        }
        return true;
    }

    private boolean createCurrency(Currency currency) {
        try {
            crudCurrency.create(currency);
        } catch (EntityAlreadyExistsException e) {
            return false;
        }
        return true;
    }

    @Override
    public Collection<Format> getFormats() {
        return importers.getFormats();
    }

    private BatchImporter getImporter(String filePath) {
        var extension = filePath.substring(filePath.lastIndexOf('.') + 1);
        var importer = importers.findByExtension(extension);
        if (importer == null) {
            throw new RuntimeException("Extension %s has no registered formatter".formatted(extension));
        }

        return importer;
    }

    private <T> int progressForEach(Collection<T> collection, Predicate<T> function, ProgressCallback progressCallback, int[] progress, int totalSteps) {
        int result = 0;
        for (T e : collection) {
            if (function.test(e)){
                result++;
            }
            progress[0]++;
            progressCallback.onProgress((progress[0] * 100.0) / totalSteps);

            //uncomment for demonstration
            /*
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
            */
        }
        return result;
    }
}
