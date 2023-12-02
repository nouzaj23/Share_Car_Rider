package cz.muni.fi.pv168.project.ui.renderers;

import cz.muni.fi.pv168.project.business.model.Category;

import javax.swing.*;

public class CategoryRenderer extends AbstractRenderer<Category>{
    public CategoryRenderer() {
        super(Category.class);
    }

    @Override
    protected void updateLabel(JLabel label, Category value) {
        label.setText(value.getName());
    }
}
