package cz.muni.fi.pv168.project.ui.panels;

import cz.muni.fi.pv168.project.business.guidProvider.GuidProvider;
import cz.muni.fi.pv168.project.business.model.Category;
import cz.muni.fi.pv168.project.business.model.Ride;
import cz.muni.fi.pv168.project.business.service.validation.Validator;
import cz.muni.fi.pv168.project.ui.actions.AddAction;
import cz.muni.fi.pv168.project.ui.actions.DeleteAction;
import cz.muni.fi.pv168.project.ui.actions.EditAction;
import cz.muni.fi.pv168.project.ui.dialog.CategoryDialog;
import cz.muni.fi.pv168.project.ui.dialog.EntityDialog;
import cz.muni.fi.pv168.project.ui.model.CategoryModel;
import cz.muni.fi.pv168.project.ui.panels.helper.PanelHelper;
import cz.muni.fi.pv168.project.ui.panels.helper.PopupMenuGenerator;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class CategoriesPanel extends AbstractPanel<Category> {

    private final CategoryModel categoryModel;
    private final Validator<Category> categoryValidator;
    private Action addAction;
    private Action editAction;
    private Action deleteAction;

    public CategoriesPanel(CategoryModel categoryModel, Validator<Category> categoryValidator) {
        this.categoryModel = categoryModel;
        this.categoryValidator = categoryValidator;

        setLayout(new BorderLayout());
        this.addAction = new AddAction<>(this);
        this.editAction = new EditAction<>(this);
        this.deleteAction = new DeleteAction<>(this);

        var actions = Arrays.asList(addAction, editAction, deleteAction);

        this.table = setUpTable(categoryModel, actions);
        addAction.setEnabled(true);
        editAction.setEnabled(false);
        deleteAction.setEnabled(false);

        var tableEmptySpaceClickAction = new MouseAdapter() {
            public void mousePressed(MouseEvent me) {
                if (me.getButton() == MouseEvent.BUTTON3) {
                    PopupMenuGenerator.generatePopupMenu(CategoriesPanel.this, actions).show(me.getComponent(), me.getX(), me.getY());
                }
            }
        };
        PanelHelper.createTopBar(this, table, null, null, Optional.of(tableEmptySpaceClickAction), actions);
    }


    private JTable setUpTable(CategoryModel categoryModel, List<Action> actions) {
        var table = new JTable(categoryModel);
        table.setAutoCreateRowSorter(true);
        table.getSelectionModel().addListSelectionListener(this::rowSelectionChanged);
        table.setComponentPopupMenu(PopupMenuGenerator.generatePopupMenu(this, actions));
        return table;
    }

    private void rowSelectionChanged(ListSelectionEvent listSelectionEvent) {
        var selectionModel = (ListSelectionModel) listSelectionEvent.getSource();
        var selectedCount = selectionModel.getSelectedItemsCount();
        
        var tripCount = 0;
        for (var index : table.getSelectedRows()) {
            tripCount += categoryModel.computeTotalRideCount(categoryModel.getCategories().get(index));
        }

        if (selectedCount == 0) {
            addAction.setEnabled(true);
            editAction.setEnabled(false);
            deleteAction.setEnabled(false);
        } else if (selectedCount == 1) {
            addAction.setEnabled(true);
            editAction.setEnabled(true);
            deleteAction.setEnabled(tripCount == 0);
        } else {
            addAction.setEnabled(true);
            editAction.setEnabled(false);
            deleteAction.setEnabled(tripCount == 0);
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
