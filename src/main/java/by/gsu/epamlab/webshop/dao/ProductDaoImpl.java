package by.gsu.epamlab.webshop.dao;

import by.gsu.epamlab.webshop.connection.DataBaseConnectionsPool;
import by.gsu.epamlab.webshop.exceptions.ConnectionException;
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

public class ProductDaoImpl implements DaoGeneralInterface<Product> {
    private int numberOfRecords;

    public int getNumberOfRecords() {
        return numberOfRecords;
    }


    DataBaseConnectionsPool connectionsPool = DataBaseConnectionsPool.getConnectionsPool();

    @Override
    public List<Product> getAll() throws NoSuchMethodException {
        throw new NoSuchMethodException();
    }

    @Override
    public Optional<Product> getById(int idProduct) throws DaoException {
        final Logger LOGGER = LogManager.getLogger();
        Product product = null;
        Connection connection = null;
        String queryProduct = "SELECT idProduct, product_name, price, `describe` from products  WHERE idProduct =?";

        try {
            connection = connectionsPool.getConnection();
            try (PreparedStatement statementProduct = connection.prepareStatement(queryProduct)) {
                statementProduct.setInt(1, idProduct);
                ResultSet resultSet = statementProduct.executeQuery();
                while (resultSet.next()) {
                    product = resultSetGetProcessing(resultSet);
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
        return Optional.ofNullable(product);
    }

    @Override
    public Optional<Product> getByLogin(String nameProduct) throws DaoException {
        final Logger LOGGER = LogManager.getLogger();
        Product product = null;
        Connection connection = null;
        String query = "SELECT idProduct, product_name, price, `describe` from products  WHERE product_name =?";
        try {
            connection = connectionsPool.getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, nameProduct);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    product = resultSetGetProcessing(resultSet);
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
        return Optional.ofNullable(product);
    }

    @Override
    public void update(Product entity) throws DaoException {
        final Logger LOGGER = LogManager.getLogger();
        Connection connection = null;
        String sql = "update products set product_name = ?, price = ?, `describe` = ? where idProduct=?";
        try {
            connection = connectionsPool.getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatementSetProcessing(preparedStatement, entity);
                preparedStatement.executeUpdate();
                LOGGER.trace("Product updated in database");
            } catch (SQLException e) {
                LOGGER.error("Didn't get connection in update() or SQL error", e.getCause());
                throw new DaoException(e.getMessage(), e.getCause());
            }
        } catch (ConnectionException e) {
            LOGGER.error("Didn't get connection in getAll()", e.getCause());
            throw new DaoException(e.getMessage(), e.getCause());
        } finally {
            DataBaseConnectionsPool.releaseConnection(connection);
        }
    }

    @Override
    public void delete(Product entity) throws DaoException {
        final Logger LOGGER = LogManager.getLogger();
        Connection connection = null;
        String sql = "DELETE FROM products where idProduct = ?";
        try {
            connection = connectionsPool.getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setObject(1, entity.getIdProduct());
                int result = preparedStatement.executeUpdate();
                if (result != 1) {
                    LOGGER.error("On delete modify more then 1 record: " + result);
                    throw new DaoException("On delete modify more then 1 record: " + result);
                }
            } catch (SQLException e) {
                LOGGER.error("Didn't get connection in delete() or SQL error", e.getCause());
                throw new DaoException(e.getMessage(), e.getCause());
            }
        } catch (ConnectionException ex) {
            LOGGER.error("Didn't get connection in delete()", ex.getCause());
            ex.printStackTrace();
        } finally {
            DataBaseConnectionsPool.releaseConnection(connection);
        }
    }

    @Override
    public int add(Product entity) throws DaoException {
        final Logger LOGGER = LogManager.getLogger();
        int idProduct;
        Connection connection = null;
        String sqlAdd = "insert into products (product_name, price, `describe`, idProduct) values (?,?,?,?)";
        try {
            connection = connectionsPool.getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlAdd)) {
                preparedStatementSetProcessing(preparedStatement, entity);
                preparedStatement.executeUpdate();
                idProduct = entity.getIdProduct();
                LOGGER.trace("User Added in database");
            } catch (SQLException e) {
                LOGGER.error("Didn't get connection in add() or SQL request processing error", e.getCause());
                throw new DaoException(e.getMessage(), e.getCause());
            }
        } catch (ConnectionException ex) {
            LOGGER.error("Didn't get connection in add()", ex.getCause());
            throw new DaoException(ex.getMessage(), ex.getCause());
        } finally {
            DataBaseConnectionsPool.releaseConnection(connection);
        }
        return idProduct;
    }


    public List<Product> getAllByPage(int offset, int maxRows) throws DaoException {
        List<Product> products = new ArrayList<>();
        final Logger LOGGER = LogManager.getLogger();
        Connection connection = null;
        String sqlGet = "select * from products limit " + offset + " , " + maxRows;
        String sqlCount = "select count(*) from products";
        try {
            connection = connectionsPool.getConnection();
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(sqlGet);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    products.add(resultSetGetProcessing(resultSet));
                }
                preparedStatement = connection.prepareStatement(sqlCount);
                resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    numberOfRecords = Integer.parseInt(resultSet.getString("count(*)"));
                }
                preparedStatement.close();
            } catch (SQLException e) {
                LOGGER.error("Didn't get connection in add() or SQL request processing error", e.getCause());
                throw new DaoException(e.getMessage(), e.getCause());
            }
        } catch (ConnectionException ex) {
            LOGGER.error("Didn't get connection in add()", ex.getCause());
            throw new DaoException(ex.getMessage(), ex.getCause());
        } finally {
            DataBaseConnectionsPool.releaseConnection(connection);
        }
        return products;
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
