package cz.muni.fi.pv168.project.ui.model;

import cz.muni.fi.pv168.project.business.model.Category;
import cz.muni.fi.pv168.project.business.model.Template;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class TemplateListModel extends AbstractListModel<Template> {
    private final ArrayList<Template> templates;

    public TemplateListModel(List<Template> templates) {
        this.templates = new ArrayList<>(templates);
    }

    @Override
    public int getSize() {
        return templates.size();
    }

    @Override
    public Template getElementAt(int index) {
        return templates.get(index);
    }

    public int indexOf(Template template) {
        return templates.indexOf(template);
    }

    public void addRow(Template template) {
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
