package by.gsu.epamlab.webshop.command;

import by.gsu.epamlab.webshop.controllers.ConstantJSP;
import by.gsu.epamlab.webshop.page.AbstractPage;
import by.gsu.epamlab.webshop.page.ForwardPage;
import jakarta.servlet.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserLogoutCommand implements InterfaceCommand {
    @Override
    public AbstractPage execute(HttpServletRequest request, HttpServletResponse response) {
        final Logger LOGGER = LoggerFactory.getLogger(UserLogoutCommand.class);
        HttpSession session = request.getSession(false);
        if (session != null)
            session.invalidate();
        LOGGER.info("execute LogoutCommand, session invalidated, Login page directed");
        return new ForwardPage(ConstantJSP.INDEX_PAGE);
    }
}
