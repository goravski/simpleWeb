package by.gsu.epamlab.webshop.dao;

import by.gsu.epamlab.webshop.connection.ConnectionManager;
import by.gsu.epamlab.webshop.exceptions.DaoException;
import by.gsu.epamlab.webshop.model.Storage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class StorageDao implements DaoGeneralInterface<Storage> {
    ConnectionManager connectionManager;

    public StorageDao(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public Optional <Storage> getById(int idProduct) throws DaoException {
        final Logger LOGGER = LogManager.getLogger();
        Storage storage = null;
        String queryStorage = "SELECT idstorage, quantity from storage_product  WHERE idstorage =?";
        try (Connection connection = connectionManager.getConnection()) {
            try (PreparedStatement statementProduct = connection.prepareStatement(queryStorage)) {
                statementProduct.setInt(1, idProduct);
                ResultSet resultSet = statementProduct.executeQuery();
                while (resultSet.next()) {
                    storage = resultSetGetProcessing(resultSet);
                }
                LOGGER.trace("Get Product by Login");
                return Optional.ofNullable(storage);
            } catch (SQLException e) {
                LOGGER.error("SQL error in StorageDao getByLogin()", e.getCause());
                throw new DaoException(e.getMessage(), e.getCause());
            }
        } catch (SQLException e) {
            LOGGER.error("Didn't get connection in getByLogin()", e.getCause());
            throw new DaoException(e.getMessage(), e.getCause());
        }
    }


    @Override
    public void update(Storage entity) throws DaoException {
        final Logger LOGGER = LogManager.getLogger();
        String query = "update storage_product set quantity =? where idstorage =?";
        try (Connection connection = connectionManager.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, entity.getQuantity());
                preparedStatement.setInt(2, entity.getProductId());
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                LOGGER.error("SQL error in StorageDao update()", e.getCause());
                throw new DaoException(e.getMessage(), e.getCause());
            }
        } catch (SQLException e) {
            LOGGER.error("Didn't get connection in StorageDao update()", e.getCause());
            throw new DaoException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public int add(Storage entity) throws DaoException {
        int idStorage;
        int productId = entity.getProductId();
        final Logger LOGGER = LogManager.getLogger();
        String query = "insert storage_product (idstorage, quantity) values (?,?)";
        try (Connection connection = connectionManager.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, productId);
                preparedStatement.setInt(2, entity.getQuantity());
                preparedStatement.executeUpdate();
                idStorage = productId;
                return idStorage;
            } catch (SQLException e) {
                LOGGER.error("Didn't add storage quantity database in add()", e.getCause());
                throw new DaoException(e.getMessage(), e.getCause());
            }
        } catch (SQLException e) {
            LOGGER.error("Didn't get connection in add()", e.getCause());
            throw new DaoException(e.getMessage(), e.getCause());
        }
    }

    private static Storage resultSetGetProcessing(ResultSet resultSet) throws SQLException {
        int idStorage = resultSet.getInt(1);
        int productId = idStorage;
        int quantity = resultSet.getInt(2);
        return new Storage(idStorage, productId, quantity);
    }
}
