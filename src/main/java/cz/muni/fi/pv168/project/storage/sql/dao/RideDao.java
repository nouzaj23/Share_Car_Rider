package cz.muni.fi.pv168.project.storage.sql.dao;

import cz.muni.fi.pv168.project.storage.sql.db.ConnectionHandler;
import cz.muni.fi.pv168.project.storage.sql.entity.RideEntity;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public final class RideDao implements DataAccessObject<RideEntity> {

    private final Supplier<ConnectionHandler> connections;

    public RideDao(Supplier<ConnectionHandler> connections) {
        this.connections = connections;
    }

    @Override
    public RideEntity create(RideEntity newRide) {
        var sql = """
                INSERT INTO Ride(
                    guid,
                    name,
                    passengers,
                    currencyId,
                    fuelExpenses,
                    categoryId,
                    "from",
                    "to",
                    distance,
                    date
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);
                """;
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, newRide.guid());
            statement.setString(2, newRide.name());
            statement.setInt(3, newRide.passengers());
            statement.setLong(4, newRide.currencyId());
            statement.setFloat(5, newRide.fuelExpenses());
            statement.setLong(6, newRide.categoryId());
            statement.setString(7, newRide.from());
            statement.setString(8, newRide.to());
            statement.setInt(9, newRide.distance());
            statement.setDate(10, Date.valueOf(newRide.date()));
            statement.executeUpdate();

            try (var keyResultSet = statement.getGeneratedKeys()) {
                long rideId;

                if (keyResultSet.next()) {
                    rideId = keyResultSet.getLong(1);
                } else {
                    throw new DataStorageException("Failed to fetch generated key for: " + newRide);
                }
                if (keyResultSet.next()) {
                    throw new DataStorageException("Multiple keys returned for: " + newRide);
                }

                return findById(rideId).orElseThrow();
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to store: " + newRide, ex);
        }
    }

    @Override
    public Collection<RideEntity> findAll() {
        var sql = """
                SELECT  id,
                        guid,
                        name,
                        passengers,
                        currencyId,
                        fuelExpenses,
                        categoryId,
                        "from",
                        "to",
                        distance,
                        date
                FROM Ride
                """;
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {

            List<RideEntity> rides = new ArrayList<>();
            try (var resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    var ride = rideFromResultSet(resultSet);
                    rides.add(ride);
                }
            }

            return rides;
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to load all rides", ex);
        }
    }

    @Override
    public Optional<RideEntity> findById(long id) {
        var sql = """
                SELECT  id,
                        guid,
                        name,
                        passengers,
                        currencyId,
                        fuelExpenses,
                        categoryId,
                        "from",
                        "to",
                        distance,
                        date
                FROM Ride
                WHERE id = ?
                """;
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setLong(1, id);
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(rideFromResultSet(resultSet));
            } else {
                // ride not found
                return Optional.empty();
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to load ride by id", ex);
        }
    }

    @Override
    public Optional<RideEntity> findByGuid(String guid) {
        var sql = """
                SELECT  id,
                        guid,
                        name,
                        passengers,
                        currencyId,
                        fuelExpenses,
                        categoryId,
                        "from",
                        "to",
                        distance,
                        date
                FROM Ride
                WHERE guid = ?
                """;
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, guid);
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(rideFromResultSet(resultSet));
            } else {
                // ride not found
                return Optional.empty();
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to load ride by id", ex);
        }
    }

    @Override
    public RideEntity update(RideEntity entity) {
        var sql = """
                UPDATE Ride
                SET name = ?,
                    passengers = ?,
                    currencyId = ?,
                    fuelExpenses = ?,
                    categoryId = ?,
                    "from" = ?,
                    "to" = ?,
                    distance = ?,
                    date = ?
                WHERE id = ?
                """;
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, entity.name());
            statement.setInt(2, entity.passengers());
            statement.setLong(3, entity.currencyId());
            statement.setFloat(4, entity.fuelExpenses());
            statement.setLong(5, entity.categoryId());
            statement.setString(6, entity.from());
            statement.setString(7, entity.to());
            statement.setInt(8, entity.distance());
            statement.setDate(9, Date.valueOf(entity.date()));
            statement.setLong(10, entity.id());
            statement.executeUpdate();

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated == 0) {
                throw new DataStorageException("Ride not found, id: " + entity.id());
            }
            if (rowsUpdated > 1) {
                throw new DataStorageException("More then 1 ride (rows=%d) has been updated: %s"
                        .formatted(rowsUpdated, entity));
            }
            return entity;
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to update ride: " + entity, ex);
        }
    }

    @Override
    public void deleteByGuid(String guid) {
        var sql = "DELETE FROM Ride WHERE guid = ?";
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, guid);
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated == 0) {
                throw new DataStorageException("Ride not found, guid: " + guid);
            }
            if (rowsUpdated > 1) {
                throw new DataStorageException("More then 1 ride (rows=%d) has been deleted: %s"
                        .formatted(rowsUpdated, guid));
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to delete ride, guid: " + guid, ex);
        }
    }

    @Override
    public void deleteAll() {
        var sql = "DELETE FROM Ride";
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to delete all rides", ex);
        }
    }

    @Override
    public boolean existsByGuid(String guid) {
        var sql = """
                SELECT id
                FROM Ride
                WHERE guid = ?
                """;
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, guid);
            var resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to check if ride exists: " + guid, ex);
        }
    }

    private static RideEntity rideFromResultSet(ResultSet resultSet) throws SQLException {
        return new RideEntity(
                resultSet.getLong("id"),
                resultSet.getString("guid"),
                resultSet.getString("name"),
                resultSet.getInt("passengers"),
                resultSet.getLong("currencyId"),
                resultSet.getFloat("fuelExpenses"),
                resultSet.getLong("categoryId"),
                resultSet.getString("from"),
                resultSet.getString("to"),
                resultSet.getInt("distance"),
                resultSet.getDate("date").toLocalDate()
        );
    }
}
