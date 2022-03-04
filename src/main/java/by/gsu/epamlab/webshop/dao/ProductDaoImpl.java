package by.gsu.epamlab.webshop.dao;

import by.gsu.epamlab.webshop.connection.DataBaseConnectionsPool;
import by.gsu.epamlab.webshop.exceptions.ConnectionException;
import by.gsu.epamlab.webshop.exceptions.DaoException;
import by.gsu.epamlab.webshop.model.Byn;
import by.gsu.epamlab.webshop.model.Product;
import by.gsu.epamlab.webshop.service.Services;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ProductDaoImpl implements DaoGeneralInterface<Product> {
    private static final Logger LOGGER = LogManager.getLogger();
    Services services = new Services();

    public ProductDaoImpl() {
    }

    @Override
    public List<Product> getAll() throws DaoException, NoSuchMethodException {
        throw new NoSuchMethodException();
    }

    @Override
    public Optional<Product> getById(int idProduct) throws DaoException {
        Product product = null;
        String query = "SELECT idProduct, product_name, price, `describe`, quantity from products  WHERE idProduct =?";
        try {
            Connection connection = DataBaseConnectionsPool.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, idProduct);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int idPerson = resultSet.getInt(1);
                String product_name = resultSet.getString(2);
                int price = resultSet.getInt(3);
                String describe = resultSet.getString(4);
                double quantity = resultSet.getDouble(5);

                product = new Product(idPerson,product_name,new Byn(price),describe, quantity);
            }
            LOGGER.trace("Get Product by Login");
            DataBaseConnectionsPool.releaseConnection(connection);
        } catch (SQLException e) {
            LOGGER.error("Didn't get data from database in getByLogin()", e.getCause());
            throw new DaoException(e.getMessage(), e.getCause());
        } catch (ConnectionException e) {
            LOGGER.error("Didn't get connection in getByLogin()", e.getCause());
            throw new DaoException(e.getMessage(), e.getCause());
        }
        return Optional.ofNullable(product);
    }

    @Override
    public Optional<Product> getByLogin(String nameProductRequest) throws DaoException {
        Product product = null;
        String query = "SELECT idProduct, product_name, price, `describe`, quantity from products  WHERE product_name =?";
        try {
            Connection connection = DataBaseConnectionsPool.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, nameProductRequest);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int idPerson = resultSet.getInt(1);
                String product_name = resultSet.getString(2);
                int price = resultSet.getInt(3);
                String describe = resultSet.getString(4);
                double quantity = resultSet.getDouble(5);

                product = new Product(idPerson,product_name,new Byn(price),describe, quantity);
            }
            LOGGER.trace("Get Product by Login");
            DataBaseConnectionsPool.releaseConnection(connection);
        } catch (SQLException e) {
            LOGGER.error("Didn't get data from database in getByLogin()", e.getCause());
            throw new DaoException(e.getMessage(), e.getCause());
        } catch (ConnectionException e) {
            LOGGER.error("Didn't get connection in getByLogin()", e.getCause());
            throw new DaoException(e.getMessage(), e.getCause());
        }
        return Optional.ofNullable(product);
    }

    @Override
    public void update(Product entity) throws DaoException {
        String sql = "update products set product_name = ?, price = ?, `describe` = ?, quantity = ? where idProduct=?";
        try {
            Connection connection = DataBaseConnectionsPool.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, entity.getProductName());
            preparedStatement.setInt(2, entity.getPrice().getValue());
            preparedStatement.setString(3, entity.getDescribe());
            preparedStatement.setDouble(4, entity.getQuantity().doubleValue());
            preparedStatement.setInt(5, entity.getIdProduct());
             preparedStatement.executeUpdate();
            LOGGER.trace("Product updated in database");
            DataBaseConnectionsPool.releaseConnection(connection);
            preparedStatement.close();
        } catch (SQLException e) {
            LOGGER.error("Didn't get connection in update() or SQL error", e.getCause());
            throw new DaoException(e.getMessage(), e.getCause());
        } catch (ConnectionException e) {
            LOGGER.error("Didn't get connection in getAll()", e.getCause());
            throw new DaoException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public void delete(Product entity) throws DaoException {
        String sql = "DELETE FROM products where idProduct = ?";
        try {
            Connection connection = DataBaseConnectionsPool.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            try {
                preparedStatement.setObject(1, entity.getIdProduct());
            } catch (Exception e) {
                LOGGER.error("Didn't execute statment in delete()", e.getCause());
                throw new DaoException(e.getMessage());
            }
            int result = preparedStatement.executeUpdate();
            if (result != 1) {
                LOGGER.error("On delete modify more then 1 record: " + result);
                throw new DaoException("On delete modify more then 1 record: " + result);
            }
            preparedStatement.close();
            DataBaseConnectionsPool.releaseConnection(connection);
        } catch (SQLException e) {
            LOGGER.error("Didn't get connection in delete() or SQL error", e.getCause());
            throw new DaoException(e.getMessage(), e.getCause());
        } catch (ConnectionException ex) {
            LOGGER.error("Didn't get connection in delete()", ex.getCause());
            ex.printStackTrace();
        }
    }

    @Override
    public boolean add(Product entity) throws DaoException {
        boolean isAdd;
        try {
            Connection connection = DataBaseConnectionsPool.getConnection();
            String sqlAdd = "insert into products (idProduct, product_name, price, quantity, `describe`) values (?,?,?,?,?)";

            PreparedStatement preparedStatement = connection.prepareStatement(sqlAdd);
            preparedStatement.setInt(1, entity.getIdProduct());
            preparedStatement.setString(2, entity.getProductName());
            preparedStatement.setInt(3, entity.getPrice().getValue());
            preparedStatement.setDouble(4, entity.getQuantity().doubleValue());
            preparedStatement.setString(5, entity.getDescribe());
            int result = preparedStatement.executeUpdate();
            isAdd = (result == 1 ? true : false);
            LOGGER.trace("User Added in database");
            DataBaseConnectionsPool.releaseConnection(connection);
            preparedStatement.close();
        } catch (SQLException e) {
            LOGGER.error("Didn't get connection in add() or SQL request processing error", e.getCause());
            throw new DaoException(e.getMessage(), e.getCause());
        } catch (ConnectionException ex) {
            LOGGER.error("Didn't get connection in add()", ex.getCause());
            throw new DaoException(ex.getMessage(), ex.getCause());
        }
        return isAdd;
    }

}
