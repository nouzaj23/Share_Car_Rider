package cz.muni.fi.pv168.project.export.worker;

import cz.muni.fi.pv168.project.export.format.Format;
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
                int[] importSize = {0,0,0,0};
                try {
                    importSize = importService.importData(filePath);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(
                            null,
                            "error while importing",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
                JOptionPane.showMessageDialog(null, "Import was done\n" +
                                                                            importSize[0] + " rides added\n" +
                                                                            importSize[1] + " templates added\n" +
                                                                            importSize[2] + " categories added\n" +
                                                                            importSize[3] + " currencies added\n");
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
