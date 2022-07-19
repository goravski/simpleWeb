package by.gsu.epamlab.webshop.command;

import by.gsu.epamlab.webshop.connection.ConnectionManager;
import by.gsu.epamlab.webshop.controllers.ConstantJSP;

import by.gsu.epamlab.webshop.dao.PersonDaoImpl;
import by.gsu.epamlab.webshop.dao.Utility;
import by.gsu.epamlab.webshop.exceptions.CommandException;
import by.gsu.epamlab.webshop.exceptions.DaoException;
import by.gsu.epamlab.webshop.model.Person;
import by.gsu.epamlab.webshop.page.AbstractPage;
import by.gsu.epamlab.webshop.page.ForwardPage;
import jakarta.servlet.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class UserRegistrationCommand implements InterfaceCommand {

    @Override
    public AbstractPage execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        ConnectionManager connectionManager = new ConnectionManager();
        PersonDaoImpl personDao = new PersonDaoImpl(connectionManager);
        final Logger LOGGER = LoggerFactory.getLogger(UserRegistrationCommand.class);
        Person person = Utility.createPersonFromRequest(request);
        request.setAttribute(CommandConstant.PERSON, person);
        try {
            int idPerson = personDao.add(person);
            if (idPerson == 0) {
                LOGGER.error("Person Add is not successful");
                return new ForwardPage(ConstantJSP.REGISTRATION_PAGE);
            }
            return new ForwardPage(ConstantJSP.ADD_USER_SUCCESS);
        } catch (DaoException e) {
            LOGGER.error("Add is not successful", e.getCause());
            throw new CommandException(e.getMessage(), e.getCause());
        }
    }
}
