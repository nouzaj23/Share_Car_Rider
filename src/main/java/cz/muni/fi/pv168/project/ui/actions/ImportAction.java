package cz.muni.fi.pv168.project.ui.actions;

import cz.muni.fi.pv168.project.export.service.ImportService;
import cz.muni.fi.pv168.project.export.worker.AsyncImporter;
import cz.muni.fi.pv168.project.export.worker.Importer;
import cz.muni.fi.pv168.project.util.Filter;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Objects;

public final class ImportAction extends AbstractAction {

    private final JFrame frame;
    private final Importer importer;
    private final Runnable refresh;

    public ImportAction(
            JFrame frame,
            ImportService importService,
            Runnable refresh) {

        super("Import");
        this.refresh = refresh;
        this.frame = Objects.requireNonNull(frame);
        this.importer = new AsyncImporter(Objects.requireNonNull(importService),
                                            () -> { refresh.run();
                                                    JOptionPane.showMessageDialog(frame, "Import was done");});

        putValue(SHORT_DESCRIPTION, "Imports employees from a file");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        var fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        importer.getFormats().forEach(f -> fileChooser.addChoosableFileFilter(new Filter(f)));

        int dialogResult = fileChooser.showOpenDialog(frame);

        if (dialogResult == JFileChooser.APPROVE_OPTION) {
            File importFile = fileChooser.getSelectedFile();

            importer.importData(importFile.getAbsolutePath());
        }
    }
}
