package cz.muni.fi.pv168.project.wiring;

import cz.muni.fi.pv168.project.business.guidProvider.UuidGuidProvider;
import cz.muni.fi.pv168.project.business.model.Category;
import cz.muni.fi.pv168.project.business.model.Currency;
import cz.muni.fi.pv168.project.business.model.Ride;
import cz.muni.fi.pv168.project.business.model.Template;
import cz.muni.fi.pv168.project.business.repository.Repository;
import cz.muni.fi.pv168.project.business.service.crud.CategoryCrudService;
import cz.muni.fi.pv168.project.business.service.crud.CrudService;
import cz.muni.fi.pv168.project.business.service.crud.RideCrudService;
import cz.muni.fi.pv168.project.business.service.crud.TemplateCrudService;
import cz.muni.fi.pv168.project.business.service.crud.CurencyCrudService;
import cz.muni.fi.pv168.project.business.service.validation.CategoryValidator;
import cz.muni.fi.pv168.project.business.service.validation.CurrencyValidator;
import cz.muni.fi.pv168.project.business.service.validation.RideValidator;
import cz.muni.fi.pv168.project.business.service.validation.TemplateValidator;
import cz.muni.fi.pv168.project.export.CSVimport;
import cz.muni.fi.pv168.project.export.JsonImport;
import cz.muni.fi.pv168.project.export.service.GenericImportService;
import cz.muni.fi.pv168.project.storage.memory.InMemoryRepository;

import java.util.List;

/**
 * Common dependency provider for both production and test environment.
 */
public class CommonDependencyProvider implements DependencyProvider {

    private final Repository<Ride> rideRepository;
    private final Repository<Category> categoryRepository;
    private final Repository<Template> templateRepository;
    private final Repository<Currency> currencyRepository;
    private final CrudService<Ride> rideCrudService;
    private final CrudService<Category> categoryCrudService;
    private final CrudService<Template> templateCrudService;
    private final GenericImportService genericImportService;
    private final CrudService<Currency> currencyCrudService;

    public CommonDependencyProvider() {
        this.rideRepository = new InMemoryRepository<>();
        this.categoryRepository = new InMemoryRepository<>();
        this.templateRepository = new InMemoryRepository<>();
        this.currencyRepository = new InMemoryRepository<>();
        var rideValidator = new RideValidator();
        var categoryValidator = new CategoryValidator();
        var templateValidator = new TemplateValidator();
        var currencyValidator = new CurrencyValidator();
        var guidProvider = new UuidGuidProvider();
        this.rideCrudService = new RideCrudService(rideRepository, rideValidator, guidProvider);
        this.categoryCrudService = new CategoryCrudService(categoryRepository, categoryValidator, guidProvider);
        this.templateCrudService = new TemplateCrudService(templateRepository, templateValidator, guidProvider);
        this.genericImportService = new GenericImportService(rideCrudService, templateCrudService, categoryCrudService, guidProvider, List.of(new JsonImport(), new CSVimport()));
        this.currencyCrudService = new CurrencyCrudService(currencyRepository, currencyValidator, guidProvider);

    }
    @Override
    public Repository<Ride> getRideRepository() {
        return rideRepository;
    }

    @Override
    public Repository<Category> getCategoryRepository() {
        return categoryRepository;
    }

    @Override
    public Repository<Template> getTemplateRepository() {
        return templateRepository;
    }

    @Override
    public Repository<Currency> getCurrencyRepository() {
        return currencyRepository;
    }

    @Override
    public CrudService<Ride> getRideCrudService() {
        return rideCrudService;
    }

    @Override
    public CrudService<Category> getCategoryCrudService() {
        return categoryCrudService;
    }

    @Override
    public CrudService<Template> getTemplateCrudService() {
        return templateCrudService;
    }

    @Override
    public GenericImportService getGenericImportService() {
        return genericImportService;
    }

    @Override
    public CrudService<Currency> getCurrencyCrudService() {
        return currencyCrudService;
    }
}
