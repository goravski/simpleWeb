package by.gsu.epamlab.webshop.command;

import by.gsu.epamlab.webshop.controllers.ConstantJSP;

import by.gsu.epamlab.webshop.dao.PersonDaoImpl;
import jakarta.servlet.http.*;

public class LogoutCommand implements InterfaceCommand{


    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {

        String page = ConstantJSP.LOGIN_PAGE;
        request.getSession().invalidate();
        log.info("execute LogoutCommand, session invalidated, Login page directed");
        return page;
    }
}
