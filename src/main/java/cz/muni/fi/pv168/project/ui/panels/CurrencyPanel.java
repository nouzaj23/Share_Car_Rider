package cz.muni.fi.pv168.project.ui.panels;

import cz.muni.fi.pv168.project.business.guidProvider.GuidProvider;
import cz.muni.fi.pv168.project.business.model.Currency;
import cz.muni.fi.pv168.project.business.model.Ride;
import cz.muni.fi.pv168.project.business.service.validation.Validator;
import cz.muni.fi.pv168.project.ui.actions.AddAction;
import cz.muni.fi.pv168.project.ui.actions.DeleteAction;
import cz.muni.fi.pv168.project.ui.actions.EditAction;
import cz.muni.fi.pv168.project.ui.dialog.CurrencyDialog;
import cz.muni.fi.pv168.project.ui.dialog.CurrencyEditDialog;
import cz.muni.fi.pv168.project.ui.dialog.EntityDialog;
import cz.muni.fi.pv168.project.ui.model.CarRidesModel;
import cz.muni.fi.pv168.project.ui.model.CurrencyModel;
import cz.muni.fi.pv168.project.ui.panels.helper.PanelHelper;
import cz.muni.fi.pv168.project.ui.panels.helper.PopupMenuGenerator;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Optional;
import java.util.List;
import java.awt.*;

public class CurrencyPanel extends AbstractPanel<Currency> {
    private final CurrencyModel currencyModel;
    private CarRidesModel rideModel;
    private final Validator<Currency> currencyValidator;
    private Action addAction;
    private Action editAction;
    private Action deleteAction;

    public CurrencyPanel(CurrencyModel currencyModel, Validator<Currency> currencyValidator, CarRidesModel rideModel) {
        this.currencyModel = currencyModel;
        this.currencyValidator = currencyValidator;
        this.rideModel = rideModel;

        setLayout(new BorderLayout());
        this.addAction = new AddAction<>(this);
        this.editAction = new EditAction<>(this);
        this.deleteAction = new DeleteAction<>(this);

        var actions = Arrays.asList(addAction, editAction, deleteAction);

        this.table = setUpTable(currencyModel, actions);
        addAction.setEnabled(true);
        editAction.setEnabled(false);
        deleteAction.setEnabled(false);

        var tableEmptySpaceClickAction = new MouseAdapter() {
            public void mousePressed(MouseEvent me) {
                if (me.getButton() == MouseEvent.BUTTON3) {
                    PopupMenuGenerator.generatePopupMenu(CurrencyPanel.this, actions).show(me.getComponent(), me.getX(), me.getY());
                }
            }
        };
        PanelHelper.createTopBar(this, table, null, null, Optional.of(tableEmptySpaceClickAction), actions);
    }


    private JTable setUpTable(CurrencyModel categoryModel, List<Action> actions) {
        var table = new JTable(categoryModel);
        table.setAutoCreateRowSorter(true);
        table.getSelectionModel().addListSelectionListener(this::rowSelectionChanged);
        table.setComponentPopupMenu(PopupMenuGenerator.generatePopupMenu(this, actions));
        return table;
    }

    private void rowSelectionChanged(ListSelectionEvent listSelectionEvent) {
        var selectionModel = (ListSelectionModel) listSelectionEvent.getSource();
        var selectedCount = selectionModel.getSelectedItemsCount();

        var containsLinkedCurrency = false;

        var rides = rideModel.getRides();
        var currencies = currencyModel.getCurrencies();
        for (var index : table.getSelectedRows()) {
            containsLinkedCurrency |= rides.stream().anyMatch(ride -> ride.getCurrency().equals(currencies.get(index)));
        }

        if (selectedCount == 0) {
            addAction.setEnabled(true);
            editAction.setEnabled(false);
            deleteAction.setEnabled(false);
        } else if (selectedCount == 1) {
            addAction.setEnabled(true);
            editAction.setEnabled(true);
            deleteAction.setEnabled(!containsLinkedCurrency);
        } else {
            addAction.setEnabled(true);
            editAction.setEnabled(false);
            deleteAction.setEnabled(!containsLinkedCurrency);
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
