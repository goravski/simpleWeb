package by.gsu.epamlab.webshop.connection;

import by.gsu.epamlab.webshop.exceptions.ConnectionException;
import by.gsu.epamlab.webshop.exceptions.ConstantException;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class DataBaseConnectionsPool implements ConnectionPool {
    private String url;
    private String user;
    private String password;
    private BlockingQueue<Connection> connectionPool;
    private List<Connection> usedConnections;


    public DataBaseConnectionsPool(String url, String user, String password, BlockingQueue<Connection> connectionPool) {
        this.url = url;
        this.user = user;
        this.password = password;
        this.connectionPool = connectionPool;
        usedConnections = new ArrayList<>();
    }

    private static Connection createConnection(String url, String user, String password) throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    public static DataBaseConnectionsPool create() throws SQLException, ClassNotFoundException {
        Class.forName(ConnectionPropertiesConstant.DRIVER_PROPERTY);
        BlockingQueue<Connection> pool = new ArrayBlockingQueue<>(ConnectionPropertiesConstant.INITIAL_POOL_SIZE);
        for (int i = 0; i < ConnectionPropertiesConstant.INITIAL_POOL_SIZE; i++) {
            pool.add(createConnection(ConnectionPropertiesConstant.URL_PROPERTY,
                    ConnectionPropertiesConstant.USER_PROPERTY, ConnectionPropertiesConstant.PASSWORD_PROPERTY));
        }
        return new DataBaseConnectionsPool(ConnectionPropertiesConstant.URL_PROPERTY,
                ConnectionPropertiesConstant.USER_PROPERTY, ConnectionPropertiesConstant.PASSWORD_PROPERTY, pool);
    }

    @Override
    public Optional<Connection> getConnection() throws ConnectionException {
        Optional<Connection> optionalConnection = Optional.empty();
        int connectionAttempts = 20;
        int count = 0;
        while (count < connectionAttempts || !optionalConnection.isPresent()) {
            try {
                optionalConnection = Optional.of(connectionPool.take());
            } catch (InterruptedException e) {
                throw new ConnectionException(ConstantException.ERROR_CAN_NOT_TAKE_CONNECTION.toString(), e.getCause());
            }
        }
        usedConnections.add(optionalConnection.get());
        return optionalConnection;
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

    @Override
    public boolean releaseConnection(Connection connection) {
        connectionPool.add(connection);
        return usedConnections.remove(connection);
    }
}
