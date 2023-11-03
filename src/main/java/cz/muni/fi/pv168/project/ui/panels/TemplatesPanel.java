package cz.muni.fi.pv168.project.ui.panels;

import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.model.Currency;
import cz.muni.fi.pv168.project.model.Ride;
import cz.muni.fi.pv168.project.ui.dialog.EntityDialog;
import cz.muni.fi.pv168.project.ui.dialog.TemplateDialog;
import cz.muni.fi.pv168.project.ui.model.CategoryListModel;
import cz.muni.fi.pv168.project.ui.model.ComboBoxModelAdapter;
import cz.muni.fi.pv168.project.ui.model.TemplateModel;
import cz.muni.fi.pv168.project.ui.panels.helper.PanelHelper;
import cz.muni.fi.pv168.project.ui.panels.helper.PopupMenuGenerator;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.util.function.Consumer;

public class TemplatesPanel extends AbstractPanel<Ride> {

    private final TemplateModel templateModel;
    private final Consumer<Integer> onSelectionChange;
    private final CategoryListModel categoryListModel;

    public TemplatesPanel(TemplateModel templateModel, CategoryListModel categoryListModel, Consumer<Integer> onSelectionChange) {
        this.templateModel = templateModel;
        this.categoryListModel = categoryListModel;
        this.onSelectionChange = onSelectionChange;

        setLayout(new BorderLayout());


        JTable table = setUpTable();

        PanelHelper.createTopBar(this, table, null, null);
    }

    private JTable setUpTable() {
        var table = new JTable(templateModel);

        table.setAutoCreateRowSorter(true);
        table.getSelectionModel().addListSelectionListener(this::rowSelectionChanged);
        var currencyComboBox = new JComboBox<>(Currency.values());
        table.setDefaultEditor(Currency.class, new DefaultCellEditor(currencyComboBox));
        var categoryComboBox = new JComboBox<>(new ComboBoxModelAdapter<>(categoryListModel));
        table.setDefaultEditor(Category.class, new DefaultCellEditor(categoryComboBox));
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
    public EntityDialog<Ride> getDialog() {
        return new TemplateDialog(Ride.exampleRide(), categoryListModel);
    }

    @Override
    public void addRow(Ride entity) {
        templateModel.addRow(entity);
    }
}
