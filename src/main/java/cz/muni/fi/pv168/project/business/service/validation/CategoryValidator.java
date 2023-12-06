package cz.muni.fi.pv168.project.business.service.validation;

import cz.muni.fi.pv168.project.business.model.Category;
import cz.muni.fi.pv168.project.business.service.validation.common.StringLengthValidator;

import java.util.List;

import static cz.muni.fi.pv168.project.business.service.validation.Validator.extracting;

public class CategoryValidator implements Validator<Category> {

    @Override
    public ValidationResult validate(Category category) {
        if (category == null) {
            return ValidationResult.failed("Category not selected");
        }
        var validators = List.of(
                extracting(
                        Category::getName, new StringLengthValidator(1, 50, "Category name"))
        );

        return Validator.compose(validators).validate(category);
    }
}
