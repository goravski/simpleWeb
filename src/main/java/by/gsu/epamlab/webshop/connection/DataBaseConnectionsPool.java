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
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;

public class DataBaseConnectionsPool implements ConnectionPool {
    private static final Logger LOGGER = LogManager.getLogger();

    private static BlockingQueue<Connection> connectionBlockingQueue;
    private final static List<Connection> usedConnections = new CopyOnWriteArrayList<>();
    private final static Properties PROPERTIES = new Properties();


    static {
        try (InputStream inputStream = DataBaseConnectionsPool.class.getClassLoader().getResourceAsStream("db.properties")) {
            PROPERTIES.load(inputStream);
            Class.forName(PROPERTIES.getProperty("db.driverClassName"));
        } catch (IOException e) {
            LOGGER.error("Load properties failed", e.getCause());
            throw new RuntimeException(e.getMessage(), e.fillInStackTrace());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            LOGGER.error("Class.forName not find com.mysql.cj.jdbc.Driver", e.getCause());
        }
    }

    private static Connection openConnection() throws SQLException {
        return DriverManager.getConnection(PROPERTIES.getProperty("db.url"),
                PROPERTIES.getProperty("db.username"), PROPERTIES.getProperty("db.password"));
    }

    private final static DataBaseConnectionsPool CONNECTIONS_POOL = new DataBaseConnectionsPool();

    public static DataBaseConnectionsPool getConnectionsPool() {
        return CONNECTIONS_POOL;
    }

    private DataBaseConnectionsPool() {
        int poolSize = Integer.parseInt(PROPERTIES.getProperty("db.pool.size"));
        connectionBlockingQueue = new ArrayBlockingQueue<>(poolSize);
        for (int i = 0; i < poolSize; i++) {
            try {
                connectionBlockingQueue.offer(openConnection());
            } catch (SQLException e) {
                LOGGER.error("Connection with database error during open connection", e.getCause());

            }
        }
    }


    public static Connection getConnection() throws ConnectionException {
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


    public static void releaseConnection(Connection connection) {
        connectionBlockingQueue.add(connection);
        usedConnections.remove(connection);
    }
}
