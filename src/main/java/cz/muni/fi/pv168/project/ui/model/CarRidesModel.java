package cz.muni.fi.pv168.project.ui.model;

import cz.muni.fi.pv168.project.business.model.Category;
import cz.muni.fi.pv168.project.business.model.Currency;
import cz.muni.fi.pv168.project.business.model.Ride;
import cz.muni.fi.pv168.project.business.service.crud.CrudService;
import cz.muni.fi.pv168.project.ui.panels.CarRidesPanel;

import javax.swing.table.AbstractTableModel;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CarRidesModel extends AbstractTableModel implements EntityTableModel<Ride> {

    private List<Ride> rides;
    private final CrudService<Ride> rideCrudService;
    private CarRidesPanel linkedPanel;
    private CategoryModel categoryModel;

    private final List<Column<Ride, ?>> columns = List.of(
            Column.editable("Name", String.class, Ride::getName, Ride::setName),
            Column.editable("Passengers", Integer.class, Ride::getPassengers, Ride::setPassengers),
            Column.editable("Currency", Currency.class, Ride::getCurrency, Ride::setCurrency),
            Column.editable("Fuel Expenses", Float.class, Ride::getFuelExpenses, (ride, value) -> {
                ride.setFuelExpenses(value);
                linkedPanel.triggerStatsUpdate();
            }),
            Column.editable("Category", Category.class, Ride::getCategory, Ride::setCategory),
            Column.readonly("From", String.class, Ride::getFrom),
            Column.readonly("To", String.class, Ride::getTo),
            Column.editable("Distance", Integer.class, Ride::getDistance, Ride::setDistance),
            Column.editable("Hours", Float.class, Ride::getHours, Ride::setHours),
            Column.readonly("Date", LocalDate.class, Ride::getDate)
    );

    public CarRidesModel(CrudService<Ride> rideCrudService, CategoryModel categoryModel) {
        this.rideCrudService = rideCrudService;
        this.rides = new ArrayList<>(rideCrudService.findAll());
        this.categoryModel = categoryModel;
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
            updateRow(ride);
            
            categoryModel.refresh();
        }
    }

    public void setLinkedPanel(CarRidesPanel linkedPanel) {
        this.linkedPanel = linkedPanel;
    }

    public CarRidesPanel getLinkedPanel() {
        return this.linkedPanel;
    }

    public void deleteRow(int rowIndex) {
        var rideToBeDeleted = getEntity(rowIndex);
        rideCrudService.deleteByGuid(rideToBeDeleted.getGuid());
        rides.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }

    public void addRow(Ride ride) {
        rideCrudService.create(ride)
                .intoException();
        int newRowIndex = rides.size();
        rides.add(ride);
        fireTableRowsInserted(newRowIndex, newRowIndex);
    }

    public void updateRow(Ride ride) {
        rideCrudService.update(ride)
                .intoException();
        int rowIndex = rides.indexOf(ride);
        fireTableRowsUpdated(rowIndex, rowIndex);
    }

    public void refresh() {
        this.rides = new ArrayList<>(rideCrudService.findAll());
        fireTableDataChanged();
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
