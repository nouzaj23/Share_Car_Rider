package cz.muni.fi.pv168.project.storage.sql.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;

import cz.muni.fi.pv168.project.storage.sql.DataStorageException;

/**
 * Connection handler which is aware of active transaction on managed connection
 */
class ConnectionHandlerImpl implements ConnectionHandler {

    private final Connection connection;

    /**
     * Creates new handler over given connection
     * @param connection database connection
     */
    ConnectionHandlerImpl(Connection connection) {
        this.connection = Objects.requireNonNull(connection, "Missing connection object");
    }

    @Override
    public Connection use() {
        return connection;
    }

    @Override
    public void close() {
        try {
            if (connection.getAutoCommit()) {
                // Not transaction, connection can be closed
                connection.close();
            }
        } catch (SQLException e) {
            throw new DataStorageException("Unable close database connection", e);
        }
    }
}
