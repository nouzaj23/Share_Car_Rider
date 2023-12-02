package cz.muni.fi.pv168.project.ui.actions;

import cz.muni.fi.pv168.project.export.service.ExportService;
import cz.muni.fi.pv168.project.util.Filter;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;

public final class ExportAction extends AbstractAction {

    private final JFrame frame;
    private final ExportService exportService;

    public ExportAction(JFrame frame, ExportService exportService) {
        super("Export");
        this.frame = frame;
        this.exportService = exportService;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        var fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        exportService.getFormats().forEach(f -> fileChooser.addChoosableFileFilter(new Filter(f)));

        int dialogResult = fileChooser.showSaveDialog(frame);
        if (dialogResult == JFileChooser.APPROVE_OPTION) {
            String exportFile = fileChooser.getSelectedFile().getAbsolutePath();

            var filter = fileChooser.getFileFilter();
            if (filter instanceof Filter) {
                exportFile = ((Filter) filter).decorate(exportFile);
            }
            exportService.exportData(exportFile);

            JOptionPane.showMessageDialog(frame, "Export has successfully finished.");
        }
    }
}
