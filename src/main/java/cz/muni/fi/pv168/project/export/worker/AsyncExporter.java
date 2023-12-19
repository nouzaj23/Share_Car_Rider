package cz.muni.fi.pv168.project.export.worker;

import cz.muni.fi.pv168.project.export.format.Format;
import cz.muni.fi.pv168.project.export.service.ExportService;


import javax.swing.SwingWorker;
import java.util.Collection;
import java.util.Objects;

public class AsyncExporter implements Exporter {

    private final ExportService exportService;
    private final Runnable onFinish;

    public AsyncExporter(ExportService exportService, Runnable onFinish) {
        this.exportService = Objects.requireNonNull(exportService);
        this.onFinish = onFinish;
    }

    @Override
    public Collection<Format> getFormats() {
        return exportService.getFormats();
    }

    @Override
    public void exportData(String filePath) {
        var asyncWorker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                exportService.exportData(filePath);
                return null;
            }

            @Override
            protected void done() {
                super.done();
                onFinish.run();
            }
        };
        asyncWorker.execute();
    }
}
