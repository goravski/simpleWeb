package by.gsu.epamlab.webshop.command;

import by.gsu.epamlab.webshop.controllers.ConstantJSP;
import jakarta.servlet.http.*;

public class UserLogoutCommand implements InterfaceCommand{


    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {

        String page = ConstantJSP.INDEX_PAGE;
        HttpSession session = request.getSession(false);
        if(session != null)
            session.invalidate();
        LOGGER.info("execute LogoutCommand, session invalidated, Login page directed");
        return page;
    }
}
