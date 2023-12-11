package cz.muni.fi.pv168.project.export.service;

import cz.muni.fi.pv168.project.business.guidProvider.GuidProvider;
import cz.muni.fi.pv168.project.business.model.Category;
import cz.muni.fi.pv168.project.business.model.Ride;
import cz.muni.fi.pv168.project.business.model.Template;
import cz.muni.fi.pv168.project.business.model.Currency;
import cz.muni.fi.pv168.project.business.service.crud.CrudService;
import cz.muni.fi.pv168.project.business.service.crud.EntityAlreadyExistsException;
import cz.muni.fi.pv168.project.export.batch.BatchImporter;
import cz.muni.fi.pv168.project.export.format.Format;
import cz.muni.fi.pv168.project.export.format.FormatMapping;

import javax.swing.*;
import java.util.Collection;

import static javax.swing.JOptionPane.OK_CANCEL_OPTION;
import static javax.swing.JOptionPane.OK_OPTION;
import static javax.swing.JOptionPane.PLAIN_MESSAGE;

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
    public void importData(String filePath) {
        var batch = getImporter(filePath).importBatch(filePath);

        batch.currencies().forEach(this::createCurrency);
        batch.categories().forEach(this::createCategory);
        batch.rides().forEach(this::createRide);
        batch.templates().forEach(this::createTemplate);
    }

    private void createRide(Ride ride) {
        try {
            crudRide.create(ride);
        } catch (EntityAlreadyExistsException e ) {

            int result = JOptionPane.showOptionDialog(null,
                    "Ride with this guid was found, do you want to import anyways?", "ERROR: duplicated ride",
                    OK_CANCEL_OPTION, PLAIN_MESSAGE, null, null, null);
            if (result == OK_OPTION) {
                ride.setGuid(GuidProvider.newGuid());
                crudRide.create(ride).intoException();
            }
        }
    }

    private void createCategory(Category category) {
        try {
            crudCategory.create(category);
        }  catch (EntityAlreadyExistsException e ) {
            int result = JOptionPane.showOptionDialog(null,
                    "Category with this guid was found, do you want to import anyways?", "ERROR: duplicated category",
                    OK_CANCEL_OPTION, PLAIN_MESSAGE, null, null, null);
            if (result == OK_OPTION) {
                category.setGuid(GuidProvider.newGuid());
                crudCategory.create(category).intoException();
            }
        }
    }

    private void createTemplate(Template template) {
        try {
            crudTemplate.create(template);
        } catch (EntityAlreadyExistsException e ) {
            int result = JOptionPane.showOptionDialog(null,
                    "Ride with this guid was found, do you want to import anyways?", "ERROR: duplicated ride",
                    OK_CANCEL_OPTION, PLAIN_MESSAGE, null, null, null);
            if (result == OK_OPTION) {
                template.setGuid(GuidProvider.newGuid());
                crudTemplate.create(template).intoException();
            }
        }
    }

    private void createCurrency(Currency currency){
        try {
            crudCurrency.create(currency);
        } catch (EntityAlreadyExistsException e ) {
            //idk todo
        }
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
