package by.gsu.epamlab.webshop.command;

import by.gsu.epamlab.webshop.controllers.ConstantJSP;
import by.gsu.epamlab.webshop.dao.PersonDaoImpl;
import by.gsu.epamlab.webshop.exceptions.CommandException;
import by.gsu.epamlab.webshop.exceptions.DaoException;
import by.gsu.epamlab.webshop.model.Person;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class UserUpdateCommand implements InterfaceCommand {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        PersonDaoImpl personDao = new PersonDaoImpl();
        final Logger LOGGER = LogManager.getLogger();
        String status = request.getParameter(CommandConstant.STATUS);
        int id = Integer.parseInt(request.getParameter(CommandConstant.ID).trim());
        String name = request.getParameter(CommandConstant.NAME);
        String login = request.getParameter(CommandConstant.LOGIN);
        String role = request.getParameter(CommandConstant.ROLE);
        Person person = new Person(id, name, login, role, status);
        try {
            personDao.update(person);

        } catch (DaoException e) {
            LOGGER.error("Can't get object from database", e.getCause());
            throw new CommandException(e.getMessage(), e.getCause());
        }
        return ConstantJSP.ADMIN_PAGE;
    }
}
