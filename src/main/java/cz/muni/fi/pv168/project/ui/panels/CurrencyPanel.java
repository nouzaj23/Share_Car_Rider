package cz.muni.fi.pv168.project.ui.panels;

import cz.muni.fi.pv168.project.business.guidProvider.GuidProvider;
import cz.muni.fi.pv168.project.business.model.Currency;
import cz.muni.fi.pv168.project.business.model.Ride;
import cz.muni.fi.pv168.project.business.service.validation.Validator;
import cz.muni.fi.pv168.project.ui.dialog.CurrencyDialog;
import cz.muni.fi.pv168.project.ui.dialog.CurrencyEditDialog;
import cz.muni.fi.pv168.project.ui.dialog.EntityDialog;
import cz.muni.fi.pv168.project.ui.model.CurrencyModel;
import cz.muni.fi.pv168.project.ui.panels.helper.PanelHelper;
import cz.muni.fi.pv168.project.ui.panels.helper.PopupMenuGenerator;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.util.function.Consumer;

public class CurrencyPanel extends AbstractPanel<Currency> {
    private final Consumer<Integer> onSelectionChange;
    private final CurrencyModel currencyModel;
    private final Validator<Currency> currencyValidator;

    public CurrencyPanel(CurrencyModel currencyModel, Consumer<Integer> onSelectionChange, Validator<Currency> currencyValidator) {
        this.currencyModel = currencyModel;
        this.onSelectionChange = onSelectionChange;
        this.currencyValidator = currencyValidator;

        setLayout(new BorderLayout());
        this.table = setUpTable(currencyModel);

        PanelHelper.createTopBar(this, table, null, null);
    }


    private JTable setUpTable(CurrencyModel categoryModel) {
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
    public EntityDialog<Currency> getDialog() {
        return new CurrencyDialog(new Currency(GuidProvider.newGuid(), "", 0), currencyValidator);
    }

    @Override
    public EntityDialog<Currency> getDialog(Currency entity) {
        return new CurrencyDialog(entity, currencyValidator);
    }

    @Override
    public EntityDialog<Currency> getEditDialog(Currency entity) {
        return new CurrencyEditDialog(entity, currencyValidator);
    }

    @Override
    public void addRow(Currency entity) {
        currencyModel.addRow(entity);
    }

    @Override
    public void deleteRow(int rowIndex) {
        currencyModel.deleteRow(rowIndex);
    }

    @Override
    public void editRow(Currency newEntity, Ride oldRide) {
        currencyModel.updateRow(newEntity);
    }
}
