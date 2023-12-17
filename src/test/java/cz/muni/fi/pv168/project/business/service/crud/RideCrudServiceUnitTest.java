package cz.muni.fi.pv168.project.business.service.crud;

import cz.muni.fi.pv168.project.business.guidProvider.GuidProvider;
import cz.muni.fi.pv168.project.business.model.Category;
import cz.muni.fi.pv168.project.business.model.Currency;
import cz.muni.fi.pv168.project.business.model.Ride;
import cz.muni.fi.pv168.project.business.repository.Repository;
import cz.muni.fi.pv168.project.business.service.validation.ValidationResult;
import cz.muni.fi.pv168.project.business.service.validation.Validator;
import cz.muni.fi.pv168.project.storage.sql.CategorySqlRepository;
import cz.muni.fi.pv168.project.storage.sql.dao.DataAccessObject;
import cz.muni.fi.pv168.project.storage.sql.entity.CategoryEntity;
import cz.muni.fi.pv168.project.storage.sql.entity.mapper.EntityMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
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
 * Unit tests for {@link RideCrudService}.
 */
public class RideCrudServiceUnitTest {
    private RideCrudService rideCrudService;
    private Repository<Ride> rideRepository;
    private Repository<Category> categoryRepository;
    private Validator<Ride> rideValidator;

    @SuppressWarnings("unchecked")
    @BeforeEach
    void setUp() {
        rideRepository = (Repository<Ride>) mock(Repository.class);
        rideValidator = (Validator<Ride>) mock(Validator.class);
        categoryRepository = (Repository<Category>) mock(Repository.class);
        rideCrudService = new RideCrudService(rideRepository, rideValidator, categoryRepository);
    }

    @Test
    void createWithGuidSucceeds() {
        var ride = createRideInstance("r-1");

        when(rideValidator.validate(ride))
                .thenReturn(ValidationResult.success());

        var result = rideCrudService.create(ride);
        assertThat(result)
                .isEqualTo(ValidationResult.success());

        verify(rideRepository, times(1))
                .create(ride);
    }

    @Test
    void createWithoutGuidSucceeds() {
        var ride = createRideInstance(null);
        var newGuid = "new-guid";
        var expectedRide = createRideInstance(newGuid);

        mockStatic(GuidProvider.class);
        when(GuidProvider.newGuid())
                .thenReturn(newGuid);

        when(rideValidator.validate(ride))
                .thenReturn(ValidationResult.success());

        var result = rideCrudService.create(ride);
        assertThat(result)
                .isEqualTo(ValidationResult.success());

        verify(rideRepository, times(1))
                .create(refEq(expectedRide));
    }

    @Test
    void createValidationError() {
        var ride = createRideInstance("r-1");
        var validationError = "validation-error";

        when(rideValidator.validate(ride))
                .thenReturn(ValidationResult.failed(validationError));

        var result = rideCrudService.create(ride);

        assertThat(result)
                .isEqualTo(ValidationResult.failed(validationError));

        verify(rideRepository, times(0))
                .create(ride);
    }

    @Test
    void createFailsForDuplicateGuid() {
        var ride = createRideInstance("r-1");

        when(rideValidator.validate(ride))
                .thenReturn(ValidationResult.success());
        when(rideRepository.existsByGuid("r-1"))
                .thenReturn(true);

        assertThatExceptionOfType(EntityAlreadyExistsException.class)
                .isThrownBy(() -> rideCrudService.create(ride))
                .withMessage("Ride with given guid already exists: r-1");
    }

    @Test
    void updateWithGuidSucceeds() {
        var ride = createRideInstance("r-1");

        when(rideValidator.validate(ride))
                .thenReturn(ValidationResult.success());

        var result = rideCrudService.update(ride);

        assertThat(result).isEqualTo(ValidationResult.success());
        verify(rideRepository, times(1))
                .update(ride);
    }

    @Test
    void updateValidationError() {
        var ride = createRideInstance("r-1");
        var validationError = "validation-error";

        when(rideValidator.validate(ride))
                .thenReturn(ValidationResult.failed(validationError));

        var result = rideCrudService.update(ride);

        assertThat(result)
                .isEqualTo(ValidationResult.failed(validationError));

        verify(rideRepository, times(0))
                .update(ride);
    }

    @Test
    void deleteByGuid() {
        rideCrudService.deleteByGuid("guid");

        verify(rideRepository, times(1))
                .deleteByGuid("guid");
    }

    @Test
    void deleteAll() {
        rideCrudService.deleteAll();

        verify(rideRepository, times(1))
                .deleteAll();
    }

    @Test
    void findAll() {
        var expectedRides = List.of(createRideInstance("r-1"));

        when(rideRepository.findAll())
                .thenReturn(expectedRides);

        var foundRides = rideCrudService.findAll();

        assertThat(foundRides)
                .isEqualTo(expectedRides);
    }

    private Ride createRideInstance(String guid) {
        var category = new Category("cat-1", "New Category");
        var currency = new Currency("cur-1", "CZK", 1.0);
        return new Ride(
                        guid,
                        "New Ride",
                        2,
                        currency,
                        20,
                        category,
                        "Home",
                        "Work",
                        5,
                        LocalDate.now());
    }
}
