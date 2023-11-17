package cz.muni.fi.pv168.project.export.service;

import cz.muni.fi.pv168.project.export.batch.Batch;
import cz.muni.fi.pv168.project.export.batch.BatchExporter;
import cz.muni.fi.pv168.project.export.format.Format;
import cz.muni.fi.pv168.project.export.format.FormatMapping;
import cz.muni.fi.pv168.project.ui.model.CarRidesModel;
import cz.muni.fi.pv168.project.ui.model.CategoryModel;
import cz.muni.fi.pv168.project.ui.model.TemplateModel;

import java.util.Collection;

public class GenericExportService implements ExportService {

    private final CarRidesModel rides;
    private final TemplateModel templates;
    private final CategoryModel categories;
    private final FormatMapping<BatchExporter> exporters;

    public GenericExportService(
            CarRidesModel rides,
            TemplateModel templates,
            CategoryModel categories,
            Collection<BatchExporter> exporters
    ) {
        this.rides = rides;
        this.templates = templates;
        this.categories = categories;
        this.exporters = new FormatMapping<>(exporters);
    }

    @Override
    public Collection<Format> getFormats() {
        return exporters.getFormats();
    }

    @Override
    public void exportData(String filePath) {
        var exporter = getExporter(filePath);

        var batch = new Batch(rides.getList(), categories.getList(), templates.getList());
        exporter.exportBatch(batch, filePath);
    }

    private BatchExporter getExporter(String filePath) {
        var extension = filePath.substring(filePath.lastIndexOf('.') + 1);
        var importer = exporters.findByExtension(extension);
        if (importer == null)
            throw new RuntimeException("Extension %s has no registered formatter".formatted(extension));
        return importer;
    }
}

