package cz.muni.fi.pv168.project.storage.sql.entity.mapper;

import cz.muni.fi.pv168.project.business.model.Currency;
import cz.muni.fi.pv168.project.storage.sql.entity.CurrencyEntity;

public class CurrencyMapper implements EntityMapper<CurrencyEntity, Currency>{
    @Override
    public Currency mapToBusiness(CurrencyEntity dbCurrency) {
        return new Currency(
                dbCurrency.guid(),
                dbCurrency.code(),
                dbCurrency.conversionRatio()
        );
    }

    @Override
    public CurrencyEntity mapNewEntityToDatabase(Currency entity) {
        return getCurrencyEntity(entity, null);
    }

    @Override
    public CurrencyEntity mapExistingEntityToDatabase(Currency entity, Long dbId) {
        return getCurrencyEntity(entity, dbId);
    }

    private static CurrencyEntity getCurrencyEntity(Currency entity, Long dbId) {
        return new CurrencyEntity(
                dbId,
                entity.getGuid(),
                entity.getCode(),
                entity.getConversionRatio()
        );
    }
}
