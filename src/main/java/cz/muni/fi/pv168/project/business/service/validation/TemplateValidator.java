package cz.muni.fi.pv168.project.business.service.validation;

import cz.muni.fi.pv168.project.business.model.Template;

public class TemplateValidator implements  Validator<Template> {

    @Override
    public ValidationResult validate(Template model) {
        // TODO: Implement validation
        return ValidationResult.success();
    }
}
