package cz.muni.fi.pv168.project.ui.model;

import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.model.Currency;
import cz.muni.fi.pv168.project.model.Ride;
import cz.muni.fi.pv168.project.ui.panels.CarRidesPanel;

import javax.swing.table.AbstractTableModel;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CarRidesModel extends AbstractTableModel implements EnitityTableModel<Ride> {

    private final List<Ride> rides;

    private CarRidesPanel linkedPannel;

    private final List<Column<Ride, ?>> columns = List.of(
            Column.editable("Name", String.class, Ride::getName, Ride::setName),
            Column.editable("Passengers", Integer.class, Ride::getPassengers, Ride::setPassengers),
            Column.editable("Currency", Currency.class, Ride::getCurrency, Ride::setCurrency),
            Column.editable("Category", Category.class, Ride::getCategory, Ride::setCategory),
            Column.readonly("From", String.class, Ride::getFrom),
            Column.readonly("To", String.class, Ride::getTo),
            Column.editable("Distance", Integer.class, Ride::getDistance, (ride, value) -> {
                ride.setDistance(value);
                linkedPannel.triggerTotalDistanceUpdate();
            }),
            Column.editable("Hours", Float.class, Ride::getHours, Ride::setHours),
            Column.editable("Date", LocalDate.class, Ride::getDate, Ride::setDate)
    );

    public CarRidesModel(Collection<Ride> rides) {
        this.rides = new ArrayList<>(rides);
    }

    @Override
    public int getRowCount() {
        return rides.size();
    }

    @Override
    public int getColumnCount() {
        return columns.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        var ride = getEntity(rowIndex);
        return columns.get(columnIndex).getValue((ride));
    }

    @Override
    public Ride getEntity(int rowIndex) {
        return rides.get(rowIndex);
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

    public void setLinkedPannel(CarRidesPanel linkedPannel) {
        this.linkedPannel = linkedPannel;
    }

    public void deleteRow(int rowIndex) {
        rides.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }

    public void addRow(Ride ride) {
        int newRowIndex = rides.size();
        ride.setCommitted(true);
        rides.add(ride);
        linkedPannel.triggerTotalDistanceUpdate();
        fireTableRowsInserted(newRowIndex, newRowIndex);
    }

    public void updateRow(Ride ride) {
        int rowIndex = rides.indexOf(ride);
        fireTableRowsUpdated(rowIndex, rowIndex);
    }
}
