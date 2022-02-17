package by.gsu.epamlab.webshop.command;

import by.gsu.epamlab.webshop.dao.PersonDaoImpl;
import by.gsu.epamlab.webshop.dao.Utils;
import by.gsu.epamlab.webshop.exceptions.DaoException;
import by.gsu.epamlab.webshop.servlets.ConstantJSP;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class LoginCommand implements InterfaceCommand {
    public LoginCommand() {
    }

    private static final String PARAM_NAME_LOGIN = "login";
    private static final String PARAM_NAME_PASSWORD = "password";

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String page = null;
        String login = request.getParameter(PARAM_NAME_LOGIN);
        String pass = request.getParameter(PARAM_NAME_PASSWORD);
        PersonDaoImpl personDao = null;
        try {
            personDao = new PersonDaoImpl();
            if (personDao.chekPassword(login, pass)) {
                if (login.equals("admin")) {
                    request.setAttribute("role", login);
                    page = ConstantJSP.ADMIN_PAGE;
                    log.info("page ADMIN executed");
                } else {
                    request.setAttribute("role", "user");
                    page = ConstantJSP.USER_PAGE;
                    log.info("page User executed");
                }
            } else {
                request.setAttribute("errorLoginPassMessage", ConstantJSP.MESSAGE_LOGIN_ERROR);
                page = ConstantJSP.ERROR_PAGE;
                log.info("page Error executed");
            }
        } catch (DaoException e) {
            log.error("Didn,t create PersonDaoImpl object", e.getCause());
            e.printStackTrace();
        }
        return page;
    }
}
