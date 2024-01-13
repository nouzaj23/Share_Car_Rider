package cz.muni.fi.pv168.project.export.service;

import cz.muni.fi.pv168.project.business.model.Category;
import cz.muni.fi.pv168.project.business.model.Ride;
import cz.muni.fi.pv168.project.business.model.Template;
import cz.muni.fi.pv168.project.business.model.Currency;
import cz.muni.fi.pv168.project.business.service.crud.CrudService;
import cz.muni.fi.pv168.project.business.service.crud.EntityAlreadyExistsException;
import cz.muni.fi.pv168.project.export.batch.BatchImporter;
import cz.muni.fi.pv168.project.export.format.Format;
import cz.muni.fi.pv168.project.export.format.FormatMapping;

import java.util.Collection;

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
    public int[] importData(String filePath) {
        var batch = getImporter(filePath).importBatch(filePath);

        int currencies = (int) batch.currencies().stream().filter(this::createCurrency).count();
        int cat = (int) batch.categories().stream().filter(this::createCategory).count();
        int rides = (int) batch.rides().stream().filter(this::createRide).count();
        int template = (int) batch.templates().stream().filter(this::createTemplate).count();

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

}
