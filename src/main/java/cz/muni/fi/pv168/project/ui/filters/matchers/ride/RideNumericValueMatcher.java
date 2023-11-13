package cz.muni.fi.pv168.project.ui.filters.matchers.ride;

import cz.muni.fi.pv168.project.model.Ride;
import cz.muni.fi.pv168.project.ui.filters.matchers.EntityMatcher;

import java.util.function.BiFunction;
import java.util.function.Function;

public class RideNumericValueMatcher extends EntityMatcher<Ride> {
    private final Number valueToMatch;
    private final Function<Ride, Number> fieldGetter;

    private final BiFunction<Number, Number, Boolean> matcherFunction;

    public RideNumericValueMatcher(Number valueToMatch, Function<Ride, Number> fieldGetter, BiFunction<Number, Number, Boolean> matcherFunction) {
        this.valueToMatch = valueToMatch;
        this.fieldGetter = fieldGetter;
        this.matcherFunction = matcherFunction;
    }

    @Override
    public boolean evaluate(Ride ride) {
        Number fieldValue = fieldGetter.apply(ride);
        return matcherFunction.apply(valueToMatch, fieldValue);
    }
}
