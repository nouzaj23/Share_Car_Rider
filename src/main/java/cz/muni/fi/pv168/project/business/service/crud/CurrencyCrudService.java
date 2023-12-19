package cz.muni.fi.pv168.project.business.service.crud;

import java.util.List;

import cz.muni.fi.pv168.project.business.guidProvider.GuidProvider;
import cz.muni.fi.pv168.project.business.model.Currency;
import cz.muni.fi.pv168.project.business.repository.Repository;
import cz.muni.fi.pv168.project.business.service.validation.ValidationResult;
import cz.muni.fi.pv168.project.business.service.validation.Validator;
import org.tinylog.Logger;


public class CurrencyCrudService implements CrudService<Currency> {
    private final Repository<Currency> currencyRepository;
    private final Validator<Currency> currencyValidator;

    public CurrencyCrudService(Repository<Currency> currencyRepository, Validator<Currency> currencyValidator) {
        this.currencyRepository = currencyRepository;
        this.currencyValidator = currencyValidator;
    }
    @Override
    public List<Currency> findAll() {
        return currencyRepository.findAll();
    }

    @Override
    public ValidationResult create(Currency newEntity) {
        var validationResult = currencyValidator.validate(newEntity);
        if (newEntity.getGuid() == null || newEntity.getGuid().isBlank()) {
            newEntity.setGuid(GuidProvider.newGuid());
        } else if (currencyRepository.existsByGuid(newEntity.getGuid())) {
            throw new EntityAlreadyExistsException("Currency with given guid already exists: " + newEntity.getGuid());
        }

        if (validationResult.isValid()) {
            currencyRepository.create(newEntity);
            Logger.info("Created new currency: {}", newEntity);

        }
        return validationResult;
    }

    @Override
    public ValidationResult update(Currency entity) {
        var validationResult = currencyValidator.validate(entity);
        if (validationResult.isValid()) {
            currencyRepository.update(entity);
            Logger.info("Updated currency: {}", entity);

        }
        return validationResult;
    }

    @Override
    public void deleteByGuid(String guid) {
        currencyRepository.deleteByGuid(guid);
    }

    @Override
    public void deleteAll() {
        currencyRepository.deleteAll();
    }
}
