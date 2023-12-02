package cz.muni.fi.pv168.project.business.service.crud;

import cz.muni.fi.pv168.project.business.guidProvider.GuidProvider;
import cz.muni.fi.pv168.project.business.model.Ride;
import cz.muni.fi.pv168.project.business.repository.Repository;
import cz.muni.fi.pv168.project.business.service.validation.ValidationResult;
import cz.muni.fi.pv168.project.business.service.validation.Validator;

import java.util.List;

public class RideCrudService implements CrudService<Ride> {

    private final Repository<Ride> rideRepository;
    private final Validator<Ride> rideValidator;
    private final GuidProvider guidProvider;

    public RideCrudService(Repository<Ride> rideRepository, Validator<Ride> rideValidator,
                           GuidProvider guidProvider) {
        this.rideRepository = rideRepository;
        this.rideValidator = rideValidator;
        this.guidProvider = guidProvider;
    }
    @Override
    public List<Ride> findAll() {
        return rideRepository.findAll();
    }

    @Override
    public ValidationResult create(Ride newEntity) {
        var validationResult = rideValidator.validate(newEntity);
        if (newEntity.getGuid() == null || newEntity.getGuid().isBlank()) {
            newEntity.setGuid(guidProvider.newGuid());
        } else if (rideRepository.existsByGuid(newEntity.getGuid())) {
            throw new EntityAlreadyExistsException("Ride with given guid already exists: " + newEntity.getGuid());
        }

        if (validationResult.isValid()) {
            rideRepository.create(newEntity);
        }

        return validationResult;
    }

    @Override
    public ValidationResult update(Ride entity) {
        var validationResult = rideValidator.validate(entity);
        if (validationResult.isValid()) {
            rideRepository.update(entity);
        }

        return validationResult;
    }

    @Override
    public void deleteByGuid(String guid) {
        rideRepository.deleteByGuid(guid);
    }

    @Override
    public void deleteAll() {
        rideRepository.deleteAll();
    }
}
