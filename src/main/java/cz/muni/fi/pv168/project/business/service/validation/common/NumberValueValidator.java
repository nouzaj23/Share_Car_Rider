package cz.muni.fi.pv168.project.business.service.validation.common;

import cz.muni.fi.pv168.project.business.service.validation.ValidationResult;

public final class NumberValueValidator extends PropertyValidator<Number> {

    private final Number min;
    private final Number max;
    public NumberValueValidator(Number min, Number max, String name) {
        super(name);
        this.min = min;
        this.max = max;
    }

    @Override
    public ValidationResult validate(Number value) {
        var result = new ValidationResult();

        if ( min.doubleValue() > value.doubleValue() ||  value.doubleValue() > max.doubleValue() ) {
            result.add("'%s' length is not between %f (inclusive) and %f (inclusive)"
                    .formatted(getName(), min.doubleValue(), max.doubleValue())
            );
        }

        return result;
    }
}
