package cz.muni.fi.pv168.project.business.service.crud;

import cz.muni.fi.pv168.project.business.guidProvider.GuidProvider;
import cz.muni.fi.pv168.project.business.model.Category;
import cz.muni.fi.pv168.project.business.repository.Repository;
import cz.muni.fi.pv168.project.business.service.validation.ValidationResult;
import cz.muni.fi.pv168.project.business.service.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 * Unit tests for the {@link CategoryCrudService}
 */
class CategoryCrudServiceUnitTest {
    private CategoryCrudService categoryCrudService;
    private Repository<Category> categoryRepository;
    private Validator<Category> categoryValidator;

    @SuppressWarnings("unchecked")
    @BeforeEach
    void setUp() {
        categoryRepository = (Repository<Category>) mock(Repository.class);
        categoryValidator = (Validator<Category>) mock(Validator.class);
        categoryCrudService = new CategoryCrudService(categoryRepository, categoryValidator);
    }

    @Test
    void createWithGuidSucceeds() {
        var category = createCategoryInstance("c-1");

        when(categoryValidator.validate(category))
                .thenReturn(ValidationResult.success());

        var result = categoryCrudService.create(category);
        assertThat(result)
                .isEqualTo(ValidationResult.success());

        verify(categoryRepository, times(1))
                .create(category);
    }

    @Test
    void createWithoutGuidSucceeds() {
        var category = createCategoryInstance(null);
        var newGuid = "new-guid";
        var expectedCategory = createCategoryInstance(newGuid);

        mockStatic(GuidProvider.class);
        when(GuidProvider.newGuid())
                .thenReturn(newGuid);

        when(categoryValidator.validate(category))
                .thenReturn(ValidationResult.success());

        var result = categoryCrudService.create(category);

        assertThat(result)
                .isEqualTo(ValidationResult.success());

        verify(categoryRepository, times(1))
                .create(refEq(expectedCategory));
    }

    @Test
    void createValidationError() {
        var category = createCategoryInstance("c-1");
        var validationError = "validation-error";

        when(categoryValidator.validate(category))
                .thenReturn(ValidationResult.failed(validationError));

        var result = categoryCrudService.create(category);

        assertThat(result)
                .isEqualTo(ValidationResult.failed(validationError));

        verify(categoryRepository, times(0))
                .create(category);
    }

    @Test
    void createFailsForDuplicateGuid() {
        var category = createCategoryInstance("c-1");

        when(categoryValidator.validate(category))
                .thenReturn(ValidationResult.success());
        when(categoryRepository.existsByGuid("c-1"))
                .thenReturn(true);

        assertThatExceptionOfType(EntityAlreadyExistsException.class)
                .isThrownBy(() -> categoryCrudService.create(category))
                .withMessage("Category with given guid already exists: c-1");
    }

    @Test
    void updateWithGuidSucceeds() {
        var category = createCategoryInstance("c-1");

        when(categoryValidator.validate(category))
                .thenReturn(ValidationResult.success());

        var result = categoryCrudService.update(category);

        assertThat(result)
                .isEqualTo(ValidationResult.success());

        verify(categoryRepository, times(1))
                .update(category);
    }

    @Test
    void updateValidationError() {
        var category = createCategoryInstance("c-1");
        var validationError = "validation-error";

        when(categoryValidator.validate(category))
                .thenReturn(ValidationResult.failed(validationError));

        var result = categoryCrudService.update(category);

        assertThat(result)
                .isEqualTo(ValidationResult.failed(validationError));

        verify(categoryRepository, times(0))
                .update(category);
    }

    @Test
    void deleteByGuid() {
        categoryCrudService.deleteByGuid("guid");

        verify(categoryRepository, times(1))
                .deleteByGuid("guid");
    }

    @Test
    void deleteAll() {
        categoryCrudService.deleteAll();

        verify(categoryRepository, times(1))
                .deleteAll();
    }

    @Test
    void findAll() {
        var expectedCategoryList = List.of(createCategoryInstance("c-1"));

        when(categoryRepository.findAll())
                .thenReturn(expectedCategoryList);

        var foundCategories = categoryCrudService.findAll();

        assertThat(foundCategories)
                .isEqualTo(expectedCategoryList);
    }

    private static Category createCategoryInstance(String guid) {
        return new Category(guid, "New Category");
    }
}
