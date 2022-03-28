package by.gsu.epamlab.webshop.command;

import by.gsu.epamlab.webshop.controllers.ConstantJSP;
import by.gsu.epamlab.webshop.dao.CartDaoImpl;
import by.gsu.epamlab.webshop.exceptions.CommandException;
import by.gsu.epamlab.webshop.exceptions.DaoException;
import by.gsu.epamlab.webshop.model.Cart;
import by.gsu.epamlab.webshop.model.Person;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class CartGetCommand implements InterfaceCommand {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        final Logger LOGGER = LogManager.getLogger();
        CartDaoImpl cartDao = new CartDaoImpl();
        HttpSession session = request.getSession();
        Person person = (Person) session.getAttribute(CommandConstant.PERSON);
        Cart cart = (Cart) session.getAttribute(CommandConstant.CART);
        if (cart.isExist()) {
            return ConstantJSP.USER_PAGE;
        } else {
            int idPerson;
            if (person.isValid()) {
                idPerson = person.getId();
            } else {
                request.setAttribute(CommandConstant.ERROR, "Invalid object person in session scope");
                return ConstantJSP.ERROR_PAGE;
            }
            try {
                Optional<Cart> opCart = cartDao.getById(idPerson);
                if (opCart.isPresent()) {
                    cart = opCart.get();
                    session.setAttribute(CommandConstant.CART, cart);
                }
            } catch (DaoException e) {
                LOGGER.error("Get cart not successful", e.getCause());
                throw new CommandException("Get cart is not successful", e.getCause());
            }
        }
        return ConstantJSP.USER_PAGE;
    }
}
