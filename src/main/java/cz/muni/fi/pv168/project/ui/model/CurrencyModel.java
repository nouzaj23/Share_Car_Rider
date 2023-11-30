package cz.muni.fi.pv168.project.ui.model;

import cz.muni.fi.pv168.project.business.model.Currency;
import cz.muni.fi.pv168.project.business.service.crud.CrudService;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class CurrencyModel extends AbstractTableModel implements EntityTableModel<Currency> {
    private List<Currency> currencies;
    private final CrudService<Currency> currencyCrudService;
    private final CurrencyListModel currencyListModel;
    private final List<Column<Currency, ?>> columns = List.of(
            Column.editable("Name", String.class, Currency::getName, Currency::setName),
            Column.readonly("Rate", Float.class, Currency::getRate)
    );

    public CurrencyModel(CrudService<Currency> currencyCrudService, CurrencyListModel currencyListModel) {
        this.currencyCrudService = currencyCrudService;
        this.currencies = new ArrayList<>(currencyCrudService.findAll());
        this.currencyListModel = currencyListModel;
    }

    @Override
    public int getRowCount() {
        return currencies.size();
    }

    @Override
    public int getColumnCount() {
        return columns.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        var category = getEntity(rowIndex);
        return columns.get(columnIndex).getValue((category));
    }
    @Override
    public Currency getEntity(int rowIndex) {
        return currencies.get(rowIndex);
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columns.get(columnIndex).getName();
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return columns.get(columnIndex).getColumnType();
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columns.get(columnIndex).isEditable();
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        if (value != null) {
            var category = getEntity(rowIndex);
            columns.get(columnIndex).setValue(value, category);
            updateRow(category);
            currencyListModel.refresh();
        }
    }

    public void deleteRow(int rowIndex) {
        var categoryToBeDeleted = getEntity(rowIndex);
        currencyCrudService.deleteByGuid(categoryToBeDeleted.getGuid());
        currencies.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
        currencyListModel.refresh();
    }

    public void addRow(Currency category) {
        currencyCrudService.create(category)
                .intoException();
        int newRowIndex = currencies.size();
        currencies.add(category);
        fireTableRowsInserted(newRowIndex, newRowIndex);
        currencyListModel.refresh();
    }

    public void updateRow(Currency category) {
        currencyCrudService.update(category)
                .intoException();
        int rowIndex = currencies.indexOf(category);
        fireTableRowsUpdated(rowIndex, rowIndex);
        currencyListModel.refresh();
    }

    public void refresh() {
        this.currencies = new ArrayList<>(currencyCrudService.findAll());
        fireTableDataChanged();
    }
}
