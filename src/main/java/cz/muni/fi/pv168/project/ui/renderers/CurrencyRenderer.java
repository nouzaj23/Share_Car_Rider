package cz.muni.fi.pv168.project.ui.renderers;

import cz.muni.fi.pv168.project.business.model.Currency;

import javax.swing.*;

public class CurrencyRenderer  extends AbstractRenderer<Currency>{
    public CurrencyRenderer() {
        super(Currency.class);
    }

    @Override
    protected void updateLabel(JLabel label, Currency value) {
        label.setText(value.getName());
    }
}
