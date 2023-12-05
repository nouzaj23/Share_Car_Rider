package cz.muni.fi.pv168.project.business.service.validation;

import cz.muni.fi.pv168.project.business.model.Template;
import cz.muni.fi.pv168.project.business.service.validation.common.NumberValueValidator;
import cz.muni.fi.pv168.project.business.service.validation.common.StringLengthValidator;

import java.util.List;

import static cz.muni.fi.pv168.project.business.service.validation.Validator.extracting;


public class TemplateValidator implements Validator<Template> {

    @Override
    public ValidationResult validate(Template template) {
        var validators = List.of(
                extracting(
                        Template::getName, new StringLengthValidator(1, 50, "Template name")),
                extracting(
                        Template::getFrom, new StringLengthValidator(1, 50, "From place name")),
                extracting(
                        Template::getTo, new StringLengthValidator(1, 50, "To place name")),
                extracting(
                        Template::getPassengers, new NumberValueValidator(0, 10, "Passengers count")),
                extracting(
                        Template::getDistance, new NumberValueValidator(0, null, "Distance value")),
                extracting(
                        Template::getHours, new NumberValueValidator(0, null, "Hours value")),
                extracting(
                        Template::getCurrency, new CurrencyValidator()),
                extracting(
                        Template::getCategory, new CategoryValidator())
        );

        return Validator.compose(validators).validate(template);
    }
}
