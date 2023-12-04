package cz.muni.fi.pv168.project.business.service.crud;

import cz.muni.fi.pv168.project.business.guidProvider.GuidProvider;
import cz.muni.fi.pv168.project.business.model.Currency;
import cz.muni.fi.pv168.project.business.repository.Repository;
import cz.muni.fi.pv168.project.business.service.validation.ValidationResult;
import cz.muni.fi.pv168.project.business.service.validation.Validator;

import java.util.List;

public class CurrencyCrudService implements CrudService<Currency> {
    private final Repository<Currency> currencyRepository;
    private final Validator<Currency> currencyValidator;
    private final GuidProvider guidProvider;

    public CurrencyCrudService(Repository<Currency> currencyRepository, Validator<Currency> categoryValidator,
                               GuidProvider guidProvider) {
        this.currencyRepository = currencyRepository;
        this.currencyValidator = categoryValidator;
        this.guidProvider = guidProvider;
    }
    @Override
    public List<Currency> findAll() {
        return currencyRepository.findAll();
    }

    @Override
    public ValidationResult create(Currency newEntity) {
        var validationResult = currencyValidator.validate(newEntity);
        if (newEntity.getGuid() == null || newEntity.getGuid().isBlank()) {
            newEntity.setGuid(guidProvider.newGuid());
        } else if (currencyRepository.existsByGuid(newEntity.getGuid())) {
            throw new EntityAlreadyExistsException("Category with given guid already exists: " + newEntity.getGuid());
        }

        if (validationResult.isValid()) {
            currencyRepository.create(newEntity);
        }
        return validationResult;
    }

    @Override
    public ValidationResult update(Currency entity) {
        var validationResult = currencyValidator.validate(entity);
        if (validationResult.isValid()) {
            currencyRepository.update(entity);
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
