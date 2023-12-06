package cz.muni.fi.pv168.project.ui.dialog;

import cz.muni.fi.pv168.project.business.model.Currency;
import cz.muni.fi.pv168.project.business.service.validation.Validator;

import javax.swing.*;

public class CurrencyEditDialog extends EntityDialog<Currency> {

    private final JTextField name = new JTextField();
    private final Currency currency;

    public CurrencyEditDialog(Currency currency, Validator<Currency> currencyValidator) {
        super(currencyValidator);
        this.currency = currency;
        setValues();
        addFields();
    }

    private void setValues() {
        name.setText(currency.getCode());
    }

    private void addFields() {
        add("Currency:", name);
    }
    @Override
    Currency getEntity() {
        currency.setCode(name.getText());
        return currency;
    }
}
