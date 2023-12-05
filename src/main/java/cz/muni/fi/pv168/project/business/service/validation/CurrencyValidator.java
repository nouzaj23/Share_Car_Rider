package cz.muni.fi.pv168.project.business.service.validation;

import cz.muni.fi.pv168.project.business.model.Currency;
import cz.muni.fi.pv168.project.business.service.validation.common.NumberValueValidator;
import cz.muni.fi.pv168.project.business.service.validation.common.StringLengthValidator;

import java.util.List;

import static cz.muni.fi.pv168.project.business.service.validation.Validator.extracting;


public class CurrencyValidator implements Validator<Currency> {

    @Override
    public ValidationResult validate(Currency currency) {
        var validators = List.of(
                extracting(
                        Currency::getCode, new StringLengthValidator(1, 5, "Currency code")),
                extracting(
                        Currency::getConversionRatio, new NumberValueValidator(Double.MIN_VALUE, Double.MAX_VALUE, "Currency conversion ratio")
                )
        );

        return Validator.compose(validators).validate(currency);
    }
}
