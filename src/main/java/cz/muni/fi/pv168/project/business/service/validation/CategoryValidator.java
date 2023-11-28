package cz.muni.fi.pv168.project.business.service.validation;

import cz.muni.fi.pv168.project.business.model.Category;

public class CategoryValidator implements Validator<Category> {

    @Override
    public ValidationResult validate(Category model) {
        // TODO: Implement validation
        return ValidationResult.success();
    }
}
