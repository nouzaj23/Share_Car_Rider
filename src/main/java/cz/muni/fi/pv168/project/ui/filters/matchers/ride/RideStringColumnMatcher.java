package cz.muni.fi.pv168.project.ui.filters.matchers.ride;

import cz.muni.fi.pv168.project.ui.filters.matchers.EntityMatcher;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

public class RideStringColumnMatcher<Ride> extends EntityMatcher<Ride> {
    private final Collection<String> stringsToMatch;
    private final Function<Ride, String> fieldGetter;

    public RideStringColumnMatcher(String stringToMatch, Function<Ride, String> fieldGetter) {
        this.stringsToMatch = List.of(stringToMatch);
        this.fieldGetter = fieldGetter;
    }

    @Override
    public boolean evaluate(Ride ride) {
        String fieldValue = fieldGetter.apply(ride);
        return stringsToMatch.stream()
                .anyMatch(fieldValue::startsWith);
    }
}
