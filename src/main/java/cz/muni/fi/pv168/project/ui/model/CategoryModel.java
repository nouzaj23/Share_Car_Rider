package cz.muni.fi.pv168.project.ui.model;

import cz.muni.fi.pv168.project.model.Category;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class CategoryModel extends AbstractTableModel implements EntityTableModel<Category> {
    private final CategoryListModel categoryListModel;
    private final List<Column<Category, ?>> columns = List.of(
            Column.editable("Name", String.class, Category::getName, Category::setName),
            Column.readonly("Rides", Integer.class, Category::getRides),
            Column.readonly("Distance", Integer.class, Category::getDistance)
    );

    public CategoryModel(CategoryListModel categoryListModel) {
        this.categoryListModel = categoryListModel;
    }

    @Override
    public int getRowCount() {
        return categoryListModel.getSize();
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
        return categoryListModel.getElementAt(rowIndex);
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
        }
    }

    public void deleteRow(int rowIndex) {
        categoryListModel.deleteRow(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }

    public void addRow(Category category) {
        int newRowIndex = categoryListModel.getSize();
        categoryListModel.addRow(category);
        fireTableRowsInserted(newRowIndex, newRowIndex);
    }

    public void updateRow(Category category) {
        int rowIndex = categoryListModel.indexOf(category);
        fireTableRowsUpdated(rowIndex, rowIndex);
    }
}
