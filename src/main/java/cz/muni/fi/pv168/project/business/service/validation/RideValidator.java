package cz.muni.fi.pv168.project.business.service.validation;

import cz.muni.fi.pv168.project.business.model.Ride;

import java.util.List;

import static cz.muni.fi.pv168.project.business.service.validation.Validator.extracting;

public class RideValidator implements Validator<Ride> {

    @Override
    public ValidationResult validate(Ride ride) {
        var validators = List.of(
                // Reusing TemplateValidator to avoid code duplication
                extracting(
                        Ride::extractTemplate, new TemplateValidator())
                /* fuelExpenses validation skipped, because we allow
                 * any float value (friends can give us money for a ride,
                 * so we can potentially earn money) */
        );

        return Validator.compose(validators).validate(ride);
    }
}
