package by.gsu.epamlab.webshop.command;

import by.gsu.epamlab.webshop.servlets.ConstantJSP;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LogoutCommand implements InterfaceCommand{
    public LogoutCommand() {
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String page = ConstantJSP.LOGIN_PAGE;
        request.getSession().invalidate();
        log.info("execute LogoutCommand, session invalidated, Login page directed");
        return page;
    }
}
