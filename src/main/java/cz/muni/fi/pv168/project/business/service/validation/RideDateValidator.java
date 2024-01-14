package cz.muni.fi.pv168.project.business.service.validation;

import cz.muni.fi.pv168.project.business.model.Ride;

import java.time.Clock;
import java.time.LocalDate;
import java.time.Period;

public class RideDateValidator implements Validator<Ride> {
    private final Clock clock;

    public RideDateValidator(Clock clock) {
        this.clock = clock;
    }

    @Override
    public ValidationResult validate(Ride ride) {
        if (ride.getDate() == null) {
            return ValidationResult.failed("Date not selected");
        }
        if (calculateDays(ride) < 0) {
            return ValidationResult.failed("Date cannot be in the future");
        }
        return ValidationResult.success();
    }

    private int calculateDays(Ride ride) {
        return Period
                .between(ride.getDate(), LocalDate.now(clock))
                .getDays();
    }
}
