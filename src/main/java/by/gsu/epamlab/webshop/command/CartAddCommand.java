package by.gsu.epamlab.webshop.command;

import by.gsu.epamlab.webshop.controllers.ConstantJSP;
import by.gsu.epamlab.webshop.dao.CartDaoImpl;
import by.gsu.epamlab.webshop.exceptions.CommandException;
import by.gsu.epamlab.webshop.exceptions.DaoException;
import by.gsu.epamlab.webshop.model.Cart;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class CartAddCommand implements InterfaceCommand {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        final Logger LOGGER = LogManager.getLogger();
        CartDaoImpl cartDao = new CartDaoImpl();
        HttpSession session = request.getSession();
        Cart cart = (Cart) session.getAttribute(CommandConstant.CART);
        if (cart.isExist()) {
            return ConstantJSP.CART_PAGE;
        } else {
            try {
                int idCart = cartDao.add(cart);
                if (idCart != 0) {
                    cart = new Cart(idCart, cart);
                    session.setAttribute(CommandConstant.CART, cart);
                } else {
                    LOGGER.error(String.format("Didn't add cart %s", cart));
                }
            } catch (DaoException e) {
                LOGGER.error("Add cart not successful", e.getCause());
                throw new CommandException("Add cart is not successful", e.getCause());
            }
        }
        return ConstantJSP.CART_PAGE;

    }
}
