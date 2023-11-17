package cz.muni.fi.pv168.project.ui.model;

import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.model.Currency;
import cz.muni.fi.pv168.project.model.Ride;
import cz.muni.fi.pv168.project.ui.panels.CarRidesPanel;

import javax.swing.table.AbstractTableModel;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CarRidesModel extends AbstractTableModel implements EntityTableModel<Ride> {

    private List<Ride> rides;

    private CarRidesPanel linkedPannel;

    private final List<Column<Ride, ?>> columns = List.of(
            Column.editable("Name", String.class, Ride::getName, (ride, value) -> {
                ride.setName(value);
                triggerSafeUpdate(ride);
            }),
            Column.editable("Passengers", Integer.class, Ride::getPassengers, (ride, value) -> {
                ride.setPassengers(value);
                triggerSafeUpdate(ride);
            }),
            Column.editable("Currency", Currency.class, Ride::getCurrency, (ride, value) -> {
                ride.setCurrency(value);
                triggerSafeUpdate(ride);
            }),
            Column.editable("Fuel Expenses", Float.class, Ride::getFuelExpenses, (ride, value) -> {
                ride.setFuelExpenses(value);
                triggerSafeUpdate(ride);
            }),
            Column.editable("Category", Category.class, Ride::getCategory, (ride, value) -> {
                ride.setCategory(value);
                triggerSafeUpdate(ride);
            }),
            Column.readonly("From", String.class, Ride::getFrom),
            Column.readonly("To", String.class, Ride::getTo),
            Column.editable("Distance", Integer.class, Ride::getDistance, (ride, value) -> {
                ride.setDistance(value);
                linkedPannel.triggerStatsUpdate();
            }),
            Column.editable("Hours", Float.class, Ride::getHours, (ride, value) -> {
                ride.setHours(value);
                triggerSafeUpdate(ride);
            }),
            Column.editable("Date", LocalDate.class, Ride::getDate, (ride, value) -> {
                ride.setDate(value);
                triggerSafeUpdate(ride);
            })
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

    public CarRidesPanel getLinkedPannel() {
        return this.linkedPannel;
    }

    public void deleteRow(int rowIndex) {
        rides.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }

    public void addRow(Ride ride) {
        int newRowIndex = rides.size();
        ride.setCommitted(true);
        rides.add(ride);
        fireTableRowsInserted(newRowIndex, newRowIndex);
        linkedPannel.triggerStatsUpdate();
    }

    public void updateRow(Ride ride) {
        int rowIndex = rides.indexOf(ride);
        fireTableRowsUpdated(rowIndex, rowIndex);
    }

    private void triggerSafeUpdate(Ride ride) {
        if (ride != null) {
            int rowIndex = rides.indexOf(ride);
            fireTableDataChanged();
            fireTableRowsUpdated(rowIndex, rowIndex);
        }
    }

    public List<Ride> getList() {
        return new ArrayList<>(rides);
    }

    public void deleteAll(){
        while ( getRowCount() != 0 ){
            deleteRow(0);
        }
    }
}
