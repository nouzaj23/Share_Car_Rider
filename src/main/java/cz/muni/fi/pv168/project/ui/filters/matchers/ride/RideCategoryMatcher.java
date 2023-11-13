package cz.muni.fi.pv168.project.ui.filters.matchers.ride;

import cz.muni.fi.pv168.project.model.Category;
import cz.muni.fi.pv168.project.model.Ride;
import cz.muni.fi.pv168.project.ui.filters.matchers.EntityMatcher;

import java.util.Collection;
import java.util.List;

public class RideCategoryMatcher extends EntityMatcher<Ride> {

    private final Collection<Category> selectedCategories;

    public RideCategoryMatcher(Category selectedCategory) {
        this.selectedCategories = List.of(selectedCategory);
    }

    @Override
    public boolean evaluate(Ride ride) {
        return selectedCategories.stream()
                .anyMatch(d -> ride.getCategory().equals(d));
    }
}
