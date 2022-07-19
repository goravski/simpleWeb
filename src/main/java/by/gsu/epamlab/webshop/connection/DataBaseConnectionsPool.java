package by.gsu.epamlab.webshop.connection;

import by.gsu.epamlab.webshop.exceptions.ConnectionException;
import by.gsu.epamlab.webshop.exceptions.ConstantException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;

public class DataBaseConnectionsPool implements ConnectionPool {
    static final Logger LOGGER = LoggerFactory.getLogger(DataBaseConnectionsPool.class);
    private static DataBaseConnectionsPool CONNECTIONS_POOL;
    private static BlockingQueue<Connection> connectionBlockingQueue;
    private final static List<Connection> usedConnections = new CopyOnWriteArrayList<>();
    private final static Properties PROPERTIES = new Properties();

    private DataBaseConnectionsPool() {
        try (InputStream inputStream = DataBaseConnectionsPool.class.getClassLoader()
                .getResourceAsStream("db.properties")) {
            PROPERTIES.load(inputStream);
            Class.forName(PROPERTIES.getProperty("db.driverClassName"));
        } catch (IOException e) {
            LOGGER.error("Load properties failed", e.getCause());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            LOGGER.error("Class.forName not find com.mysql.cj.jdbc.Driver", e.getCause());
        }
        int poolSize = Integer.parseInt(PROPERTIES.getProperty("db.pool.size"));
        connectionBlockingQueue = new ArrayBlockingQueue<>(poolSize);
        for (int i = 0; i < poolSize; i++) {
            try {
                connectionBlockingQueue.offer(openConnection());
            } catch (SQLException | ConnectionException e) {
                LOGGER.error("Connection with database error during open connection", e.getCause());
            }
        }
    }

    public static DataBaseConnectionsPool getConnectionsPool() {
        if (CONNECTIONS_POOL == null) {
            CONNECTIONS_POOL = new DataBaseConnectionsPool();
        }
        return CONNECTIONS_POOL;
    }

    private Connection openConnection() throws SQLException, ConnectionException {
        return DriverManager.getConnection(PROPERTIES.getProperty("db.url"),
                PROPERTIES.getProperty("db.username"), PROPERTIES.getProperty("db.password"));
    }


    @Override
    public Connection getConnection() throws ConnectionException {
        try {
            Connection connection = connectionBlockingQueue.take();
            LOGGER.trace("Get connection from connection pool queue");
            usedConnections.add(connection);
            LOGGER.trace("Add the connection to list used");
            return connection;
        } catch (InterruptedException e) {
            LOGGER.error("Failed to get connection from Queue", e.getCause());
            throw new ConnectionException(ConstantException.ERROR_CAN_NOT_TAKE_CONNECTION.toString(), e.getCause());
        }
    }

    @Override
    public void returnConnection(Connection connection) {
        connectionBlockingQueue.add(connection);
        usedConnections.remove(connection);
    }

}
