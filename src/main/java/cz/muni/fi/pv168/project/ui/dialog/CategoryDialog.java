package cz.muni.fi.pv168.project.ui.dialog;

import cz.muni.fi.pv168.project.business.model.Category;

import javax.swing.*;

public class CategoryDialog extends EntityDialog<Category>{
    private final JTextField name = new JTextField();
    private final Category category;

    public CategoryDialog(Category category) {
        this.category = category;
        setValues();
        addFields();
    }

    private void setValues() {
        name.setText(category.getName());
    }

    private void addFields() {
        add("Ride name:", name);
    }

    @Override
    Category getEntity() {
        category.setName(name.getText());
        return category;
    }
}
