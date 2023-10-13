package cz.muni.fi.pv168.project.ui.panels;

import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.ui.actions.AddAction;
import cz.muni.fi.pv168.project.ui.dialog.CategoryDialog;
import cz.muni.fi.pv168.project.ui.dialog.EntityDialog;

import javax.swing.*;
import java.awt.*;

public class CategoriesPanel extends AbstractPanel<Category> {

    private final JTable table;
    private final Action addAction = new AddAction<Category>(this);

    public CategoriesPanel() {
        setLayout(new BorderLayout());
        table = setUpTable();

        var toolbar = new JToolBar();
        toolbar.add(addAction);

        add(toolbar, BorderLayout.SOUTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }


    private JTable setUpTable() {
        return super.setUpTable(new String[]{"Name", "Number of rides", "Distance"},
                new Object[]{"Sluzobna jazda", "1", "130KM"});
    }

    @Override
    public EntityDialog<Category> getDialog() {
        return new CategoryDialog(Category.exampleCategory());
    }

    @Override
    public void addRow(Category entity) {
        getModel().addRow(new Object[] {entity.getName(), entity.getRides(), entity.getDistance()});
    }
}
