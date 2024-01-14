package cz.muni.fi.pv168.project.export.service;

import cz.muni.fi.pv168.project.business.model.Category;
import cz.muni.fi.pv168.project.business.model.Entity;
import cz.muni.fi.pv168.project.business.model.Ride;
import cz.muni.fi.pv168.project.business.model.Template;
import cz.muni.fi.pv168.project.business.service.crud.CrudService;
import cz.muni.fi.pv168.project.export.batch.Batch;
import cz.muni.fi.pv168.project.export.batch.BatchExporter;
import cz.muni.fi.pv168.project.export.format.Format;
import cz.muni.fi.pv168.project.export.format.FormatMapping;
import cz.muni.fi.pv168.project.ui.model.EntityTableModel;
import cz.muni.fi.pv168.project.ui.panels.AbstractPanel;
import cz.muni.fi.pv168.project.wiring.DependencyProvider;

import javax.swing.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class GenericExportService implements ExportService {

    private final AbstractPanel<Ride> rides;
    private final AbstractPanel<Template> templates;
    private final AbstractPanel<Category> categories;
    private final FormatMapping<BatchExporter> exporters;
    private final EntityTableModel<Ride> carRidesModel;
    private final EntityTableModel<Template> templateModel;
    private final EntityTableModel<Category> categoryModel;
    private final DependencyProvider dependencyProvider;

    public GenericExportService(
            AbstractPanel<Ride> rides,
            AbstractPanel<Template> templates,
            AbstractPanel<Category> categories,
            EntityTableModel<Ride> carRidesModel,
            EntityTableModel<Template> templateModel,
            EntityTableModel<Category> categoryModel,
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
    public int[] exportData(String filePath) {
        var exporter = getExporter(filePath);

        var batch = createBatch();
        return exporter.exportBatch(batch, filePath);
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

        return new Batch(rideBatch, categoryBatch, templateBatch, dependencyProvider.getCurrencyCrudService().findAll());
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

