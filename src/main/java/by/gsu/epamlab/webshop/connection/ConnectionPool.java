package by.gsu.epamlab.webshop.connection;


import java.sql.Connection;

public interface ConnectionPool {

    boolean releaseConnection(Connection connection);

}
