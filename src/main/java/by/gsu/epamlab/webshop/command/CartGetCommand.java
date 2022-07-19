package by.gsu.epamlab.webshop.command;

import by.gsu.epamlab.webshop.connection.ConnectionManager;
import by.gsu.epamlab.webshop.controllers.ConstantJSP;
import by.gsu.epamlab.webshop.dao.CartDaoImpl;
import by.gsu.epamlab.webshop.exceptions.CommandException;
import by.gsu.epamlab.webshop.exceptions.DaoException;
import by.gsu.epamlab.webshop.model.Cart;
import by.gsu.epamlab.webshop.model.Person;
import by.gsu.epamlab.webshop.page.AbstractPage;
import by.gsu.epamlab.webshop.page.ForwardPage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class CartGetCommand implements InterfaceCommand {
    @Override
    public AbstractPage execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        ConnectionManager connectionManager = new ConnectionManager();
        final Logger LOGGER = LoggerFactory.getLogger(CartGetCommand.class);
        CartDaoImpl cartDao = new CartDaoImpl(connectionManager);
        HttpSession session = request.getSession();
        Person person = (Person) session.getAttribute(CommandConstant.PERSON);
        Cart cart ;
        int idPerson;
        if (person.isValid()) {
            idPerson = person.getId();
        } else {
            request.setAttribute(CommandConstant.ERROR, "Invalid object person in session scope");
            return new ForwardPage(ConstantJSP.ERROR_PAGE);
        }
        try {
            Optional<Cart> opCart = cartDao.getById(idPerson);
            if (opCart.isPresent()) {
                cart = opCart.get();
                session.setAttribute(CommandConstant.CART, cart);
            }
            return new ForwardPage(ConstantJSP.USER_PAGE);
        } catch (DaoException e) {
            LOGGER.error("Get cart not successful", e.getCause());
            throw new CommandException("Get cart is not successful", e.getCause());
        }
    }
}
