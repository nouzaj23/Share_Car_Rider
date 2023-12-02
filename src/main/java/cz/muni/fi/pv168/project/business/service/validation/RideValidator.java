package cz.muni.fi.pv168.project.business.service.validation;

import cz.muni.fi.pv168.project.business.model.Ride;

public class RideValidator implements Validator<Ride> {

    @Override
    public ValidationResult validate (Ride model) {
        // TODO: Implement validation
        return ValidationResult.success();
    }
}
