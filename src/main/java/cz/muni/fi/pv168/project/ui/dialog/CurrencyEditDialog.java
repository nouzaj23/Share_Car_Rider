package cz.muni.fi.pv168.project.ui.dialog;

import cz.muni.fi.pv168.project.business.model.Currency;

import javax.swing.*;

public class CurrencyEditDialog extends EntityDialog<Currency> {

    private final JTextField name = new JTextField();
    private final Currency currency;

    public CurrencyEditDialog(Currency currency) {
        this.currency = currency;
        setValues();
        addFields();
    }

    private void setValues() {
        name.setText(currency.getName());
    }

    private void addFields() {
        add("Currency:", name);
    }
    @Override
    Currency getEntity() {
        currency.setName(name.getText());
        return currency;
    }
}
