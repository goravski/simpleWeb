package by.gsu.epamlab.webshop.command;

import by.gsu.epamlab.webshop.controllers.ConstantJSP;
import by.gsu.epamlab.webshop.exceptions.CommandException;
import by.gsu.epamlab.webshop.exceptions.DaoException;
import by.gsu.epamlab.webshop.model.Person;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


public class UpdateUserCommand implements InterfaceCommand {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        Boolean status;
        String stat = request.getParameter("status");
        int id = Integer.parseInt(request.getParameter("id").trim());
        String name = request.getParameter("name");
        String login = request.getParameter("login");
        String role = request.getParameter("role");
        if (stat.equals("unblock")) {
            status = true;
        } else {
            status = false;
        }

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
