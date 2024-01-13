package cz.muni.fi.pv168.project.wiring;

import cz.muni.fi.pv168.project.business.model.Category;
import cz.muni.fi.pv168.project.business.model.Currency;
import cz.muni.fi.pv168.project.business.model.Ride;
import cz.muni.fi.pv168.project.business.model.Template;
import cz.muni.fi.pv168.project.business.repository.Repository;
import cz.muni.fi.pv168.project.business.service.crud.CategoryCrudService;
import cz.muni.fi.pv168.project.business.service.crud.CrudService;
import cz.muni.fi.pv168.project.business.service.crud.CurrencyCrudService;
import cz.muni.fi.pv168.project.business.service.crud.RideCrudService;
import cz.muni.fi.pv168.project.business.service.crud.TemplateCrudService;
import cz.muni.fi.pv168.project.business.service.validation.*;
import cz.muni.fi.pv168.project.storage.sql.CategorySqlRepository;
import cz.muni.fi.pv168.project.storage.sql.CurrencySqlRepository;
import cz.muni.fi.pv168.project.storage.sql.RideSqlRepository;
import cz.muni.fi.pv168.project.storage.sql.TemplateSqlRepository;
import cz.muni.fi.pv168.project.storage.sql.dao.CategoryDao;
import cz.muni.fi.pv168.project.storage.sql.dao.CurrencyDao;
import cz.muni.fi.pv168.project.storage.sql.dao.RideDao;
import cz.muni.fi.pv168.project.storage.sql.dao.TemplateDao;
import cz.muni.fi.pv168.project.storage.sql.db.DatabaseManager;
import cz.muni.fi.pv168.project.storage.sql.db.TransactionConnectionSupplier;
import cz.muni.fi.pv168.project.storage.sql.db.TransactionExecutor;
import cz.muni.fi.pv168.project.storage.sql.db.TransactionExecutorImpl;
import cz.muni.fi.pv168.project.storage.sql.db.TransactionManagerImpl;
import cz.muni.fi.pv168.project.storage.sql.entity.mapper.CategoryMapper;
import cz.muni.fi.pv168.project.storage.sql.entity.mapper.CurrencyMapper;
import cz.muni.fi.pv168.project.storage.sql.entity.mapper.RideMapper;
import cz.muni.fi.pv168.project.storage.sql.entity.mapper.TemplateMapper;

import cz.muni.fi.pv168.project.export.CSVimport;
import cz.muni.fi.pv168.project.export.JsonImport;
import cz.muni.fi.pv168.project.export.service.GenericImportService;

import java.util.List;

/**
 * Common dependency provider for both production and test environment.
 */
public class CommonDependencyProvider implements DependencyProvider {

    private final DatabaseManager databaseManager;
    private final TransactionExecutor transactionExecutor;

    private final Repository<Category> categories;
    private final Repository<Ride> rides;
    private final Repository<Template> templates;
    private final Repository<Currency> currencies;

    private final CrudService<Ride> rideCrudService;
    private final CrudService<Category> categoryCrudService;
    private final CrudService<Template> templateCrudService;
    private final GenericImportService genericImportService;
    private final CrudService<Currency> currencyCrudService;

    private final Validator<Ride> rideValidator;
    private final Validator<Category> categoryValidator;
    private final Validator<Template> templateValidator;
    private final Validator<Currency> currencyValidator;

    public CommonDependencyProvider(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
        var transactionManager = new TransactionManagerImpl(databaseManager);
        this.transactionExecutor = new TransactionExecutorImpl(transactionManager::beginTransaction);
        var transactionConnectionSupplier = new TransactionConnectionSupplier(transactionManager, databaseManager);


        var categoryDao = new CategoryDao(transactionConnectionSupplier);
        var categoryMapper = new CategoryMapper();
        this.categories = new CategorySqlRepository(
                categoryDao,
                categoryMapper
        );

        var currencyDao = new CurrencyDao(transactionConnectionSupplier);
        var currencyMapper = new CurrencyMapper();
        this.currencies = new CurrencySqlRepository(
                currencyDao,
                currencyMapper
        );

        this.rides = new RideSqlRepository(
            new RideDao(transactionConnectionSupplier), 
            new RideMapper(currencyDao, categoryDao, currencyMapper, categoryMapper));


        this.templates = new TemplateSqlRepository(
            new TemplateDao(transactionConnectionSupplier), 
            new TemplateMapper(currencyDao, categoryDao, currencyMapper, categoryMapper)
        );
        
        rideValidator = new RideValidator();
        categoryValidator = new CategoryValidator();
        templateValidator = new TemplateValidator();
        currencyValidator = new CurrencyValidator();
        this.categoryCrudService = new CategoryCrudService(categories, categoryValidator);
        this.templateCrudService = new TemplateCrudService(templates, templateValidator);
        this.currencyCrudService = new CurrencyCrudService(currencies, currencyValidator);
        this.rideCrudService = new RideCrudService(rides, rideValidator, (CategorySqlRepository) categories);

        this.genericImportService = new GenericImportService(rideCrudService, templateCrudService, categoryCrudService, currencyCrudService,
                List.of(new JsonImport(currencyCrudService, categoryCrudService), new CSVimport(currencyCrudService, categoryCrudService)));
    }

    @Override
    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    @Override
    public Repository<Ride> getRideRepository() {
        return rides;
    }

    @Override
    public Repository<Currency> getCurrencyRepository() {
        return currencies;
    }

    @Override
    public Repository<Category> getCategoryRepository() {
        return categories;
    }

    @Override
    public Repository<Template> getTemplateRepository() {
        return templates;
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

    @Override
    public Validator<Ride> getRideValidator() {
        return rideValidator;
    }

    @Override
    public Validator<Category> getCategoryValidator() {
        return categoryValidator;
    }

    @Override
    public Validator<Template> getTemplateValidator() {
        return templateValidator;
    }

    @Override
    public Validator<Currency> getCurrencyValidator() {
        return currencyValidator;
    }

    @Override
    public TransactionExecutor getTransactionExecutor() {
        return transactionExecutor;
    }
}
