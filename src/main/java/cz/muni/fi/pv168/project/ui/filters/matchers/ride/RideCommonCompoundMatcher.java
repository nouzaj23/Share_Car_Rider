package cz.muni.fi.pv168.project.ui.filters.matchers.ride;

import cz.muni.fi.pv168.project.business.model.Ride;
import cz.muni.fi.pv168.project.ui.filters.matchers.EntityMatcher;

import java.util.Collection;

public class RideCommonCompoundMatcher extends EntityMatcher<Ride> {

    private final Collection<EntityMatcher<Ride>> rideMatchers;

    public RideCommonCompoundMatcher(Collection<EntityMatcher<Ride>> rideMatchers) {
        this.rideMatchers = rideMatchers;
    }

    @Override
    public boolean evaluate(Ride ride) {
        return rideMatchers.isEmpty() || rideMatchers.stream()
                .anyMatch(matcher -> matcher == null || matcher.evaluate(ride));
    }
}
