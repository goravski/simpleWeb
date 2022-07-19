package by.gsu.epamlab.webshop.dao;

import by.gsu.epamlab.webshop.connection.ConnectionManager;
import by.gsu.epamlab.webshop.exceptions.DaoException;
import by.gsu.epamlab.webshop.model.Cart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Optional;

public class CartDaoImpl implements DaoGeneralInterface<Cart> {
    private ConnectionManager connectionManager ;

    public CartDaoImpl(ConnectionManager connectionManager){
        this.connectionManager = connectionManager;
    }
    final Logger LOGGER = LoggerFactory.getLogger(CartDaoImpl.class);
    @Override
    public Optional<Cart> getById(int personId) throws DaoException {
        String sqlIdCart = "select idCart, personId from cart where personId = ?";
        Cart cart = null;
        try (Connection connection = connectionManager.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlIdCart)) {
                preparedStatement.setInt(1, personId);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    int idCart = resultSet.getInt(1);
                    int idPerson = resultSet.getInt(2);
                    cart = new Cart(idCart, idPerson);
                }
                return Optional.ofNullable(cart);
            } catch (SQLException e) {
                LOGGER.error("SQL request processing error in CartDao getById()", e.getCause());
                throw new DaoException(e.getMessage(), e.getCause());
            }
        } catch (SQLException e) {
            LOGGER.error("Didn't get connection in CartDao getById()", e.getCause());
            throw new DaoException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public int add(Cart entity) throws DaoException {

        int idCart = 0;
        String sqlCart = "insert cart (personId) VALUES (?)";
        String sqlGetId = "select last_insert_id()";

        try (Connection connection = connectionManager.getConnection()) {
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
                return idCart;
            } catch (SQLException e) {
                LOGGER.error("SQL request processing error in CartDao add()", e.getCause());
                throw new DaoException(e.getMessage(), e.getCause());
            }
        } catch (SQLException ex) {
            LOGGER.error("Didn't get connection in CartDao add()", ex.getCause());
            throw new DaoException(ex.getMessage(), ex.getCause());
        }
    }
}
