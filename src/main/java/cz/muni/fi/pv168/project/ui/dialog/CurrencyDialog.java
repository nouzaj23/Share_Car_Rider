package cz.muni.fi.pv168.project.ui.dialog;

import cz.muni.fi.pv168.project.business.model.Currency;

import javax.swing.*;

public class CurrencyDialog extends EntityDialog<Currency>{
    private final JTextField name = new JTextField();
    private final JTextField rate = new JTextField();
    private final Currency currency;

    public CurrencyDialog(Currency currency) {
        this.currency = currency;
        setValues();
        addFields();
    }

    private void setValues() {
        name.setText(currency.getName());
    }

    private void addFields() {
        add("Currency:", name);
        add("Rate", rate);
    }

    @Override
    Currency getEntity() {
        currency.setName(name.getText());
        return currency;
    }

}


