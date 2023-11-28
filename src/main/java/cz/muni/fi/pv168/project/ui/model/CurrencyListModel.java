package cz.muni.fi.pv168.project.ui.model;

import cz.muni.fi.pv168.project.business.model.Currency;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class CurrencyListModel extends AbstractListModel<Currency> {

    private final ArrayList<Currency> currencies;

    public CurrencyListModel(List<Currency> currencies) {
        this.currencies = new ArrayList<>(currencies);
    }

    @Override
    public int getSize() {
        return currencies.size();
    }

    @Override
    public Currency getElementAt(int index) {
        return currencies.get(index);
    }

    public int indexOf(Currency currency) {
        return currencies.indexOf(currency);
    }

    public void addRow(Currency currency) {
        var currenciesSize = currencies.size() - 1;
        currencies.add(currency);
        fireIntervalAdded(this, currenciesSize, currenciesSize);
    }

    public void deleteRow(int rowIndex) {
        currencies.remove(rowIndex);
        fireIntervalRemoved(this, rowIndex, rowIndex);
    }

    public void updateRow(Currency currency) {
        int rowIndex = currencies.indexOf(currency);
        fireContentsChanged(this, rowIndex, rowIndex);
    }
}
