package cz.muni.fi.pv168.project.business.service.crud;

import cz.muni.fi.pv168.project.business.guidProvider.GuidProvider;
import cz.muni.fi.pv168.project.business.model.Category;
import cz.muni.fi.pv168.project.business.model.Currency;
import cz.muni.fi.pv168.project.business.model.Template;
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
 * Unit tests for {@link TemplateCrudService}.
 */
public class TemplateCrudServiceUnitTest {
    private TemplateCrudService templateCrudService;
    private Repository<Template> templateRepository;
    private Validator<Template> templateValidator;

    @SuppressWarnings("unchecked")
    @BeforeEach
    void setUp() {
        templateRepository = (Repository<Template>) mock(Repository.class);
        templateValidator = (Validator<Template>) mock(Validator.class);
        templateCrudService = new TemplateCrudService(templateRepository, templateValidator);
    }

    @Test
    void createWithGuidSucceeds() {
        var template = createTemplateInstance("t-1");

        when(templateValidator.validate(template))
                .thenReturn(ValidationResult.success());

        var result = templateCrudService.create(template);
        assertThat(result)
                .isEqualTo(ValidationResult.success());

        verify(templateRepository, times(1))
                .create(template);
    }

    @Test
    void createWithoutGuidSucceeds() {
        var template = createTemplateInstance(null);
        var newGuid = "new-guid";
        var expectedTemplate = createTemplateInstance(newGuid);

        var mockSettings = mockStatic(GuidProvider.class);
        when(GuidProvider.newGuid())
                .thenReturn(newGuid);

        when(templateValidator.validate(template))
                .thenReturn(ValidationResult.success());

        var result = templateCrudService.create(template);
        assertThat(result)
                .isEqualTo(ValidationResult.success());

        mockSettings.close();

        verify(templateRepository, times(1))
                .create(refEq(expectedTemplate));
    }

    @Test
    void createValidationError() {
        var template = createTemplateInstance("t-1");
        var validationError = "validation-error";

        when(templateValidator.validate(template))
                .thenReturn(ValidationResult.failed(validationError));

        var result = templateCrudService.create(template);

        assertThat(result)
                .isEqualTo(ValidationResult.failed(validationError));

        verify(templateRepository, times(0))
                .create(template);
    }

    @Test
    void createFailsForDuplicateGuid() {
        var template = createTemplateInstance("t-1");

        when(templateValidator.validate(template))
                .thenReturn(ValidationResult.success());
        when(templateRepository.existsByGuid("t-1"))
                .thenReturn(true);

        assertThatExceptionOfType(EntityAlreadyExistsException.class)
                .isThrownBy(() -> templateCrudService.create(template))
                .withMessage("Template with given guid already exists: t-1");
    }

    @Test
    void updateWithGuidSucceeds() {
        var template = createTemplateInstance("t-1");

        when(templateValidator.validate(template))
                .thenReturn(ValidationResult.success());

        var result = templateCrudService.update(template);

        assertThat(result).isEqualTo(ValidationResult.success());
        verify(templateRepository, times(1))
                .update(template);
    }

    @Test
    void updateValidationError() {
        var template = createTemplateInstance("t-1");
        var validationError = "validation-error";

        when(templateValidator.validate(template))
                .thenReturn(ValidationResult.failed(validationError));

        var result = templateCrudService.update(template);

        assertThat(result)
                .isEqualTo(ValidationResult.failed(validationError));

        verify(templateRepository, times(0))
                .update(template);
    }

    @Test
    void deleteByGuid() {
        templateCrudService.deleteByGuid("guid");

        verify(templateRepository, times(1))
                .deleteByGuid("guid");
    }

    @Test
    void deleteAll() {
        templateCrudService.deleteAll();

        verify(templateRepository, times(1))
                .deleteAll();
    }

    @Test
    void findAll() {
        var expectedTemplates = List.of(createTemplateInstance("t-1"));

        when(templateRepository.findAll())
                .thenReturn(expectedTemplates);

        var foundTemplates = templateCrudService.findAll();

        assertThat(foundTemplates)
                .isEqualTo(expectedTemplates);
    }

    private Template createTemplateInstance(String guid) {
        var category = new Category("cat-1", "New Category");
        var currency = new Currency("cur-1", "New Currency", 1.0);
        return new Template(guid, "name", 1, currency, category, "Home", "Work", 2);
    }
}
