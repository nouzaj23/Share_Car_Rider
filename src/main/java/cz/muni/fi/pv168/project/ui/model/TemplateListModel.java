package cz.muni.fi.pv168.project.ui.model;

import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.model.Ride;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class TemplateListModel extends AbstractListModel<Ride> {
    private final ArrayList<Ride> templates;

    public TemplateListModel(List<Ride> templates) {
        this.templates = new ArrayList<>(templates);
    }

    @Override
    public int getSize() {
        return templates.size();
    }

    @Override
    public Ride getElementAt(int index) {
        return templates.get(index);
    }

    public int indexOf(Ride ride) {
        return templates.indexOf(ride);
    }

    public void addRow(Ride template) {
        var categoriesSize = templates.size() - 1;
        templates.add(template);
        fireIntervalAdded(this, categoriesSize, categoriesSize);
    }

    public void deleteRow(int rowIndex) {
        templates.remove(rowIndex);
        fireIntervalRemoved(this, rowIndex, rowIndex);
    }

    public void updateRow(Category category) {
        int rowIndex = templates.indexOf(category);
        fireContentsChanged(this, rowIndex, rowIndex);
    }
}
