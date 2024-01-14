package cz.muni.fi.pv168.project.business.service.crud;

import cz.muni.fi.pv168.project.business.guidProvider.GuidProvider;
import cz.muni.fi.pv168.project.business.model.Category;
import cz.muni.fi.pv168.project.business.model.Ride;
import cz.muni.fi.pv168.project.business.repository.Repository;
import cz.muni.fi.pv168.project.business.service.validation.ValidationResult;
import cz.muni.fi.pv168.project.business.service.validation.Validator;

import org.tinylog.Logger;

import java.util.List;

public class RideCrudService implements CrudService<Ride> {

    private final Repository<Ride> rideRepository;
    private final Validator<Ride> rideValidator;

    public RideCrudService(Repository<Ride> rideRepository, Validator<Ride> rideValidator, Repository<Category> categoryRepository) {
        this.rideRepository = rideRepository;
        this.rideValidator = rideValidator;
    }
    @Override
    public List<Ride> findAll() {
        return rideRepository.findAll();
    }

    @Override
    public ValidationResult create(Ride newEntity) {
        var validationResult = rideValidator.validate(newEntity);
        if (newEntity.getGuid() == null || newEntity.getGuid().isBlank()) {
            newEntity.setGuid(GuidProvider.newGuid());
        } else if (rideRepository.existsByGuid(newEntity.getGuid())) {
            throw new EntityAlreadyExistsException("Ride with given guid already exists: " + newEntity.getGuid());
        } else if (findDuplicateByName(newEntity)){
            throw new EntityAlreadyExistsException("Ride with given name and date already exists: " + newEntity.getName() + newEntity.getDate().toString());
        }

        if (validationResult.isValid()) {
            rideRepository.create(newEntity);
        }
        Logger.info("Created new ride: {}", newEntity);
        
        return validationResult;
    }

    @Override
    public ValidationResult update(Ride entity) {
        var validationResult = rideValidator.validate(entity);

        if (validationResult.isValid()) {
            rideRepository.update(entity);
            Logger.info("Updated ride: {}", entity);

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

    private boolean findDuplicateByName(Ride newEntity){
        return rideRepository.findAll()
                             .stream()
                             .anyMatch(ride -> ride.getName().equals(newEntity.getName())
                                                && ride.getDate().equals(newEntity.getDate()));
    }
}
