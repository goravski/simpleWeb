package by.gsu.epamlab.webshop.command;

import by.gsu.epamlab.webshop.controllers.ConstantJSP;

import by.gsu.epamlab.webshop.dao.PersonDaoImpl;
import by.gsu.epamlab.webshop.exceptions.CommandException;
import by.gsu.epamlab.webshop.exceptions.DaoException;
import by.gsu.epamlab.webshop.model.Person;
import jakarta.servlet.http.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class RegistrationCommand implements InterfaceCommand {

    private PersonDaoImpl personDao = PersonDaoImpl.getDaoInstance();
    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        String name = request.getParameter("firstName");
        String login =  request.getParameter("login");
        String password = request.getParameter("password");
        Person person = new Person(name, login, password);
        try {
            boolean isAdd = personDao.add(person);
            if (!isAdd){
                return ConstantJSP.REGISTRATION_PAGE;
            }
        } catch (DaoException e) {
            LOGGER.error("Add is not successful", e.getCause());
            throw new CommandException(e.getMessage(), e.getCause());
        }
        log.info("execute RegistrationCommand, Success page directed");
        return ConstantJSP.REGISTRATION_SUCCESS;
    }
}
