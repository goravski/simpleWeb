package by.gsu.epamlab.webshop.dao;

import by.gsu.epamlab.webshop.connection.ConnectionManager;
import by.gsu.epamlab.webshop.exceptions.DaoException;
import by.gsu.epamlab.webshop.model.Byn;
import by.gsu.epamlab.webshop.model.Product;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class ProductDaoImpl implements DaoGeneralInterface<Product> {
    private AtomicInteger numberOfRecords;

    public AtomicInteger getNumberOfRecords() {
        return numberOfRecords;
    }

    ConnectionManager connectionManager;

    public ProductDaoImpl(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public Optional<Product> getById(int idProduct) throws DaoException {
        final Logger LOGGER = LogManager.getLogger();
        Product product = null;
        String queryProduct = "SELECT idProduct, product_name, price, `describe` from products  WHERE idProduct =?";
        try (Connection connection = connectionManager.getConnection()) {
            try (PreparedStatement statementProduct = connection.prepareStatement(queryProduct)) {
                statementProduct.setInt(1, idProduct);
                ResultSet resultSet = statementProduct.executeQuery();
                while (resultSet.next()) {
                    product = resultSetGetProcessing(resultSet);
                }
                LOGGER.trace("Get Product by Login");
                return Optional.ofNullable(product);
            } catch (SQLException e) {
                LOGGER.error("SQL request processing error in ProductDao getByLogin()", e.getCause());
                throw new DaoException(e.getMessage(), e.getCause());
            }
        } catch (SQLException e) {
            LOGGER.error("Didn't get connection in ProductDao getByLogin()", e.getCause());
            throw new DaoException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public Optional<Product> getByLogin(String nameProduct) throws DaoException {
        final Logger LOGGER = LogManager.getLogger();
        Product product = null;
        String query = "SELECT idProduct, product_name, price, `describe` from products  WHERE product_name =?";
        try (Connection connection = connectionManager.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, nameProduct);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    product = resultSetGetProcessing(resultSet);
                }
                LOGGER.trace("Get Product by Login");
                return Optional.ofNullable(product);
            } catch (SQLException e) {
                LOGGER.error("SQL request processing error in ProductDao getByLogin()", e.getCause());
                throw new DaoException(e.getMessage(), e.getCause());
            }
        } catch (SQLException e) {
            LOGGER.error("Didn't get connection in ProductDao getByLogin()", e.getCause());
            throw new DaoException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public void update(Product entity) throws DaoException {
        final Logger LOGGER = LogManager.getLogger();
        String sql = "update products set product_name = ?, price = ?, `describe` = ? where idProduct=?";
        try (Connection connection = connectionManager.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatementSetProcessing(preparedStatement, entity);
                preparedStatement.executeUpdate();
                LOGGER.trace("Product updated in database");
            } catch (SQLException e) {
                LOGGER.error("SQL request processing error in ProductDao update()", e.getCause());
                throw new DaoException(e.getMessage(), e.getCause());
            }
        } catch (SQLException e) {
            LOGGER.error("Didn't get connection in ProductDao update()", e.getCause());
            throw new DaoException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public void delete(Product entity) throws DaoException {
        final Logger LOGGER = LogManager.getLogger();
        String sql = "DELETE FROM products where idProduct = ?";
        try (Connection connection = connectionManager.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setObject(1, entity.getIdProduct());
                int result = preparedStatement.executeUpdate();
                if (result != 1) {
                    LOGGER.error("On delete modify more then 1 record: " + result);
                    throw new DaoException("On delete modify more then 1 record: " + result);
                }
            } catch (SQLException e) {
                LOGGER.error("SQL request processing error in ProductDao delete()", e.getCause());
                throw new DaoException(e.getMessage(), e.getCause());
            }
        } catch (SQLException ex) {
            LOGGER.error("Didn't get connection in ProductDao delete()", ex.getCause());
            ex.printStackTrace();
        }
    }

    @Override
    public int add(Product entity) throws DaoException {
        final Logger LOGGER = LogManager.getLogger();
        int idProduct = 0;
        String sqlAdd = "insert into products (product_name, price, `describe`, idProduct) values (?,?,?,?)";
        String sqlGet = "SELECT * from products WHERE idProduct = ?";
        try (Connection connection = connectionManager.getConnection()) {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(sqlAdd);
                preparedStatementSetProcessing(preparedStatement, entity);
                preparedStatement.executeUpdate();
                preparedStatement = connection.prepareStatement(sqlGet);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    idProduct = resultSet.getInt(1);
                    LOGGER.trace("User Added in database");
                }
                preparedStatement.close();
                return idProduct;
            } catch (SQLException e) {
                LOGGER.error("SQL request processing error in ProductDao add()", e.getCause());
                throw new DaoException(e.getMessage(), e.getCause());
            }
        } catch (SQLException ex) {
            LOGGER.error("Didn't get connection in ProductDao add()", ex.getCause());
            throw new DaoException(ex.getMessage(), ex.getCause());
        }
    }


    public List<Product> getAllByPage(int offset, int maxRows) throws DaoException {
        List<Product> products = new ArrayList<>();
        final Logger LOGGER = LogManager.getLogger();
        String sqlGet = "select * from products limit " + offset + " , " + maxRows;
        String sqlCount = "select count(*) from products";
        try (Connection connection = connectionManager.getConnection()) {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(sqlGet);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    products.add(resultSetGetProcessing(resultSet));
                }
                preparedStatement = connection.prepareStatement(sqlCount);
                resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    int result = Integer.parseInt(resultSet.getString("count(*)"));
                    numberOfRecords = new AtomicInteger(result);
                }
                preparedStatement.close();
                return products;
            } catch (SQLException e) {
                LOGGER.error("Didn't get connection in add() or SQL request processing error", e.getCause());
                throw new DaoException(e.getMessage(), e.getCause());
            }
        } catch (SQLException ex) {
            LOGGER.error("Didn't get connection in add()", ex.getCause());
            throw new DaoException(ex.getMessage(), ex.getCause());
        }
    }

    private static Product resultSetGetProcessing(ResultSet resultSet) throws SQLException {
        int idPerson = resultSet.getInt(1);
        String product_name = resultSet.getString(2);
        int price = resultSet.getInt(3);
        String describe = resultSet.getString(4);
        return new Product(idPerson, product_name, new Byn(price), describe);
    }

    private static void preparedStatementSetProcessing(PreparedStatement preparedStatement, Product entity) throws SQLException {
        preparedStatement.setInt(4, entity.getIdProduct());
        preparedStatement.setString(1, entity.getProductName());
        preparedStatement.setInt(2, entity.getPrice().getValue());
        preparedStatement.setString(3, entity.getDescribe());
    }
}
