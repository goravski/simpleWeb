package by.gsu.epamlab.webshop.dao;

import by.gsu.epamlab.webshop.connection.DataBaseConnectionsPool;
import by.gsu.epamlab.webshop.exceptions.ConnectionException;
import by.gsu.epamlab.webshop.exceptions.DaoException;
import by.gsu.epamlab.webshop.model.Cart;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.List;
import java.util.Optional;

public class CartDaoImpl implements DaoGeneralInterface<Cart> {
    DataBaseConnectionsPool connectionsPool = DataBaseConnectionsPool.getConnectionsPool();

    @Override
    public List<Cart> getAll() throws NoSuchMethodException {
        throw new NoSuchMethodException("Method not implemented");
    }

    @Override
    public Optional<Cart> getById(int personId) throws DaoException {
        final Logger LOGGER = LogManager.getLogger();
        String sqlIdCart = "select idCart, personId from cart where personId = ?";
        Connection connection = null;
        Cart cart = null;
        try {
            connection = connectionsPool.getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlIdCart)) {
                preparedStatement.setInt(1, personId);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    int idCart = resultSet.getInt(1);
                    int idPerson = resultSet.getInt(2);
                    cart = new Cart(idCart, idPerson);
                }
            } catch (SQLException e) {
                LOGGER.error("Didn't get connection in Cart get() or SQL request processing error", e.getCause());
                throw new DaoException(e.getMessage(), e.getCause());
            }
        } catch (ConnectionException e) {
            LOGGER.error("Didn't get connection in getByLogin()", e.getCause());
            throw new DaoException(e.getMessage(), e.getCause());
        } finally {
            DataBaseConnectionsPool.releaseConnection(connection);
        }
        return Optional.ofNullable(cart);
    }

    @Override
    public Optional<Cart> getByLogin(String loginRequest) {
        return Optional.empty();
    }

    @Override
    public void update(Cart entity) throws DaoException, NoSuchMethodException {
        throw new NoSuchMethodException("Method not implemented");
    }

    @Override
    public void delete(Cart entity) throws NoSuchMethodException {
        throw new NoSuchMethodException("Method not implemented");
    }

    @Override
    public int add(Cart entity) throws DaoException {
        final Logger LOGGER = LogManager.getLogger();
        int idCart = 0;
        String sqlCart = "insert cart (personId) VALUES (?)";
        String sqlGetId = "select last_insert_id()";
        Connection connection = null;
        try {
            connection = connectionsPool.getConnection();
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(sqlCart);
                preparedStatement.setInt(1, entity.getPersonId());
                preparedStatement.executeUpdate();
                preparedStatement = connection.prepareStatement(sqlGetId);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    idCart = resultSet.getInt(1);
                }
                preparedStatement.close();
            } catch (SQLException e) {
                LOGGER.error("Didn't get connection in add() or SQL request processing error", e.getCause());
                throw new DaoException(e.getMessage(), e.getCause());
            }
        } catch (ConnectionException ex) {
            LOGGER.error("Didn't get connection in CartDao add()", ex.getCause());
            throw new DaoException(ex.getMessage(), ex.getCause());
        } finally {
            DataBaseConnectionsPool.releaseConnection(connection);
        }
        return idCart;
    }
}
