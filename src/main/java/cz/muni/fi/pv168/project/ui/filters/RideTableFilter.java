package cz.muni.fi.pv168.project.ui.filters;

import cz.muni.fi.pv168.project.business.model.Category;
import cz.muni.fi.pv168.project.business.model.Currency;
import cz.muni.fi.pv168.project.business.model.Ride;
import cz.muni.fi.pv168.project.ui.filters.matchers.EntityMatcher;
import cz.muni.fi.pv168.project.ui.filters.matchers.EntityMatchers;
import cz.muni.fi.pv168.project.ui.filters.matchers.ride.*;
import cz.muni.fi.pv168.project.ui.model.CarRidesModel;
import cz.muni.fi.pv168.project.ui.panels.CarRidesPanel;

import javax.swing.table.TableRowSorter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public class RideTableFilter {
    private final RideCompoundMatcher rideCompoundMatcher;
    private final CarRidesPanel carRidesPanel;

    public RideTableFilter(TableRowSorter<CarRidesModel> rowSorter) {
        rideCompoundMatcher = new RideCompoundMatcher(rowSorter);
        rowSorter.setRowFilter(rideCompoundMatcher);
        carRidesPanel = rowSorter.getModel().getLinkedPanel();
    }

    public void filterCategory(List<Category> selectedItems) {
        List<EntityMatcher<Ride>> matchers = new ArrayList<>();
        selectedItems.forEach(elem -> matchers.add(new RideCategoryMatcher(elem)));
        if (matchers.isEmpty()) {
            rideCompoundMatcher.setCategoryMatcher(null);
        }
        rideCompoundMatcher.setCategoryMatcher(new RideCommonCompoundMatcher(matchers));
        carRidesPanel.triggerStatsUpdate();
    }

    public void filterCurrency(List<Currency> selectedItems) {
        List<EntityMatcher<Ride>> matchers = new ArrayList<>();
        selectedItems.forEach(elem -> matchers.add(new RideCurrencyMatcher(elem)));
        if (matchers.isEmpty()) {
            rideCompoundMatcher.setCurrencyMatcher(null);
        }
        rideCompoundMatcher.setCurrencyMatcher(new RideCommonCompoundMatcher(matchers));
        carRidesPanel.triggerStatsUpdate();
    }

    public void filterFrom(String filterString) {
        rideCompoundMatcher.setFromMatcher(createCommonStringCompoundMatcher(filterString, Ride::getFrom));
        carRidesPanel.triggerStatsUpdate();
    }

    public void filterTo(String filterString) {
        rideCompoundMatcher.setToMatcher(createCommonStringCompoundMatcher(filterString, Ride::getTo));
        carRidesPanel.triggerStatsUpdate();
    }

    private RideCommonCompoundMatcher createCommonStringCompoundMatcher(
            String filterString, Function<Ride, String> getter) {
        List<EntityMatcher<Ride>> matchers = new ArrayList<>();
        String[] substrings = filterString.split(",");
        Arrays.stream(substrings)
                .map(String::trim)
                .forEach(elem -> {
                    if (!elem.isEmpty()) {
                        matchers.add(new RideStringColumnMatcher<>(elem, getter));
                    }
                });

        if (matchers.isEmpty()) {
            return null;
        }

        return new RideCommonCompoundMatcher(matchers);
    }

    public void filterMin(String filterString) {
        try {
            var minValue = Integer.parseInt(filterString);
            rideCompoundMatcher.setMinPassengers(new RideNumericValueMatcher(minValue, Ride::getPassengers, (a, b) -> a.intValue() <= b.intValue()));
        } catch (NumberFormatException e) {
            rideCompoundMatcher.setMinPassengers(null);
        }
        finally {
            carRidesPanel.triggerStatsUpdate();
        }
    }

    public void filterMax(String filterString) {
        try {
            var minValue = Integer.parseInt(filterString);
            rideCompoundMatcher.setMaxPassengers(new RideNumericValueMatcher(minValue, Ride::getPassengers, (a, b) -> a.intValue() >= b.intValue()));
        } catch (NumberFormatException e) {
            rideCompoundMatcher.setMaxPassengers(null);
        }
        finally {
            carRidesPanel.triggerStatsUpdate();
        }
    }

    public void resetFilter() {
        rideCompoundMatcher.resetFilter();
        carRidesPanel.triggerStatsUpdate();
    }

    private static class RideCompoundMatcher extends EntityMatcher<Ride> {

        private final TableRowSorter<CarRidesModel> rowSorter;
        private EntityMatcher<Ride> categoryMatcher = EntityMatchers.all();
        private EntityMatcher<Ride> currencyMatcher = EntityMatchers.all();
        private EntityMatcher<Ride> fromMatcher = EntityMatchers.all();
        private EntityMatcher<Ride> toMatcher = EntityMatchers.all();
        private EntityMatcher<Ride> minPassengers = EntityMatchers.all();
        private EntityMatcher<Ride> maxPassengers = EntityMatchers.all();

        private RideCompoundMatcher(TableRowSorter<CarRidesModel> rowSorter) {
            this.rowSorter = rowSorter;
        }

        private void resetFilter() {
            this.categoryMatcher = EntityMatchers.all();
            this.currencyMatcher = EntityMatchers.all();
            this.fromMatcher = EntityMatchers.all();
            this.toMatcher = EntityMatchers.all();
            this.minPassengers = EntityMatchers.all();
            this.maxPassengers = EntityMatchers.all();
            rowSorter.sort();
        }
        private void setCategoryMatcher(EntityMatcher<Ride> categoryMatcher) {
            this.categoryMatcher = categoryMatcher;
            rowSorter.sort();
        }

        private void setCurrencyMatcher(EntityMatcher<Ride> currencyMatcher) {
            this.currencyMatcher = currencyMatcher;
            rowSorter.sort();
        }

        private void setFromMatcher(EntityMatcher<Ride> fromMatcher) {
            this.fromMatcher = fromMatcher;
            rowSorter.sort();
        }

        private void setToMatcher(EntityMatcher<Ride> toMatcher) {
            this.toMatcher = toMatcher;
            rowSorter.sort();
        }

        private void setMinPassengers(EntityMatcher<Ride> minPassengers) {
            this.minPassengers = minPassengers;
            rowSorter.sort();
        }

        private void setMaxPassengers(EntityMatcher<Ride> maxPassengers) {
            this.maxPassengers = maxPassengers;
            rowSorter.sort();
        }


        @Override
        public boolean evaluate(Ride ride) {
            return Stream.of(categoryMatcher, currencyMatcher, fromMatcher, toMatcher, minPassengers, maxPassengers)
                    .allMatch(m -> m == null || m.evaluate(ride));
        }
    }
}
