package cz.muni.fi.pv168.project.ui.filters.matchers.ride;


import cz.muni.fi.pv168.project.model.Currency;
import cz.muni.fi.pv168.project.model.Ride;
import cz.muni.fi.pv168.project.ui.filters.matchers.EntityMatcher;

import java.util.Collection;
import java.util.List;

public class RideCurrencyMatcher extends EntityMatcher<Ride> {
    private final Collection<Currency> selectedCurrencies;

    public RideCurrencyMatcher(Currency selectedCurrency) {
        this.selectedCurrencies = List.of(selectedCurrency);
    }

    @Override
    public boolean evaluate(Ride ride) {
        return selectedCurrencies.stream()
                .anyMatch(d -> ride.getCurrency().equals(d));
    }
}
