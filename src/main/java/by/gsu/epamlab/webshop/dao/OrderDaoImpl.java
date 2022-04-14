package by.gsu.epamlab.webshop.dao;

import by.gsu.epamlab.webshop.connection.ConnectionManager;
import by.gsu.epamlab.webshop.exceptions.DaoException;
import by.gsu.epamlab.webshop.model.Order;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.List;


public class OrderDaoImpl implements DaoGeneralInterface<Order> {
    ConnectionManager connectionManager;

    public OrderDaoImpl(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public int add(Order entity) throws DaoException {
        final Logger LOGGER = LogManager.getLogger();
        int idOrder;
        try (Connection connection = connectionManager.getConnection()) {
            idOrder = transferOrder(connection, entity);
            return idOrder;
        } catch (SQLException ex) {
            LOGGER.error("Didn't get connection in OrderDao add(Order)", ex.getCause());
            throw new DaoException(ex.getMessage(), ex.getCause());
        }
    }

    @Override
    public void add(List<Order> orderList) throws DaoException {
        final Logger LOGGER = LogManager.getLogger();
        try (Connection connection = connectionManager.getConnection()) {
            for (Order order : orderList){
                transferOrder(connection, order);
            }
        } catch (SQLException ex) {
            LOGGER.error("Didn't get connection in OrderDao add(List<Order>)", ex.getCause());
            throw new DaoException(ex.getMessage(), ex.getCause());
        }
    }

    public int transferOrder(Connection connection, Order entity) throws DaoException {
        int idOrder = 0;
        int quantityProducts;
        int resultProducts = 0;
        int quantityOrder = entity.getOrderQuantity();
        int idProduct = entity.getProduct().getIdProduct();
        int cartId = entity.getCartId();
        int cost = entity.getCost().getValue();
        Date date = Date.valueOf(entity.getDate());
        PreparedStatement statementFrom = null;
        PreparedStatement statementTo = null;
        String sqlTo = "insert order_product (productId, quantityOrder, costOrder, cartId, dateOrder) " +
                "VALUES (?,?,?,?,?)";
        String sqlFrom = "select quantity from storage_product where idstorage = ?";
        String sqlFromUpdate = "update storage_product set quantity = ? where idstorage = ?";
        String sqlGetId = "select last_insert_id()";

        try {
            connection.setAutoCommit(false);
            statementFrom = connection.prepareStatement(sqlFrom);
            statementFrom.setInt(1, idProduct);
            ResultSet resultSetFrom = statementFrom.executeQuery();
            while (resultSetFrom.next()) {
                quantityProducts = resultSetFrom.getInt(1);

                if (quantityProducts >= quantityOrder) {
                    resultProducts = quantityProducts - quantityOrder;
                } else {
                    throw new SQLException("Invalid quantity order. Your order quantity more than available");
                }
            }
            statementFrom.close();
            statementFrom = connection.prepareStatement(sqlFromUpdate);
            statementFrom.setInt(1, resultProducts);
            statementFrom.setInt(2, idProduct);
            statementFrom.executeUpdate();

            statementTo = connection.prepareStatement(sqlTo);
            statementTo.setInt(1, idProduct);
            statementTo.setInt(4, cartId);
            statementTo.setInt(2, quantityOrder);
            statementTo.setInt(3, cost);
            statementTo.setDate(5, date);
            statementTo.executeUpdate();
            statementTo.close();

            statementTo = connection.prepareStatement(sqlGetId);
            ResultSet resultSet = statementTo.executeQuery();
            while (resultSet.next()) {
                idOrder = resultSet.getInt(1);
            }


            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new DaoException("connection rollback is failed");
            }
            throw new DaoException("Transaction failed", e.getCause());
        } finally {
            try {
                statementFrom.close();
            } catch (SQLException e) {
                throw new DaoException("statementFrom close is failed");
            }
            try {
                statementTo.close();
            } catch (SQLException e) {
                throw new DaoException("statementTo close is failed");
            }
        }
        return idOrder;
    }
}
