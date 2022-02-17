package by.gsu.epamlab.webshop.command;

import by.gsu.epamlab.webshop.dao.PersonDaoImpl;
import by.gsu.epamlab.webshop.exceptions.AuthorizationException;
import by.gsu.epamlab.webshop.exceptions.DaoException;
import by.gsu.epamlab.webshop.model.Person;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

public class AuthorizationCommand implements InterfaceCommand {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws AuthorizationException {
        PersonDaoImpl personDao = null;
        try {
            personDao = new PersonDaoImpl();
        } catch (DaoException e) {
            e.printStackTrace();
        }

        String login = request.getParameter("login");

        Optional <Person> personOptional = null;
        try {
            personOptional = personDao.getByLogin(login);
        } catch (DaoException e) {
            throw new AuthorizationException(e.getMessage(), e.getCause());
        }
        if (personOptional.isPresent()){

            return null;
        }else {
            return null;

        }

    }
}
