package by.gsu.epamlab.webshop.connection;

import by.gsu.epamlab.webshop.exceptions.ConnectionException;
import by.gsu.epamlab.webshop.exceptions.ConstantException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class DataBaseConnectionsPool implements ConnectionPool {
    private String url;
    private String user;
    private String password;
    private static BlockingQueue<Connection> connectionPool;
    private static List<Connection> usedConnections;
    private static final Properties PROPERTIES = new Properties();

    private DataBaseConnectionsPool(String url, String user, String password, BlockingQueue<Connection> connectionPool) {
        this.url = url;
        this.user = user;
        this.password = password;
        this.connectionPool = connectionPool;
        usedConnections = new ArrayList<>();
    }

    private static final Logger LOGGER = LogManager.getLogger();

    static {
        try (InputStream inputStream = DataBaseConnectionsPool.class.getClassLoader().getResourceAsStream("db.properties")) {
            PROPERTIES.load(inputStream);
            Class.forName(PROPERTIES.getProperty("db.driverClassName"));
            initConnectionPoll();
        } catch (IOException e) {
            LOGGER.error("Load properties failed", e.getCause());
            throw new RuntimeException(e.getMessage(), e.fillInStackTrace());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            LOGGER.error("Class.forName not find com.mysql.cj.jdbc.Driver",e.getCause());
        } catch (ConnectionException e) {
            LOGGER.error("init connection poll error in static block", e.getCause());
            e.printStackTrace();
        }
    }

    private static ConnectionPool initConnectionPoll() throws ConnectionException {
        int poolSize = Integer.parseInt(PROPERTIES.getProperty("db.pool.size"));
        connectionPool = new ArrayBlockingQueue<>(poolSize);
        for (int i = 0; i < poolSize; i++) {
            try {
                connectionPool.add(openConnection());
            } catch (SQLException e) {
                LOGGER.error("Connection with database error during int Connection Pool", e.getCause());
                throw new ConnectionException(e.getMessage(), e.getCause());
            }
        }
        return new DataBaseConnectionsPool(PROPERTIES.getProperty("db.url"),
                PROPERTIES.getProperty("db.username"), PROPERTIES.getProperty("db.password"), connectionPool);
    }

    private static Connection openConnection() throws SQLException {
        return DriverManager.getConnection(PROPERTIES.getProperty("db.url"),
                PROPERTIES.getProperty("db.username"), PROPERTIES.getProperty("db.password"));
    }

    public static Optional<Connection> getConnection() throws ConnectionException {
        int poolSize = Integer.parseInt(PROPERTIES.getProperty("db.pool.size"));
        Optional<Connection> optionalConnection = Optional.empty();
            try {
                optionalConnection = Optional.of(connectionPool.take());
            } catch (InterruptedException e) {
                LOGGER.error("Failed to get connection from Queue", e.getCause());
                throw new ConnectionException(ConstantException.ERROR_CAN_NOT_TAKE_CONNECTION.toString(), e.getCause());
            }
        usedConnections.add(optionalConnection.get());
        return optionalConnection;
    }


    public boolean releaseConnection(Connection connection) {
        connectionPool.add(connection);
        return usedConnections.remove(connection);
    }

    public int getSize() {
        return connectionPool.size() + usedConnections.size();
    }

    public void shutdown() throws SQLException {
        usedConnections.forEach(this::releaseConnection);
        for (Connection connection : connectionPool) {
            connection.close();
        }
        connectionPool.clear();
    }
}
