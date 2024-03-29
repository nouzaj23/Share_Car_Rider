package cz.muni.fi.pv168.project.ui.model;

import cz.muni.fi.pv168.project.business.model.Category;
import cz.muni.fi.pv168.project.business.model.Ride;
import cz.muni.fi.pv168.project.business.service.crud.CrudService;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CategoryModel extends AbstractTableModel implements EntityTableModel<Category> {
    private List<Category> categories;
    private final CrudService<Category> categoryCrudService;
    private final CategoryListModel categoryListModel;
    private CarRidesModel rideModel;
    private final List<Column<Category, ?>> columns = List.of(
            Column.editable("Name", String.class, Category::getName, Category::setName),
            Column.readonly("Rides", Integer.class, this::computeTotalRideCount),
            Column.readonly("Distance", Integer.class, this::computeTotalDistance)
    );

    public CategoryModel(CrudService<Category> categoryCrudService, CategoryListModel categoryListModel) {
        this.categoryCrudService = categoryCrudService;
        this.categories = new ArrayList<>(categoryCrudService.findAll());
        this.categoryListModel = categoryListModel;
    }

    public void setRidesModel(CarRidesModel rideModel) {
        this.rideModel = rideModel;
    }

    public List<Category> getCategories() {
        return Collections.unmodifiableList(categories);
    }

    @Override
    public int getRowCount() {
        return categories.size();
    }

    @Override
    public int getColumnCount() {
        return columns.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        var category = getEntity(rowIndex);
        return columns.get(columnIndex).getValue((category));
    }
    @Override
    public Category getEntity(int rowIndex) {
        return categories.get(rowIndex);
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
            var category = getEntity(rowIndex);
            columns.get(columnIndex).setValue(value, category);
            updateRow(category);
            categoryListModel.refresh();
        }
    }

    public void deleteRow(int rowIndex) {
        var categoryToBeDeleted = getEntity(rowIndex);
        categoryCrudService.deleteByGuid(categoryToBeDeleted.getGuid());
        categories.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
        categoryListModel.refresh();
    }

    public void addRow(Category category) {
        categoryCrudService.create(category)
                .intoException();
        int newRowIndex = categories.size();
        categories.add(category);
        fireTableRowsInserted(newRowIndex, newRowIndex);
        categoryListModel.refresh();
    }

    public void updateRow(Category category) {
        categoryCrudService.update(category)
                .intoException();
        int rowIndex = categories.indexOf(category);
        fireTableRowsUpdated(rowIndex, rowIndex);
        categoryListModel.refresh();
    }

    public void refresh() {
        this.categories = new ArrayList<>(categoryCrudService.findAll());
        fireTableDataChanged();
    }

    public int computeTotalDistance(Category category) {
        return rideModel.getRides().stream()
            .filter(ride -> ride.getCategory().getGuid().equals(category.getGuid()))
            .mapToInt(Ride::getDistance)
            .sum();
    }

    public int computeTotalRideCount(Category category) {
        return rideModel.getRides().stream()
            .filter(ride -> ride.getCategory().getGuid().equals(category.getGuid())).toList().size();
    }

    public List<Ride> getRidesForCategories(List<Category> category) {
        return rideModel.getRides().stream()
            .filter(ride -> category.stream().anyMatch(cat -> cat.getGuid().equals(ride.getCategory().getGuid())))
            .toList();
    }
}
