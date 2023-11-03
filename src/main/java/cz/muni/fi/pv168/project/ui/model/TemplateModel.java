package cz.muni.fi.pv168.project.ui.model;

import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.model.Currency;
import cz.muni.fi.pv168.project.model.Ride;

import javax.swing.table.AbstractTableModel;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TemplateModel extends AbstractTableModel implements EnitityTableModel<Ride> {

    private final List<Ride> templates;
    private final List<Column<Ride, ?>> columns = List.of(
            Column.editable("Name", String.class, Ride::getName, Ride::setName),
            Column.editable("Passengers", Integer.class, Ride::getPassengers, Ride::setPassengers),
            Column.editable("Currency", Currency.class, Ride::getCurrency, Ride::setCurrency),
            Column.editable("Category", Category.class, Ride::getCategory, Ride::setCategory),
            Column.readonly("From", String.class, Ride::getFrom),
            Column.readonly("To", String.class, Ride::getTo),
            Column.editable("Distance", Integer.class, Ride::getDistance, Ride::setDistance),
            Column.editable("Hours", Float.class, Ride::getHours, Ride::setHours)
    );

    public TemplateModel(Collection<Ride> rides) {
        this.templates = new ArrayList<>(rides);
    }

    public Ride[] getArray(){
        return templates.toArray(Ride[]::new);
    }
    @Override
    public int getRowCount() {
        return templates.size();
    }

    @Override
    public int getColumnCount() {
        return columns.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        var template = getEntity(rowIndex);
        return columns.get(columnIndex).getValue((template));
    }

    @Override
    public Ride getEntity(int rowIndex) {
        return templates.get(rowIndex);
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columns.get(columnIndex).getName();
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return columns.get(columnIndex).getColumnType();
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columns.get(columnIndex).isEditable();
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        if (value != null) {
            var ride = getEntity(rowIndex);
            columns.get(columnIndex).setValue(value, ride);
        }
    }

    public void deleteRow(int rowIndex) {
        templates.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }

    public void addRow(Ride ride) {
        int newRowIndex = templates.size();
        templates.add(ride);
        fireTableRowsInserted(newRowIndex, newRowIndex);
    }

    public void updateRow(Ride ride) {
        int rowIndex = templates.indexOf(ride);
        fireTableRowsUpdated(rowIndex, rowIndex);
    }
}
