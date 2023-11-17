package cz.muni.fi.pv168.project.ui.actions;

import cz.muni.fi.pv168.project.export.service.ImportService;
import cz.muni.fi.pv168.project.util.Filter;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Objects;

public final class ImportAction extends AbstractAction {

    private final JFrame frame;
    private final ImportService importService;

    public ImportAction(
            JFrame frame,
            ImportService importService) {
        super("Import");
        this.frame = Objects.requireNonNull(frame);
        this.importService = Objects.requireNonNull(importService);

        putValue(SHORT_DESCRIPTION, "Imports employees from a file");
        putValue(MNEMONIC_KEY, KeyEvent.VK_I);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl I"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        var fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        importService.getFormats().forEach(f -> fileChooser.addChoosableFileFilter(new Filter(f)));

        int dialogResult = fileChooser.showOpenDialog(frame);

        if (dialogResult == JFileChooser.APPROVE_OPTION) {
            File importFile = fileChooser.getSelectedFile();

            importService.importData(importFile.getAbsolutePath());

            JOptionPane.showMessageDialog(frame, "Import was done");
        }
    }
}
