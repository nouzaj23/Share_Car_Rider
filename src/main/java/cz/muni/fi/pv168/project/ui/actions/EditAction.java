package cz.muni.fi.pv168.project.ui.actions;

import cz.muni.fi.pv168.project.business.guidProvider.GuidProvider;
import cz.muni.fi.pv168.project.business.model.Ride;
import cz.muni.fi.pv168.project.ui.model.EntityTableModel;
import cz.muni.fi.pv168.project.ui.panels.AbstractPanel;
import cz.muni.fi.pv168.project.ui.resources.Icons;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class EditAction<E> extends AbstractAction {

    private final AbstractPanel<E> panel;

    public EditAction(AbstractPanel<E> panel) {
        super("Edit", Icons.EDIT_ICON);
        this.panel = panel;
        putValue(SHORT_DESCRIPTION, "Edit record");
        putValue(MNEMONIC_KEY, KeyEvent.VK_E);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl E"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        var table = panel.getTable();
        int[] selectedRows = table.getSelectedRows();
        if (selectedRows.length != 1) {
            throw new IllegalStateException("Invalid selected rows count (must be 1): " + selectedRows.length);
        }
        if (table.isEditing()) {
            table.getCellEditor().cancelCellEditing();
        }
        var tableModel = (EntityTableModel) table.getModel();
        int modelRow = table.convertRowIndexToModel(selectedRows[0]);
        var entity = tableModel.getEntity(modelRow);
        var dialog = panel.getEditDialog((E) entity);
        if (entity instanceof Ride oldRide) {
            Ride oldRideCopy = 
            new Ride(
                GuidProvider.newGuid(),
                oldRide.getName(), 
                oldRide.getPassengers(), 
                oldRide.getCurrency(), 
                oldRide.getFuelExpenses(), 
                oldRide.getCategory(),
                oldRide.getFrom(), 
                oldRide.getTo(),
                oldRide.getDistance(),
                oldRide.getDate());
            dialog.show(table, "Edit Employee")
                    .ifPresent(r -> panel.editRow(r, oldRideCopy));
        } else {
            dialog.show(table, "Edit Employee")
                    .ifPresent(r -> panel.editRow(r, null));
        }
    }
}
