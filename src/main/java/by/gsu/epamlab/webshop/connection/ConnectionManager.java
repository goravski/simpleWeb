package by.gsu.epamlab.webshop.connection;

import by.gsu.epamlab.webshop.exceptions.ConnectionException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionManager implements AutoCloseable {
    private static final Logger LOGGER = LogManager.getLogger();
    private final static ConnectionPool CONNECTION_POOL = DataBaseConnectionsPool.getConnectionsPool();
    private Connection connection;

    public ConnectionManager()  {
        connection = null;
        try {
            connection = CONNECTION_POOL.getConnection();
        } catch (ConnectionException e) {
            LOGGER.error("Didn't get connection in getByLogin()", e.getCause());
        }
    }


    public Connection getConnection() {
        return connection;
    }

    @Override
    public void close() {
        CONNECTION_POOL.returnConnection(connection);
    }

    public void startTransaction() throws ConnectionException {
        try {
            connection.setAutoCommit(false);
        } catch (SQLException exception) {
            LOGGER.error(String.format("Method %s.setAutoCommit(false) is failed", connection), exception);
            throw new ConnectionException(String.format("Method %s.setAutoCommit(false) is failed", connection), exception);
        }
    }

    public void commitTransaction() throws ConnectionException {
        try {
            connection.commit();
        } catch (SQLException exception) {
            LOGGER.error(String.format("Method %s.commit() is failed", connection), exception);
            throw new ConnectionException(String.format("Method %s.commit() is failed", connection), exception);
        }
    }

    public void rollbackTransaction() throws ConnectionException {
        try {
            connection.rollback();
        } catch (SQLException exception) {
            LOGGER.error(String.format("Method %s.rollback() is failed", connection), exception);
            throw new ConnectionException(String.format("Method %s.rollback() is failed", connection), exception);
        }
    }

    public void endTransaction() throws ConnectionException {
        try {
            connection.setAutoCommit(true);
        } catch (SQLException exception) {
            LOGGER.error(String.format("Method %s.endTransaction() is failed", connection), exception);
            throw new ConnectionException(String.format("Method %s.endTransaction() is failed", connection), exception);
        }
    }

}
