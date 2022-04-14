package by.gsu.epamlab.webshop.command;

import by.gsu.epamlab.webshop.controllers.ConstantJSP;
import by.gsu.epamlab.webshop.page.AbstractPage;
import by.gsu.epamlab.webshop.page.ForwardPage;
import jakarta.servlet.http.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UserLogoutCommand implements InterfaceCommand {
    @Override
    public AbstractPage execute(HttpServletRequest request, HttpServletResponse response) {
        final Logger LOGGER = LogManager.getLogger();
        HttpSession session = request.getSession(false);
        if (session != null)
            session.invalidate();
        LOGGER.info("execute LogoutCommand, session invalidated, Login page directed");
        return new ForwardPage(ConstantJSP.INDEX_PAGE);
    }
}
