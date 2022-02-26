package by.gsu.epamlab.webshop.command;

import by.gsu.epamlab.webshop.dao.PersonDaoImpl;
import by.gsu.epamlab.webshop.exceptions.CommandException;
import by.gsu.epamlab.webshop.exceptions.DaoException;
import by.gsu.epamlab.webshop.controllers.ConstantJSP;
import by.gsu.epamlab.webshop.exceptions.ServiceException;
import by.gsu.epamlab.webshop.model.Person;
import jakarta.servlet.http.*;

import java.util.Optional;


public class LoginCommand implements InterfaceCommand {
    private PersonDaoImpl personDao = PersonDaoImpl.getDaoInstance();
    private static final String PARAM_NAME_LOGIN = "login";
    private static final String PARAM_NAME_PASSWORD = "password";

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        String page = null;
        String login = request.getParameter(PARAM_NAME_LOGIN);
        String pass = request.getParameter(PARAM_NAME_PASSWORD);
        HttpSession session = request.getSession();
        try {
            Optional <Person> person = personDao.getByLogin(login);

            if (person.isPresent() & services.checkPassword(person, pass)) {

                session.setAttribute("person", person.get());
                if (person.get().getRole().equals("admin")) {
                    page = ConstantJSP.ADMIN_PAGE;
                    LOGGER.info ("page ADMIN executed");
                }
                if (person.get().getRole().equals("user")) {
                    page = ConstantJSP.USER_PAGE;
                    LOGGER.info ("page User executed");
                }
                person = Optional.empty();
            } else {
                page = ConstantJSP.INDEX_PAGE;
                LOGGER.info ("Login or password is not correct");
            }
        } catch (DaoException e){
            LOGGER.error("Can't get object from database", e.getCause());
            throw  new CommandException(e.getMessage(), e.getCause());
        } catch (ServiceException e) {
            LOGGER.error("Check password error", e.getCause());
        }
        return page;
    }
}
