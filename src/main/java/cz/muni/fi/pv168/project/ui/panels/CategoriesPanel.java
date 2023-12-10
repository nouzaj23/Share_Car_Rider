package cz.muni.fi.pv168.project.ui.panels;

import cz.muni.fi.pv168.project.business.guidProvider.GuidProvider;
import cz.muni.fi.pv168.project.business.model.Category;
import cz.muni.fi.pv168.project.business.model.Ride;
import cz.muni.fi.pv168.project.business.service.validation.Validator;
import cz.muni.fi.pv168.project.ui.dialog.CategoryDialog;
import cz.muni.fi.pv168.project.ui.dialog.EntityDialog;
import cz.muni.fi.pv168.project.ui.model.CategoryModel;
import cz.muni.fi.pv168.project.ui.panels.helper.PanelHelper;
import cz.muni.fi.pv168.project.ui.panels.helper.PopupMenuGenerator;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.util.function.Consumer;

public class CategoriesPanel extends AbstractPanel<Category> {

    private final Consumer<Integer> onSelectionChange;
    private final CategoryModel categoryModel;
    private final Validator<Category> categoryValidator;

    public CategoriesPanel(CategoryModel categoryModel, Consumer<Integer> onSelectionChange, Validator<Category> categoryValidator) {
        this.categoryModel = categoryModel;
        this.onSelectionChange = onSelectionChange;
        this.categoryValidator = categoryValidator;

        setLayout(new BorderLayout());
        this.table = setUpTable(categoryModel);

        PanelHelper.createTopBar(this, table, null, null);
    }


    private JTable setUpTable(CategoryModel categoryModel) {
        var table = new JTable(categoryModel);
        table.setAutoCreateRowSorter(true);
        table.getSelectionModel().addListSelectionListener(this::rowSelectionChanged);
        table.setComponentPopupMenu(PopupMenuGenerator.generatePopupMenu(this));
        return table;
    }

    private void rowSelectionChanged(ListSelectionEvent listSelectionEvent) {
        var selectionModel = (ListSelectionModel) listSelectionEvent.getSource();
        var count = selectionModel.getSelectedItemsCount();
        if (onSelectionChange != null) {
            onSelectionChange.accept(count);
        }
    }

    @Override
    public EntityDialog<Category> getDialog() {
        return new CategoryDialog(new Category(GuidProvider.newGuid(), ""), categoryValidator);
    }

    @Override
    public EntityDialog<Category> getDialog(Category entity) {
        return new CategoryDialog(entity, categoryValidator);
    }

    @Override
    public EntityDialog<Category> getEditDialog(Category entity) {
        return getDialog(entity);
    }

    @Override
    public void addRow(Category entity) {
        categoryModel.addRow(entity);
    }

    @Override
    public void deleteRow(int rowIndex) {
        categoryModel.deleteRow(rowIndex);
    }

    @Override
    public void editRow(Category newEntity, Ride oldRide) {
        categoryModel.updateRow(newEntity);
    }
}
