package by.gsu.epamlab.webshop.command;

import by.gsu.epamlab.webshop.controllers.ConstantJSP;
import by.gsu.epamlab.webshop.exceptions.CommandException;
import by.gsu.epamlab.webshop.model.Cart;
import by.gsu.epamlab.webshop.model.Order;
import by.gsu.epamlab.webshop.page.AbstractPage;
import by.gsu.epamlab.webshop.page.ForwardPage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class OrderDeleteListCommand implements InterfaceCommand {
    @Override
    public AbstractPage execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        final Logger LOGGER = LoggerFactory.getLogger(OrderDeleteListCommand.class);
        HttpSession session = request.getSession();
        Cart cart = (Cart) session.getAttribute(CommandConstant.CART);
        Order order = (Order) request.getAttribute(CommandConstant.ORDER);
            cart.getOrderList().remove(order);
            session.setAttribute(CommandConstant.CART, cart);
            LOGGER.info(String.format("Order %s add to list", order));
            return new ForwardPage(ConstantJSP.CART_PAGE);
    }
}
