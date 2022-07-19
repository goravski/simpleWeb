package by.gsu.epamlab.webshop.command;

import by.gsu.epamlab.webshop.connection.ConnectionManager;
import by.gsu.epamlab.webshop.controllers.ConstantJSP;
import by.gsu.epamlab.webshop.dao.OrderDaoImpl;
import by.gsu.epamlab.webshop.exceptions.CommandException;
import by.gsu.epamlab.webshop.exceptions.DaoException;
import by.gsu.epamlab.webshop.model.Order;
import by.gsu.epamlab.webshop.page.AbstractPage;
import by.gsu.epamlab.webshop.page.RedirectPage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


public class OrderAddCommand implements InterfaceCommand {
    @Override
    public AbstractPage execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        ConnectionManager connectionManager = new ConnectionManager();
        final Logger LOGGER = LoggerFactory.getLogger(OrderAddCommand.class);
        OrderDaoImpl orderDao = new OrderDaoImpl(connectionManager);
        List<Order> newList = (List<Order>) request.getAttribute(CommandConstant.ORDER_LIST);
        try {
            orderDao.add(newList);
            return new RedirectPage(ConstantJSP.PAYMENT_PAGE);
        } catch (DaoException e) {
            LOGGER.error(String.format("Add order %s not successful", newList), e.getCause());
            throw new CommandException(String.format("Add order %s not successful", newList), e.getCause());
        }
    }
}
