package cz.muni.fi.pv168.project.storage.sql.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import cz.muni.fi.pv168.project.storage.sql.db.ConnectionHandler;
import cz.muni.fi.pv168.project.storage.sql.entity.CurrencyEntity;

public class CurrencyDao implements DataAccessObject<CurrencyEntity> {

    private final Supplier<ConnectionHandler> connections;

    public CurrencyDao(Supplier<ConnectionHandler> connections) {
        this.connections = connections;
    }

    @Override
    public CurrencyEntity create(CurrencyEntity newCurrency) {
        var sql = "INSERT INTO Currency (guid, code, conversionRatio) VALUES (?, ?, ?);";

        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, newCurrency.guid());
            statement.setString(2, newCurrency.code());
            statement.setDouble(3, newCurrency.conversionRatio());
            statement.executeUpdate();

            try (ResultSet keyResultSet = statement.getGeneratedKeys()) {
                long currencyId;

                if (keyResultSet.next()) {
                    currencyId = keyResultSet.getLong(1);
                } else {
                    throw new DataStorageException("Failed to fetch generated key for: " + newCurrency);
                }
                if (keyResultSet.next()) {
                    throw new DataStorageException("Multiple keys returned for: " + newCurrency);
                }

                return findById(currencyId).orElseThrow();
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to store: " + newCurrency, ex);
        }
    }

    @Override
    public Collection<CurrencyEntity> findAll() {
        var sql = """
                SELECT id,
                       guid,
                       code,
                       conversionRatio
                FROM Currency
                """;
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            List<CurrencyEntity> currencies = new ArrayList<>();
            try (var resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    var currency = currencyFromResultSet(resultSet);
                    currencies.add(currency);
                }
            }

            return currencies;
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to load all currencies", ex);
        }
    }

    @Override
    public Optional<CurrencyEntity> findById(long id) {
        var sql = """
                SELECT id,
                       guid,
                       code,
                       conversionRatio
                FROM Currency
                WHERE id = ?
                """;
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setLong(1, id);
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(currencyFromResultSet(resultSet));
            } else {
                return Optional.empty();
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to load currency by id: " + id, ex);
        }
    }

    @Override
    public Optional<CurrencyEntity> findByGuid(String guid) {
        var sql = """
                SELECT id,
                       guid,
                       code,
                       conversionRatio
                FROM Currency
                WHERE guid = ?
                """;
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, guid);
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(currencyFromResultSet(resultSet));
            } else {
                return Optional.empty();
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to load currency by guid: " + guid, ex);
        }
    }

    @Override
    public CurrencyEntity update(CurrencyEntity entity) {
        var sql = """
                UPDATE Currency
                SET code = ?,
                    conversionRatio = ?
                WHERE id = ?
                """;
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, entity.code());
            statement.setDouble(2, entity.conversionRatio());
            statement.setLong(3, entity.id());
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated == 0) {
                throw new DataStorageException("Currency not found, id: " + entity.id());
            }
            if (rowsUpdated > 1) {
                throw new DataStorageException("More then 1 currency (rows=%d) has been updated: %s"
                        .formatted(rowsUpdated, entity));
            }
            return entity;
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to update currency: " + entity, ex);
        }
    }

    @Override
    public void deleteByGuid(String guid) {
        var sql = """
                DELETE FROM Currency
                WHERE guid = ?
                """;
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, guid);
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated == 0) {
                throw new DataStorageException("Currency not found, guid: " + guid);
            }
            if (rowsUpdated > 1) {
                throw new DataStorageException("More then 1 currency (rows=%d) has been deleted: %s"
                        .formatted(rowsUpdated, guid));
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to delete currency, guid: " + guid, ex);
        }
    }

    @Override
    public void deleteAll() {
        var sql = "DELETE FROM Currency";
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to delete all currencies", ex);
        }
    }

    @Override
    public boolean existsByGuid(String guid) {
        var sql = """
                SELECT id
                FROM Currency
                WHERE guid = ?
                """;
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, guid);
            try (var resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to check if currencies exists, guid: " + guid, ex);
        }
    }

    private static CurrencyEntity currencyFromResultSet(ResultSet resultSet) throws SQLException {
        return new CurrencyEntity(
                resultSet.getLong("id"),
                resultSet.getString("guid"),
                resultSet.getString("code"),
                resultSet.getFloat("conversionRatio")
        );
    }
}
