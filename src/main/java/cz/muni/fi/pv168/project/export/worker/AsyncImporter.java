package cz.muni.fi.pv168.project.export.worker;

import cz.muni.fi.pv168.project.export.format.Format;
import cz.muni.fi.pv168.project.export.service.ImportService;
import cz.muni.fi.pv168.project.export.service.ProgressCallback;
import org.h2.constraint.DomainColumnResolver;

import javax.swing.*;
import java.awt.*;
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
                JProgressBar progressBar = new JProgressBar(0, 100);
                JFrame parentFrame = new JFrame();

                SwingUtilities.invokeLater(() -> {
                    var progressDialog = new JDialog(parentFrame, "Import Progress", Dialog.ModalityType.MODELESS);
                    progressDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                    progressDialog.setSize(300, 100);
                    progressDialog.setLocationRelativeTo(null);

                    progressBar.setStringPainted(true);
                    progressDialog.add(progressBar);

                    // Show the progress dialog on the EDT
                    SwingUtilities.invokeLater(() -> progressDialog.setVisible(true));
                });

                int[] importSize = {0,0,0,0};

                ProgressCallback progressCallback = progress -> {
                    setProgress((int) progress);
                };

                addPropertyChangeListener(evt -> {
                    if ("progress".equals(evt.getPropertyName())) {
                        int progress = (int) evt.getNewValue();
                        progressBar.setValue(progress);
                    }
                });

                try {
                    importSize = importService.importData(filePath, progressCallback);
                    parentFrame.dispose();
                } catch (Exception e) {
                    parentFrame.dispose();
                    JOptionPane.showMessageDialog(
                            null,
                            "error while importing",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                    throw e;
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
