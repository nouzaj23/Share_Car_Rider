package cz.muni.fi.pv168.project.export.service;

import cz.muni.fi.pv168.project.export.batch.BatchImporter;
import cz.muni.fi.pv168.project.export.format.Format;
import cz.muni.fi.pv168.project.export.format.FormatMapping;
import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.model.Ride;
import cz.muni.fi.pv168.project.model.Template;
import cz.muni.fi.pv168.project.ui.model.CarRidesModel;
import cz.muni.fi.pv168.project.ui.model.CategoryModel;
import cz.muni.fi.pv168.project.ui.model.TemplateModel;
import java.util.Collection;

public class GenericImportService implements ImportService {

    private final CarRidesModel ridesModel;
    private final CategoryModel categoryModel;
    private final TemplateModel templateModel;
    private final FormatMapping<BatchImporter> importers;

    public GenericImportService(
            CarRidesModel ridesModel,
            CategoryModel categoryModel,
            TemplateModel templateModel,
            Collection<BatchImporter> importers
    ) {
        this.ridesModel = ridesModel;
        this.categoryModel = categoryModel;
        this.templateModel = templateModel;
        this.importers = new FormatMapping<>(importers);
    }

    @Override
    public void importData(String filePath) {
        ridesModel.deleteAll();
        categoryModel.deleteAll();
        templateModel.deleteAll();

        var batch = getImporter(filePath).importBatch(filePath);

        batch.categories().forEach(this::createCategory);
        batch.rides().forEach(this::createRide);
        batch.templates().forEach(this::createTemplate);
    }

    private void createRide(Ride ride) {
        ridesModel.addRow(ride);
    }

    private void createCategory(Category category) {
        categoryModel.addRow(category);
    }

    private void createTemplate(Template template) {
        templateModel.addRow(template);
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
