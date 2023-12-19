package cz.muni.fi.pv168.project.ui.model;

import cz.muni.fi.pv168.project.business.model.Category;
import cz.muni.fi.pv168.project.business.service.crud.CrudService;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryListModel extends AbstractListModel<Category> {

    private List<Category> categories;
    private final CrudService<Category> categoryCrudService;

    public CategoryListModel(CrudService<Category> categoryCrudService) {
        this.categoryCrudService = categoryCrudService;
        this.categories = categoryCrudService.findAll();
    }

    @Override
    public int getSize() {
        return categories.size();
    }

    @Override
    public Category getElementAt(int index) {
        return categories.get(index);
    }

    public void refresh() {
        this.categories = new ArrayList<>(categoryCrudService.findAll());
        fireContentsChanged(this, 0, getSize() - 1);
    }

}
