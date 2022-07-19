package by.gsu.epamlab.webshop.command;

import by.gsu.epamlab.webshop.connection.ConnectionManager;
import by.gsu.epamlab.webshop.controllers.ConstantJSP;
import by.gsu.epamlab.webshop.dao.CartDaoImpl;
import by.gsu.epamlab.webshop.exceptions.CommandException;
import by.gsu.epamlab.webshop.exceptions.DaoException;
import by.gsu.epamlab.webshop.model.Cart;
import by.gsu.epamlab.webshop.page.AbstractPage;
import by.gsu.epamlab.webshop.page.ForwardPage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CartAddCommand implements InterfaceCommand {

    @Override
    public AbstractPage execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        ConnectionManager connectionManager = new ConnectionManager();
        final Logger LOGGER = LoggerFactory.getLogger(CartAddCommand.class);
        CartDaoImpl cartDao = new CartDaoImpl(connectionManager);
        HttpSession session = request.getSession();
        Cart cart = (Cart) session.getAttribute(CommandConstant.CART);
        try {
            int idCart = cartDao.add(cart);
            cart = new Cart(idCart, cart);
            session.setAttribute(CommandConstant.CART, cart);
            return new ForwardPage(ConstantJSP.CART_PAGE);
        } catch (DaoException e) {
            LOGGER.error("Add cart not successful", e.getCause());
            throw new CommandException("Add cart is not successful", e.getCause());
        }
    }
}
