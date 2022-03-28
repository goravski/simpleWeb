package by.gsu.epamlab.webshop.dao;

import by.gsu.epamlab.webshop.connection.DataBaseConnectionsPool;
import by.gsu.epamlab.webshop.exceptions.ConnectionException;
import by.gsu.epamlab.webshop.exceptions.DaoException;
import by.gsu.epamlab.webshop.model.Storage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class StorageDao implements DaoGeneralInterface<Storage> {
    DataBaseConnectionsPool connectionsPool = DataBaseConnectionsPool.getConnectionsPool();

    @Override
    public List getAll() throws DaoException, NoSuchMethodException {
        throw new NoSuchMethodException("Method not implemented");
    }

    @Override
    public Optional getById(int idProduct) throws DaoException {
        final Logger LOGGER = LogManager.getLogger();
        Connection connection = null;
        Storage storage = null;
        String queryProduct = "SELECT idstorage, quantity from storage_product  WHERE idstorage =?";
        try {
            connection = connectionsPool.getConnection();
            try (PreparedStatement statementProduct = connection.prepareStatement(queryProduct)) {
                statementProduct.setInt(1, idProduct);
                ResultSet resultSet = statementProduct.executeQuery();
                while (resultSet.next()) {
                    storage = resultSetGetProcessing(resultSet);
                }
                LOGGER.trace("Get Product by Login");
            } catch (SQLException e) {
                LOGGER.error("Didn't get data from database in getByLogin()", e.getCause());
                throw new DaoException(e.getMessage(), e.getCause());
            }
        } catch (ConnectionException e) {
            LOGGER.error("Didn't get connection in getByLogin()", e.getCause());
            throw new DaoException(e.getMessage(), e.getCause());
        } finally {
            DataBaseConnectionsPool.releaseConnection(connection);
        }
        return Optional.ofNullable(storage);
    }

    @Override
    public Optional getByLogin(String loginRequest) throws DaoException, NoSuchMethodException {
        throw new NoSuchMethodException("Method not implemented");
    }

    @Override
    public void update(Storage entity) throws DaoException {
        final Logger LOGGER = LogManager.getLogger();
        Connection connection = null;
        String query = "update storage_product set quantity =? where idstorage =?";
        try {
            connection = connectionsPool.getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, entity.getQuantity());
                preparedStatement.setInt(2, entity.getProductId());
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                LOGGER.error("Didn't add storage quantity database in update()", e.getCause());
                throw new DaoException(e.getMessage(), e.getCause());
            }
        } catch (ConnectionException e) {
            LOGGER.error("Didn't get connection in update()", e.getCause());
            throw new DaoException(e.getMessage(), e.getCause());
        } finally {
            DataBaseConnectionsPool.releaseConnection(connection);
        }
    }

    @Override
    public void delete(Storage entity) throws DaoException, NoSuchMethodException {

        throw new NoSuchMethodException("Method not implemented");
    }

    @Override
    public int add(Storage entity) throws DaoException {
        int idStorage = 0;
        int productId = entity.getProductId();
        final Logger LOGGER = LogManager.getLogger();
        Connection connection = null;
        String query = "insert storage_product (idstorage, quantity) values (?,?)";
        try {
            connection = connectionsPool.getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, productId);
                preparedStatement.setInt(2, entity.getQuantity());
                preparedStatement.executeUpdate();
                idStorage = productId;
            } catch (SQLException e) {
                LOGGER.error("Didn't add storage quantity database in add()", e.getCause());
                throw new DaoException(e.getMessage(), e.getCause());
            }
        } catch (ConnectionException e) {
            LOGGER.error("Didn't get connection in add()", e.getCause());
            throw new DaoException(e.getMessage(), e.getCause());
        } finally {
            DataBaseConnectionsPool.releaseConnection(connection);
        }
        return idStorage;
    }

    private static Storage resultSetGetProcessing(ResultSet resultSet) throws SQLException {
        int idStorage = resultSet.getInt(1);
        int productId = idStorage;
        int quantity = resultSet.getInt(2);
        return new Storage(idStorage, productId, quantity);
    }
}
