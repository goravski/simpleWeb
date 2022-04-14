package by.gsu.epamlab.webshop.connection;


import by.gsu.epamlab.webshop.exceptions.ConnectionException;
import java.sql.Connection;

public interface ConnectionPool {
    Connection getConnection() throws ConnectionException;
    void returnConnection(Connection connection);
}
