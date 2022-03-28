package by.gsu.epamlab.webshop.command;

import by.gsu.epamlab.webshop.controllers.ConstantJSP;
import by.gsu.epamlab.webshop.dao.OrderDaoImpl;
import by.gsu.epamlab.webshop.exceptions.CommandException;
import by.gsu.epamlab.webshop.exceptions.DaoException;
import by.gsu.epamlab.webshop.model.Cart;
import by.gsu.epamlab.webshop.model.Order;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.stream.Collectors;

public class OrderAddCommand implements InterfaceCommand {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        final Logger LOGGER = LogManager.getLogger();
        OrderDaoImpl orderDao = new OrderDaoImpl();
        HttpSession session = request.getSession();
        Cart cart = (Cart) session.getAttribute(CommandConstant.CART);
        int cartId = cart.getId();
        List<Order> orderList = cart.getOrderList();
        List<Order> newlist = orderList.stream().map(s -> new Order(cartId, s)).collect(Collectors.toList());
        for (Order order : newlist) {
            try {
                if (order.isValid()) {
                    orderDao.add(order);
                } else {
                    LOGGER.error(String.format("Order %s is not valid with product or person %s",
                            order, cart.getPersonId()));
                    request.setAttribute(CommandConstant.ERROR, order);
                    return ConstantJSP.ERROR_PAGE;
                }
            } catch (DaoException e) {
                LOGGER.error(String.format("Add order %s not successful", order), e.getCause());
                throw new CommandException(String.format("Add order %s not successful", order), e.getCause());
            }
        }
        return ConstantJSP.PAYMENT_PAGE;

    }
}
