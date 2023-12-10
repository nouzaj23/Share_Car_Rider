package cz.muni.fi.pv168.project.export.worker;

import cz.muni.fi.pv168.project.export.format.Format;
import cz.muni.fi.pv168.project.export.service.ExportService;
import cz.muni.fi.pv168.project.export.service.ImportService;

import javax.swing.*;
import java.util.Collection;
import java.util.Objects;

/**
 * @author Adam Paulen
 */
public class AsyncImporter implements Importer {

    private final ImportService importService;
    private final Runnable onFinish;

    public AsyncImporter(ImportService importService, Runnable onFinish) {
        this.importService = Objects.requireNonNull(importService);
        this.onFinish = onFinish;
    }

    @Override
    public Collection<Format> getFormats() {
        return importService.getFormats();
    }

    @Override
    public void importData(String filePath) {
        var asyncWorker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                importService.importData(filePath);
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
