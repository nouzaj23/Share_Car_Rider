package cz.muni.fi.pv168.project.ui.dialog;

import cz.muni.fi.pv168.project.business.model.Category;
import cz.muni.fi.pv168.project.business.service.validation.Validator;

import javax.swing.*;

public class CategoryDialog extends EntityDialog<Category>{
    private final JTextField name = new JTextField();
    private final Category category;

    public CategoryDialog(Category category, Validator<Category> categoryValidator) {
        super(categoryValidator);
        this.category = category;
        setValues();
        addFields();
    }

    private void setValues() {
        name.setText(category.getName());
    }

    private void addFields() {
        add("Ride name:", name);
        addErrorPanel();
    }

    @Override
    Category getEntity() {
        category.setName(name.getText());
        return category;
    }
}
