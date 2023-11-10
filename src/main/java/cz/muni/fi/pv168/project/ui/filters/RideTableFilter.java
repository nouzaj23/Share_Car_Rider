package cz.muni.fi.pv168.project.ui.filters;

import cz.muni.fi.pv168.project.model.Currency;
import cz.muni.fi.pv168.project.ui.filters.matchers.EntityMatcher;
import cz.muni.fi.pv168.project.ui.filters.matchers.EntityMatchers;
import cz.muni.fi.pv168.project.ui.filters.matchers.ride.RideCategoryCompoundMatcher;
import cz.muni.fi.pv168.project.ui.filters.matchers.ride.RideCategoryMatcher;
import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.model.Ride;
import cz.muni.fi.pv168.project.ui.filters.matchers.ride.RideCurrencyCompoundMatcher;
import cz.muni.fi.pv168.project.ui.filters.matchers.ride.RideCurrencyMatcher;
import cz.muni.fi.pv168.project.ui.model.CarRidesModel;

import javax.swing.table.TableRowSorter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class RideTableFilter {
    private final RideCompoundMatcher rideCompoundMatcher;

    public RideTableFilter(TableRowSorter<CarRidesModel> rowSorter) {
        rideCompoundMatcher = new RideCompoundMatcher(rowSorter);
        rowSorter.setRowFilter(rideCompoundMatcher);
    }

    public void filterCategory(List<Category> selectedItems) {
        List<EntityMatcher<Ride>> matchers = new ArrayList<>();
        selectedItems.forEach(elem -> matchers.add(new RideCategoryMatcher(elem)));
        if (matchers.isEmpty()) {
            rideCompoundMatcher.setCategoryMatcher(null);
        }
        rideCompoundMatcher.setCategoryMatcher(new RideCategoryCompoundMatcher(matchers));
    }

    public void filterCurrency(List<Currency> selectedItems) {
        List<EntityMatcher<Ride>> matchers = new ArrayList<>();
        selectedItems.forEach(elem -> matchers.add(new RideCurrencyMatcher(elem)));
        if (matchers.isEmpty()) {
            rideCompoundMatcher.setCurrencyMatcher(null);
        }
        rideCompoundMatcher.setCurrencyMatcher(new RideCurrencyCompoundMatcher(matchers));
    }

    public void resetFilter() {
        rideCompoundMatcher.resetFilter();
    }

    private static class RideCompoundMatcher extends EntityMatcher<Ride> {

        private final TableRowSorter<CarRidesModel> rowSorter;
        private EntityMatcher<Ride> categoryMatcher = EntityMatchers.all();
        private EntityMatcher<Ride> currencyMatcher = EntityMatchers.all();

        private RideCompoundMatcher(TableRowSorter<CarRidesModel> rowSorter) {
            this.rowSorter = rowSorter;
        }

        private void resetFilter() {
            this.categoryMatcher = null;
            this.currencyMatcher = null;
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

        @Override
        public boolean evaluate(Ride ride) {
            return Stream.of(categoryMatcher, currencyMatcher)
                    .allMatch(m -> m == null || m.evaluate(ride));
        }
    }
}
