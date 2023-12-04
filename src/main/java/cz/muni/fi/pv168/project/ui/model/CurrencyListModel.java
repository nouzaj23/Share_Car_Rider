package cz.muni.fi.pv168.project.ui.model;

import cz.muni.fi.pv168.project.business.model.Category;
import cz.muni.fi.pv168.project.business.model.Currency;
import cz.muni.fi.pv168.project.business.service.crud.CrudService;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class CurrencyListModel extends AbstractListModel<Currency> {

    private List<Currency> currencies;
    private final CrudService<Currency> currencyCrudService;

    public CurrencyListModel(CrudService<Currency> currencyCrudService) {
        this.currencyCrudService = currencyCrudService;
        this.currencies = currencyCrudService.findAll();
    }

    @Override
    public int getSize() {
        return currencies.size();
    }

    @Override
    public Currency getElementAt(int index) {
        return currencies.get(index);
    }

    public void refresh() {
        this.currencies = new ArrayList<>(currencyCrudService.findAll());
        fireContentsChanged(this, 0, getSize() - 1);
    }
}
