package by.gsu.epamlab.webshop.command;

import by.gsu.epamlab.webshop.controllers.ConstantJSP;
import by.gsu.epamlab.webshop.dao.PersonDaoImpl;
import by.gsu.epamlab.webshop.exceptions.CommandException;
import by.gsu.epamlab.webshop.exceptions.DaoException;
import by.gsu.epamlab.webshop.model.Person;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


public class UpdateUserCommand implements InterfaceCommand {
    PersonDaoImpl personDao = new PersonDaoImpl();
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {

        String status = request.getParameter("status");
        int id = Integer.parseInt(request.getParameter("id").trim());
        String name = request.getParameter("name");
        String login = request.getParameter("login");
        String role = request.getParameter("role");
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
