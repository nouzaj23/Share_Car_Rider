package cz.muni.fi.pv168.project.storage.sql;

import java.util.List;
import java.util.Optional;

import cz.muni.fi.pv168.project.business.model.Currency;
import cz.muni.fi.pv168.project.business.repository.Repository;
import cz.muni.fi.pv168.project.storage.sql.dao.DataAccessObject;
import cz.muni.fi.pv168.project.storage.sql.entity.CurrencyEntity;
import cz.muni.fi.pv168.project.storage.sql.entity.mapper.EntityMapper;

public class CurrencySqlRepository implements Repository<Currency> {

    private final DataAccessObject<CurrencyEntity> currencyDao;
    private final EntityMapper<CurrencyEntity, Currency> currencyMapper;

    public CurrencySqlRepository(
            DataAccessObject<CurrencyEntity> currencyDao,
            EntityMapper<CurrencyEntity, Currency> currencyMapper) {
        this.currencyDao = currencyDao;
        this.currencyMapper = currencyMapper;
    }


    @Override
    public List<Currency> findAll() {
        return currencyDao
                .findAll()
                .stream()
                .map(currencyMapper::mapToBusiness)
                .toList();
    }

    @Override
    public void create(Currency newEntity) {
        currencyDao.create(currencyMapper.mapNewEntityToDatabase(newEntity));
    }

    @Override
    public void update(Currency entity) {
        var existingCurrency = currencyDao.findByGuid(entity.getGuid())
                .orElseThrow(() -> new DataStorageException("Currency not found, guid: " + entity.getGuid()));
        var updatedCurrencyEntity = currencyMapper
                .mapExistingEntityToDatabase(entity, existingCurrency.id());

        currencyDao.update(updatedCurrencyEntity);
    }

    @Override
    public void deleteByGuid(String guid) {
        currencyDao.deleteByGuid(guid);
    }

    @Override
    public void deleteAll() {
        currencyDao.deleteAll();
    }

    @Override
    public boolean existsByGuid(String guid) {
        return currencyDao.existsByGuid(guid);
    }

    @Override
    public Optional<Currency> findByGuid(String guid) {
       return currencyDao
            .findByGuid(guid)
            .map(currencyMapper::mapToBusiness);
    }
}
