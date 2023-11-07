package cz.muni.fi.pv168.project.ui.panels;

import cz.muni.fi.pv168.project.model.Category;
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

    public CategoriesPanel(CategoryModel categoryModel, Consumer<Integer> onSelectionChange) {
        this.categoryModel = categoryModel;
        this.onSelectionChange = onSelectionChange;

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
        return new CategoryDialog(Category.exampleCategory());
    }

    @Override
    public void addRow(Category entity) {
        categoryModel.addRow(entity);
    }
}
