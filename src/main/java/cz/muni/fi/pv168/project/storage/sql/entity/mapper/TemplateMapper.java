package cz.muni.fi.pv168.project.storage.sql.entity.mapper;

import cz.muni.fi.pv168.project.business.model.Category;
import cz.muni.fi.pv168.project.business.model.Currency;
import cz.muni.fi.pv168.project.business.model.Template;
import cz.muni.fi.pv168.project.storage.sql.dao.DataAccessObject;
import cz.muni.fi.pv168.project.storage.sql.dao.DataStorageException;
import cz.muni.fi.pv168.project.storage.sql.entity.CategoryEntity;
import cz.muni.fi.pv168.project.storage.sql.entity.CurrencyEntity;
import cz.muni.fi.pv168.project.storage.sql.entity.TemplateEntity;

public class TemplateMapper implements EntityMapper<TemplateEntity, Template> {

  private final DataAccessObject<CurrencyEntity> currencyDao;
  private final DataAccessObject<CategoryEntity> categoryDao;
  private final EntityMapper<CurrencyEntity, Currency> currencyMapper;
  private final EntityMapper<CategoryEntity, Category> categoryMapper;

  public TemplateMapper(
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
  public Template mapToBusiness(TemplateEntity entity) {
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

    return new Template(
        entity.guid(),
        entity.name(),
        entity.passengers(),
        currency,
        category,
        entity.from(),
        entity.to(),
        entity.distance());
  }

  @Override
  public TemplateEntity mapNewEntityToDatabase(Template entity) {
    var currencyEntity = currencyDao
        .findByGuid(entity.getCurrency().getGuid())
        .orElseThrow(() -> new DataStorageException("Department not found, guid: " +
            entity.getCurrency().getGuid()));

    var categoryEntity = categoryDao
        .findByGuid(entity.getCategory().getGuid())
        .orElseThrow(() -> new DataStorageException("Department not found, guid: " +
            entity.getCategory().getGuid()));

    return new TemplateEntity(
        entity.getGuid(),
        entity.getName(),
        entity.getPassengers(),
        currencyEntity.id(),
        categoryEntity.id(),
        entity.getFrom(),
        entity.getTo(),
        entity.getDistance()
      );
  }

  @Override
  public TemplateEntity mapExistingEntityToDatabase(Template entity, Long dbId) {
    var currencyEntity = currencyDao
        .findByGuid(entity.getCurrency().getGuid())
        .orElseThrow(() -> new DataStorageException("Department not found, guid: " +
            entity.getCurrency().getGuid()));

    var categoryEntity = categoryDao
        .findByGuid(entity.getCategory().getGuid())
        .orElseThrow(() -> new DataStorageException("Department not found, guid: " +
            entity.getCategory().getGuid()));

    return new TemplateEntity(
        dbId,
        entity.getGuid(),
        entity.getName(),
        entity.getPassengers(),
        currencyEntity.id(),
        categoryEntity.id(),
        entity.getFrom(),
        entity.getTo(),
        entity.getDistance()
        );
  }
}
