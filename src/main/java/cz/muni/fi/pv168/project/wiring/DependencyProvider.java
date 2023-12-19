package cz.muni.fi.pv168.project.wiring;

import cz.muni.fi.pv168.project.business.model.Category;
import cz.muni.fi.pv168.project.business.model.Currency;
import cz.muni.fi.pv168.project.business.model.Ride;
import cz.muni.fi.pv168.project.business.model.Template;
import cz.muni.fi.pv168.project.business.repository.Repository;
import cz.muni.fi.pv168.project.business.service.crud.CrudService;
import cz.muni.fi.pv168.project.export.service.GenericExportService;
import cz.muni.fi.pv168.project.export.service.GenericImportService;
import cz.muni.fi.pv168.project.business.service.validation.Validator;
import cz.muni.fi.pv168.project.storage.sql.db.DatabaseManager;
import cz.muni.fi.pv168.project.storage.sql.db.TransactionExecutor;

/**
 * Interface for instance wiring
 */
public interface DependencyProvider {

    DatabaseManager getDatabaseManager();
    Repository<Currency> getCurrencyRepository();
    Repository<Ride> getRideRepository();
    Repository<Category> getCategoryRepository();
    Repository<Template> getTemplateRepository();
    CrudService<Ride> getRideCrudService();
    CrudService<Category> getCategoryCrudService();
    CrudService<Template> getTemplateCrudService();
    GenericImportService getGenericImportService();
    CrudService<Currency> getCurrencyCrudService();
    Validator<Ride> getRideValidator();
    Validator<Category> getCategoryValidator();
    Validator<Template> getTemplateValidator();
    Validator<Currency> getCurrencyValidator();
    TransactionExecutor getTransactionExecutor();
}
