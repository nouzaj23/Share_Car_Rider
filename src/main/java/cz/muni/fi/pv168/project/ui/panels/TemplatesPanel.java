package cz.muni.fi.pv168.project.ui.panels;

import cz.muni.fi.pv168.project.business.model.Category;
import cz.muni.fi.pv168.project.business.model.Currency;
import cz.muni.fi.pv168.project.business.model.Ride;
import cz.muni.fi.pv168.project.business.model.Template;
import cz.muni.fi.pv168.project.ui.dialog.EntityDialog;
import cz.muni.fi.pv168.project.ui.dialog.TemplateDialog;
import cz.muni.fi.pv168.project.ui.model.CategoryListModel;
import cz.muni.fi.pv168.project.ui.model.ComboBoxModelAdapter;
import cz.muni.fi.pv168.project.ui.model.CurrencyListModel;
import cz.muni.fi.pv168.project.ui.model.TemplateModel;
import cz.muni.fi.pv168.project.ui.panels.helper.PanelHelper;
import cz.muni.fi.pv168.project.ui.panels.helper.PopupMenuGenerator;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.util.function.Consumer;

public class TemplatesPanel extends AbstractPanel<Template> {

    private final TemplateModel templateModel;
    private final Consumer<Integer> onSelectionChange;
    private final CategoryListModel categoryListModel;
    private final CurrencyListModel currencyListModel;

    public TemplatesPanel(TemplateModel templateModel, CategoryListModel categoryListModel, CurrencyListModel currencyListModel, Consumer<Integer> onSelectionChange) {
        this.templateModel = templateModel;
        this.categoryListModel = categoryListModel;
        this.onSelectionChange = onSelectionChange;
        this.currencyListModel = currencyListModel;

        setLayout(new BorderLayout());


        this.table = setUpTable();

        PanelHelper.createTopBar(this, table, null, null);
    }

    private JTable setUpTable() {
        var table = new JTable(templateModel);

        table.setAutoCreateRowSorter(true);
        table.getSelectionModel().addListSelectionListener(this::rowSelectionChanged);
        var currencyComboBox = new JComboBox<>(new ComboBoxModelAdapter<>(currencyListModel));
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
    public EntityDialog<Template> getDialog() {
        return new TemplateDialog(Template.exampleTemplate(), categoryListModel, currencyListModel);
    }

    @Override
    public EntityDialog<Template> getDialog(Template entity) {
        return new TemplateDialog(entity, categoryListModel, currencyListModel);
    }

    @Override
    public EntityDialog<Template> getEditDialog(Template entity) {
        return getDialog(entity);
    }

    @Override
    public void addRow(Template entity) {
        templateModel.addRow(entity);
    }

    @Override
    public void deleteRow(int rowIndex) {
        templateModel.deleteRow(rowIndex);
    }

    @Override
    public void editRow(Template newEntity, Ride oldRide) {
        templateModel.updateRow(newEntity);
    }
}
