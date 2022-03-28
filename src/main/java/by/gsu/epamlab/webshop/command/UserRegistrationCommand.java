package by.gsu.epamlab.webshop.command;

import by.gsu.epamlab.webshop.controllers.ConstantJSP;

import by.gsu.epamlab.webshop.dao.PersonDaoImpl;
import by.gsu.epamlab.webshop.exceptions.CommandException;
import by.gsu.epamlab.webshop.exceptions.DaoException;
import by.gsu.epamlab.webshop.model.Person;
import jakarta.servlet.http.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class UserRegistrationCommand implements InterfaceCommand {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        PersonDaoImpl personDao = new PersonDaoImpl();
        final Logger LOGGER = LogManager.getLogger();
        String name = request.getParameter(CommandConstant.NAME);
        String login = request.getParameter(CommandConstant.LOGIN);
        String password = request.getParameter(CommandConstant.PASSWORD);
        Person person = new Person(name, login, password);
        request.setAttribute(CommandConstant.PERSON, person);
        if (!name.equals("") && !login.equals("") && !password.equals("")) {
            try {
                int idPerson = personDao.add(person);
                if (idPerson == 0) {
                    LOGGER.error("Person Add is not successful");
                    return ConstantJSP.REGISTRATION_PAGE;
                }
            } catch (DaoException e) {
                LOGGER.error("Add is not successful", e.getCause());
                throw new CommandException(e.getMessage(), e.getCause());
            }
            LOGGER.info("execute RegistrationCommand");
        } else {
            return ConstantJSP.REGISTRATION_PAGE;
        }
        return ConstantJSP.ADD_USER_SUCCESS;
    }
}
