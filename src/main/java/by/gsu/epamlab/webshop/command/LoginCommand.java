package by.gsu.epamlab.webshop.command;

import by.gsu.epamlab.webshop.dao.Utils;
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
        if (Utils.checkLogin(login, pass)) {
            if (login.equals("admin")) {
                request.setAttribute("role", login);
                page = ConstantJSP.ADMIN_PAGE;
            } else {
                request.setAttribute("role", "user");
                page = ConstantJSP.USER_PAGE;
            }
        } else {
            request.setAttribute("errorLoginPassMessage", ConstantJSP.MESSAGE_LOGIN_ERROR);
            page = ConstantJSP.ERROR_PAGE;
        }
        return page;
    }
}
