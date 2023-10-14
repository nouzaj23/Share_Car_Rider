package cz.muni.fi.pv168.project.ui.model;

import cz.muni.fi.pv168.project.model.Category;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryListModel extends AbstractListModel<Category> {

    private final ArrayList<Category> categories;

    public CategoryListModel(List<Category> categories) {
        this.categories = new ArrayList<>(categories);
    }

    @Override
    public int getSize() {
        return categories.size();
    }

    @Override
    public Category getElementAt(int index) {
        return categories.get(index);
    }

    public int indexOf(Category category) {
        return categories.indexOf(category);
    }

    public void addRow(Category category) {
        var categoriesSize = categories.size() - 1;
        categories.add(category);
        fireIntervalAdded(this, categoriesSize, categoriesSize);
    }

    public void deleteRow(int rowIndex) {
        categories.remove(rowIndex);
        fireIntervalRemoved(this, rowIndex, rowIndex);
    }

    public void updateRow(Category category) {
        int rowIndex = categories.indexOf(category);
        fireContentsChanged(this, rowIndex, rowIndex);
    }
}
