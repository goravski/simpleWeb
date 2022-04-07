package by.gsu.epamlab.webshop.command;

import by.gsu.epamlab.webshop.controllers.ConstantJSP;
import by.gsu.epamlab.webshop.exceptions.CommandException;
import by.gsu.epamlab.webshop.model.Cart;
import by.gsu.epamlab.webshop.model.Order;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class OrderDeleteListCommand implements InterfaceCommand {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        final Logger LOGGER = LogManager.getLogger();
        int idOrder = Integer.parseInt(request.getParameter(CommandConstant.ID));
        HttpSession session = request.getSession();
        Cart cart = (Cart) session.getAttribute(CommandConstant.CART);
        List<Order> orderList = cart.getOrderList();
        Optional<Order> optionalOrder = orderList.stream().filter(order -> order.getId() == idOrder).findFirst();
        Order order;
        if (optionalOrder.isPresent()) {
            order = optionalOrder.get();
        } else {
            LOGGER.error(String.format("Cant get Order from list orders: %s", orderList));
            request.setAttribute(CommandConstant.ERROR, orderList);
            return ConstantJSP.ERROR_PAGE;
        }
        if (order.isValid()) {
            cart.getOrderList().remove(order);
            session.setAttribute(CommandConstant.CART, cart);
            LOGGER.info(String.format("Order %s add to list", order));
        } else {
            LOGGER.error(String.format("Order %s is not valid with person %s",
                    order, cart.getPersonId()));
            request.setAttribute(CommandConstant.ERROR, order);
            return ConstantJSP.ERROR_PAGE;
        }
        return ConstantJSP.CART_PAGE;
    }
}
