package cz.muni.fi.pv168.project.ui.dialog;

import cz.muni.fi.pv168.project.business.model.Currency;

import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

public class CurrencyDialog extends EntityDialog<Currency>{
    private final JTextField name = new JTextField();
    private final JSpinner rate = new JSpinner(new SpinnerNumberModel(1, Float.MIN_VALUE, Float.MAX_VALUE, 1));
    private final Currency currency;

    public CurrencyDialog(Currency currency) {
        this.currency = currency;
        setValues();
        addFields();
    }

    private void setValues() {
        name.setText(currency.getCode());
        rate.setValue(currency.getConversionRatio());
    }

    private void addFields() {
        add("Currency:", name);
        add("Rate", rate);
    }

    @Override
    Currency getEntity() {
        currency.setCode(name.getText());
        currency.setConversionRatio(((Number) rate.getValue()).floatValue());
        return currency;
    }

}


