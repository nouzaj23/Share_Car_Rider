package cz.muni.fi.pv168.project.ui.actions;

import cz.muni.fi.pv168.project.ui.panels.AbstractPanel;
import cz.muni.fi.pv168.project.ui.resources.Icons;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Comparator;

public class DeleteAction<E> extends AbstractAction {

    private final AbstractPanel<E> panel;

    public DeleteAction(AbstractPanel<E> panel) {
        super("Delete", Icons.DELETE_ICON);
        this.panel = panel;
        putValue(SHORT_DESCRIPTION, "Delete record");
        putValue(MNEMONIC_KEY, KeyEvent.VK_D);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl D"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        var table = panel.getTable();

        var selectedRows = table.getSelectedRows();
        var selectedRowsLength = selectedRows.length;
        if (selectedRowsLength > 1) {
            var answer = JOptionPane.showConfirmDialog(
                    panel,
                    "Are you sure you want to delete %s rows?".formatted(selectedRowsLength),
                    "Delete rows",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (answer==JOptionPane.YES_OPTION) {
                deleteRows(selectedRows, table);
            }
        } else {
            deleteRows(selectedRows, table);
        }
    }

    private void deleteRows(int[] selectedRows, JTable table) {
        Arrays.stream(selectedRows)
                // view row index must be converted to model row index
                .map(table::convertRowIndexToModel)
                .boxed()
                // We need to delete rows in descending order to not change index of rows
                // which are not deleted yet
                .sorted(Comparator.reverseOrder())
                .forEach(panel::deleteRow);
    }
}
