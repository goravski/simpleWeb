package by.gsu.epamlab.webshop.command;

import by.gsu.epamlab.webshop.connection.ConnectionManager;
import by.gsu.epamlab.webshop.dao.PersonDaoImpl;
import by.gsu.epamlab.webshop.dao.Utility;
import by.gsu.epamlab.webshop.exceptions.CommandException;
import by.gsu.epamlab.webshop.exceptions.DaoException;
import by.gsu.epamlab.webshop.controllers.ConstantJSP;
import by.gsu.epamlab.webshop.exceptions.ValidationException;
import by.gsu.epamlab.webshop.model.Cart;
import by.gsu.epamlab.webshop.model.Person;
import by.gsu.epamlab.webshop.page.AbstractPage;
import by.gsu.epamlab.webshop.page.ForwardPage;
import jakarta.servlet.http.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;


public class LoginCommand implements InterfaceCommand {

    @Override
    public AbstractPage execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        ConnectionManager connectionManager = new ConnectionManager();
        final Logger LOGGER = LogManager.getLogger();
        PersonDaoImpl personDao = new PersonDaoImpl(connectionManager);
        AbstractPage page = null;
        String login = request.getParameter(CommandConstant.LOGIN);
        String pass = request.getParameter(CommandConstant.PASSWORD);
        HttpSession session = request.getSession();
        try {
            Optional<Person> person = personDao.getByLogin(login);

            if (person.isPresent() & Utility.checkPassword(person, pass)) {
                session.setAttribute("person", person.get());
            } else {
                page = new ForwardPage( ConstantJSP.INDEX_PAGE);
                LOGGER.info("Login or password is not correct");
                return page;
            }
            if (person.get().getRole().equals(CommandConstant.ADMIN_ROLE)) {
                page = new ForwardPage( ConstantJSP.ADMIN_PAGE);
                LOGGER.info("page ADMIN executed");
            } else if (person.get().getRole().equals(CommandConstant.USER_ROLE)) {
                LOGGER.info("page User executed");
                session.setAttribute(CommandConstant.CART, new Cart(person.get().getId()));
                page = new ForwardPage( ConstantJSP.GET_CART);
            } else {
                person = Optional.empty();
            }
        } catch (
                DaoException e) {
            LOGGER.error("Can't get object from database", e.getCause());
            throw new CommandException(e.getMessage(), e.getCause());
        } catch (
                ValidationException e) {
            LOGGER.error("Check password error", e.getCause());
        }
        return page;
    }
}
