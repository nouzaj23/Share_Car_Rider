package cz.muni.fi.pv168.project.wiring;

import cz.muni.fi.pv168.project.business.model.Category;
import cz.muni.fi.pv168.project.business.model.Currency;
import cz.muni.fi.pv168.project.business.model.Ride;
import cz.muni.fi.pv168.project.business.model.Template;
import cz.muni.fi.pv168.project.business.repository.Repository;
import cz.muni.fi.pv168.project.business.service.crud.CrudService;
import cz.muni.fi.pv168.project.export.service.GenericExportService;
import cz.muni.fi.pv168.project.export.service.GenericImportService;

/**
 * Interface for instance wiring
 */
public interface DependencyProvider {

    Repository<Ride> getRideRepository();
    Repository<Category> getCategoryRepository();
    Repository<Template> getTemplateRepository();
    Repository<Currency> getCurrencyRepository();
    CrudService<Ride> getRideCrudService();
    CrudService<Category> getCategoryCrudService();
    CrudService<Template> getTemplateCrudService();
    GenericImportService getGenericImportService();
    CrudService<Currency> getCurrencyCrudService();
}
