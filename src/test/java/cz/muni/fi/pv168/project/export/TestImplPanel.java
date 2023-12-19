package cz.muni.fi.pv168.project.export;

import cz.muni.fi.pv168.project.business.model.Entity;
import cz.muni.fi.pv168.project.business.model.Ride;
import cz.muni.fi.pv168.project.ui.dialog.EntityDialog;
import cz.muni.fi.pv168.project.ui.panels.AbstractPanel;

import javax.swing.*;

/**
 * @author Adam Paulen
 */
public class TestImplPanel<T extends Entity> extends AbstractPanel<Entity>{
    @Override
    public EntityDialog<Entity> getDialog() {
        return null;
    }

    @Override
    public EntityDialog<Entity> getDialog(Entity entity) {
        return null;
    }

    @Override
    public EntityDialog<Entity> getEditDialog(Entity entity) {
        return null;
    }

    @Override
    public void addRow(Entity entity) {

    }

    @Override
    public void deleteRow(int rowIndex) {

    }

    @Override
    public void editRow(Entity newEntity, Ride oldRide) {
    }

    public JTable getTable() {
        return new JTable();
    }
}
