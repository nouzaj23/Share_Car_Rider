package cz.muni.fi.pv168.project.export.service;

import cz.muni.fi.pv168.project.business.model.Entity;
import cz.muni.fi.pv168.project.business.service.crud.CrudService;
import cz.muni.fi.pv168.project.export.batch.Batch;
import cz.muni.fi.pv168.project.export.batch.BatchExporter;
import cz.muni.fi.pv168.project.export.format.Format;
import cz.muni.fi.pv168.project.export.format.FormatMapping;
import cz.muni.fi.pv168.project.ui.model.CarRidesModel;
import cz.muni.fi.pv168.project.ui.model.CategoryModel;
import cz.muni.fi.pv168.project.ui.model.EntityTableModel;
import cz.muni.fi.pv168.project.ui.model.TemplateModel;
import cz.muni.fi.pv168.project.ui.panels.CarRidesPanel;
import cz.muni.fi.pv168.project.ui.panels.CategoriesPanel;
import cz.muni.fi.pv168.project.ui.panels.TemplatesPanel;
import cz.muni.fi.pv168.project.wiring.DependencyProvider;

import javax.swing.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class GenericExportService implements ExportService {

    private final CarRidesPanel rides;
    private final TemplatesPanel templates;
    private final CategoriesPanel categories;
    private final FormatMapping<BatchExporter> exporters;
    private final CarRidesModel carRidesModel;
    private final TemplateModel templateModel;
    private final CategoryModel categoryModel;
    private final DependencyProvider dependencyProvider;

    public GenericExportService(
            CarRidesPanel rides,
            TemplatesPanel templates,
            CategoriesPanel categories,
            CarRidesModel carRidesModel,
            TemplateModel templateModel,
            CategoryModel categoryModel,
            DependencyProvider dependencyProvider,
            Collection<BatchExporter> exporters
    ) {
        this.rides = rides;
        this.templates = templates;
        this.categories = categories;
        this.carRidesModel = carRidesModel;
        this.templateModel = templateModel;
        this.categoryModel = categoryModel;
        this.dependencyProvider = dependencyProvider;
        this.exporters = new FormatMapping<>(exporters);
    }

    @Override
    public Collection<Format> getFormats() {
        return exporters.getFormats();
    }

    @Override
    public void exportData(String filePath) {
        var exporter = getExporter(filePath);

        var batch = createBatch();
        exporter.exportBatch(batch, filePath);
    }

    private BatchExporter getExporter(String filePath) {
        var extension = filePath.substring(filePath.lastIndexOf('.') + 1);
        var importer = exporters.findByExtension(extension);
        if (importer == null)
            throw new RuntimeException("Extension %s has no registered formatter".formatted(extension));
        return importer;
    }

    private Batch createBatch(){
        var rideBatch = createEntityList(carRidesModel, rides.getTable(), dependencyProvider.getRideCrudService());
        var templateBatch = createEntityList(templateModel, templates.getTable(), dependencyProvider.getTemplateCrudService());
        var categoryBatch = createEntityList(categoryModel, categories.getTable(), dependencyProvider.getCategoryCrudService());

        return new Batch(rideBatch, categoryBatch, templateBatch);
    }

    private <T extends Entity> List<T> createEntityList(EntityTableModel<T> model, JTable table, CrudService<T> crudService) {
        List<T> batch = Arrays.stream(table.getSelectedRows())
                .mapToObj(model::getEntity)
                .toList();

        if(batch.isEmpty()){
            batch = crudService.findAll();
        }

        return batch;
    }
}

