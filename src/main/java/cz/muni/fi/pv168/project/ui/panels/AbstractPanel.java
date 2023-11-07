package cz.muni.fi.pv168.project.ui.panels;

import cz.muni.fi.pv168.project.ui.dialog.EntityDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

abstract public class AbstractPanel<E> extends JPanel {

    private final DefaultTableModel model = new DefaultTableModel();

    protected JTable table;

    public JTable setUpTable(String[] columnNames, Object[] template) {
        var table = new JTable();
        table.setAutoCreateRowSorter(true);
        for (String columnName : columnNames) {
            model.addColumn(columnName);
        }
        model.addRow(template);
        table.setModel(model);
        return table;
    }

    public DefaultTableModel getModel(){
        return model;
    }

    abstract public EntityDialog<E> getDialog();

    public abstract void addRow(E entity);

    public JTable getTable() {
        return table;
    }
}
