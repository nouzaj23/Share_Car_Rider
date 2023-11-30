package cz.muni.fi.pv168.project.business.service.validation;

import cz.muni.fi.pv168.project.business.model.Currency;

public class CurrencyValidator implements Validator<Currency> {

    @Override
    public ValidationResult validate(Currency model) {
        // TODO: Implement validation
        return ValidationResult.success();
    }
}
