package by.gsu.epamlab.webshop.connection;

import by.gsu.epamlab.webshop.exceptions.ConnectionException;

import java.sql.Connection;
import java.util.Optional;

public interface ConnectionPool {
    Optional<Connection> getConnection () throws ConnectionException;
    boolean releaseConnection(Connection connection);

}
