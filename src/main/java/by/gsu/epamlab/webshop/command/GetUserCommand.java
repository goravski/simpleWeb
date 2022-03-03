package by.gsu.epamlab.webshop.command;

import by.gsu.epamlab.webshop.controllers.ConstantJSP;
import by.gsu.epamlab.webshop.dao.PersonDaoImpl;
import by.gsu.epamlab.webshop.exceptions.CommandException;
import by.gsu.epamlab.webshop.exceptions.DaoException;
import by.gsu.epamlab.webshop.model.Person;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;

public class GetUserCommand implements InterfaceCommand {
    PersonDaoImpl personDao = new PersonDaoImpl();
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        String login =  request.getParameter("login");
        try {
            Optional <Person> optionalPerson = personDao.getByLogin(login);
            if (optionalPerson.isPresent()){
                request.setAttribute("person", optionalPerson.get());
            } else {
                request.setAttribute("person", new Person());
            }
        } catch (DaoException e) {
            LOGGER.error("Can't get object from database", e.getCause());
            throw new CommandException(e.getMessage(), e.getCause());
        }
        return ConstantJSP.ADMIN_PAGE;
    }
}
