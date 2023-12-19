package cz.muni.fi.pv168.project.business.service.crud;

import cz.muni.fi.pv168.project.business.guidProvider.GuidProvider;
import cz.muni.fi.pv168.project.business.model.Currency;
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
 * Unit tests for {@link CurrencyCrudService}.
 */
public class CurrencyCrudServiceUnitTest {
    private CurrencyCrudService currencyCrudService;
    private Repository<Currency> currencyRepository;
    private Validator<Currency> currencyValidator;

    @SuppressWarnings("unchecked")
    @BeforeEach
    void setUp() {
        currencyRepository = (Repository<Currency>) mock(Repository.class);
        currencyValidator = (Validator<Currency>) mock(Validator.class);
        currencyCrudService = new CurrencyCrudService(currencyRepository, currencyValidator);
    }

    @Test
    void createWithGuidSucceeds() {
        var currency = createCurrencyInstance("c-1");

        when(currencyValidator.validate(currency))
                .thenReturn(ValidationResult.success());

        var result = currencyCrudService.create(currency);
        assertThat(result)
                .isEqualTo(ValidationResult.success());

        verify(currencyRepository, times(1))
                .create(currency);
    }

    @Test
    void createWithoutGuidSucceeds() {
        var currency = createCurrencyInstance(null);
        var newGuid = "new-guid";
        var expectedCurrency = createCurrencyInstance(newGuid);

        var mockSettings = mockStatic(GuidProvider.class);
        when(GuidProvider.newGuid())
                .thenReturn(newGuid);

        when(currencyValidator.validate(currency))
                .thenReturn(ValidationResult.success());

        var result = currencyCrudService.create(currency);

        assertThat(result)
                .isEqualTo(ValidationResult.success());

        mockSettings.close();

        verify(currencyRepository, times(1))
                .create(refEq(expectedCurrency));
    }

    @Test
    void createValidationError() {
        var currency = createCurrencyInstance("c-1");
        var validationError = "validation-error";

        when(currencyValidator.validate(currency))
                .thenReturn(ValidationResult.failed(validationError));

        var result = currencyCrudService.create(currency);

        assertThat(result)
                .isEqualTo(ValidationResult.failed(validationError));

        verify(currencyRepository, times(0))
                .create(currency);
    }

    @Test
    void createFailsForDuplicateGuid() {
        var currency = createCurrencyInstance("c-1");

        when(currencyValidator.validate(currency))
                .thenReturn(ValidationResult.success());
        when(currencyRepository.existsByGuid("c-1"))
                .thenReturn(true);

        assertThatExceptionOfType(EntityAlreadyExistsException.class)
                .isThrownBy(() -> currencyCrudService.create(currency))
                .withMessage("Currency with given guid already exists: c-1");
    }

    @Test
    void updateWithGuidSucceeds() {
        var currency = createCurrencyInstance("c-1");

        when(currencyValidator.validate(currency))
                .thenReturn(ValidationResult.success());

        var result = currencyCrudService.update(currency);

        assertThat(result)
                .isEqualTo(ValidationResult.success());

        verify(currencyRepository, times(1))
                .update(currency);
    }

    @Test
    void updateValidationError() {
        var currency = createCurrencyInstance("c-1");
        var validationError = "validation-error";

        when(currencyValidator.validate(currency))
                .thenReturn(ValidationResult.failed(validationError));

        var result = currencyCrudService.update(currency);

        assertThat(result)
                .isEqualTo(ValidationResult.failed(validationError));

        verify(currencyRepository, times(0))
                .update(currency);
    }

    @Test
    void deleteByGuid() {
        currencyCrudService.deleteByGuid("guid");

        verify(currencyRepository, times(1))
                .deleteByGuid("guid");
    }

    @Test
    void deleteAll() {
        currencyCrudService.deleteAll();

        verify(currencyRepository, times(1))
                .deleteAll();
    }

    @Test
    void findAll() {
        var expectedCurrencies = List.of(createCurrencyInstance("c-1"));

        when(currencyRepository.findAll())
                .thenReturn(expectedCurrencies);

        var foundCurrencies = currencyCrudService.findAll();

        assertThat(foundCurrencies)
                .isEqualTo(expectedCurrencies);
    }

    private Currency createCurrencyInstance(String guid) {
        return new Currency(guid, "CZK",  1.0);
    }
}
