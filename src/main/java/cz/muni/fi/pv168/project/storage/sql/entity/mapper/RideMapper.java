package cz.muni.fi.pv168.project.storage.sql.entity.mapper;

import cz.muni.fi.pv168.project.business.model.Category;
import cz.muni.fi.pv168.project.business.model.Currency;
import cz.muni.fi.pv168.project.business.model.Ride;
import cz.muni.fi.pv168.project.storage.sql.dao.DataAccessObject;
import cz.muni.fi.pv168.project.storage.sql.dao.DataStorageException;
import cz.muni.fi.pv168.project.storage.sql.entity.CategoryEntity;
import cz.muni.fi.pv168.project.storage.sql.entity.CurrencyEntity;
import cz.muni.fi.pv168.project.storage.sql.entity.RideEntity;

public class RideMapper implements EntityMapper<RideEntity, Ride> {

  private final DataAccessObject<CurrencyEntity> currencyDao;
  private final DataAccessObject<CategoryEntity> categoryDao;
  private final EntityMapper<CurrencyEntity, Currency> currencyMapper;
  private final EntityMapper<CategoryEntity, Category> categoryMapper;

  public RideMapper(
      DataAccessObject<CurrencyEntity> currencyDao,
      DataAccessObject<CategoryEntity> categoryDao,
      EntityMapper<CurrencyEntity, Currency> currencyMapper,
      EntityMapper<CategoryEntity, Category> categoryMapper) {
    this.currencyDao = currencyDao;
    this.categoryDao = categoryDao;
    this.currencyMapper = currencyMapper;
    this.categoryMapper = categoryMapper;
  }

  @Override
  public Ride mapToBusiness(RideEntity entity) {
    var currency = currencyDao
        .findById(entity.currencyId())
        .map(currencyMapper::mapToBusiness)
        .orElseThrow(() -> new DataStorageException("Currency not found, id: " +
            entity.currencyId()));

    var category = categoryDao
        .findById(entity.categoryId())
        .map(categoryMapper::mapToBusiness)
        .orElseThrow(() -> new DataStorageException("Category not found, id: " +
            entity.currencyId()));

    return new Ride(
        entity.guid(),
        entity.name(),
        entity.passengers(),
        currency,
        entity.fuelExpenses(),
        category,
        entity.from(),
        entity.to(),
        entity.distance(),
        entity.date());
  }

  @Override
  public RideEntity mapNewEntityToDatabase(Ride entity) {
    var currencyEntity = currencyDao
        .findByGuid(entity.getCurrency().getGuid())
        .orElseThrow(() -> new DataStorageException("Department not found, guid: " +
            entity.getCurrency().getGuid()));

    var categoryEntity = categoryDao
        .findByGuid(entity.getCategory().getGuid())
        .orElseThrow(() -> new DataStorageException("Department not found, guid: " +
            entity.getCategory().getGuid()));

    return new RideEntity(
        entity.getGuid(),
        entity.getName(),
        entity.getPassengers(),
        currencyEntity.id(),
        entity.getFuelExpenses(),
        categoryEntity.id(),
        entity.getFrom(),
        entity.getTo(),
        entity.getDistance(),
        entity.getDate()
      );
  }

  @Override
  public RideEntity mapExistingEntityToDatabase(Ride entity, Long dbId) {
    var currencyEntity = currencyDao
        .findByGuid(entity.getCurrency().getGuid())
        .orElseThrow(() -> new DataStorageException("Department not found, guid: " +
            entity.getCurrency().getGuid()));

    var categoryEntity = categoryDao
        .findByGuid(entity.getCategory().getGuid())
        .orElseThrow(() -> new DataStorageException("Department not found, guid: " +
            entity.getCategory().getGuid()));

    return new RideEntity(
        dbId,
        entity.getGuid(),
        entity.getName(),
        entity.getPassengers(),
        currencyEntity.id(),
        entity.getFuelExpenses(),
        categoryEntity.id(),
        entity.getFrom(),
        entity.getTo(),
        entity.getDistance(),
        entity.getDate()
        );
  }
}
