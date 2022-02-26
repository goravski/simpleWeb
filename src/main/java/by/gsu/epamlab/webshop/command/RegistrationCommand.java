package by.gsu.epamlab.webshop.command;

import by.gsu.epamlab.webshop.controllers.ConstantJSP;

import by.gsu.epamlab.webshop.exceptions.CommandException;
import by.gsu.epamlab.webshop.exceptions.DaoException;
import by.gsu.epamlab.webshop.model.Person;
import jakarta.servlet.http.*;



public class RegistrationCommand implements InterfaceCommand {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        String name = request.getParameter("firstName");
        String login =  request.getParameter("login");
        String password = request.getParameter("password");
        Person person = new Person(name, login, password);
        try {
            boolean isAdd = personDao.add(person);
            if (!isAdd){
                person = new Person();
                return ConstantJSP.REGISTRATION_PAGE;

            }
        } catch (DaoException e) {
            LOGGER.error("Add is not successful", e.getCause());
            throw new CommandException(e.getMessage(), e.getCause());
        }
        person = new Person();
        LOGGER.info("execute RegistrationCommand, Success page directed");
        return ConstantJSP.REGISTRATION_SUCCESS;
    }
}
