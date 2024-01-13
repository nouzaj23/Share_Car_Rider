package cz.muni.fi.pv168.project.export.worker;

import cz.muni.fi.pv168.project.export.format.Format;
import cz.muni.fi.pv168.project.export.service.ExportService;


import javax.swing.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

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
    public int[] exportData(String filePath) {
        var asyncWorker = new SwingWorker<int[], Void>() {
            @Override
            protected int[] doInBackground() {
                return exportService.exportData(filePath);
            }

            @Override
            protected void done() {
                super.done();
                try {
                    int[] result = get();
                    JOptionPane.showMessageDialog(null, "Export has successfully finished.\n" +
                                                                            result[0] + " rides exported\n" +
                                                                            result[1] + " templates exported\n" +
                                                                            result[2] + " categories exported\n" +
                                                                            result[3] + " currencies exported");
                    onFinish.run();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
                onFinish.run();

            }
        };
        asyncWorker.execute();
        return new int[0];
    }
}
