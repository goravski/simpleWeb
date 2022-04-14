package by.gsu.epamlab.webshop.command;

import by.gsu.epamlab.webshop.controllers.ConstantJSP;
import by.gsu.epamlab.webshop.page.AbstractPage;
import by.gsu.epamlab.webshop.page.ForwardPage;
import jakarta.servlet.http.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ErrorCommand implements InterfaceCommand {

    @Override
    public AbstractPage execute(HttpServletRequest request, HttpServletResponse response) {
        final Logger LOGGER = LogManager.getLogger();
        LOGGER.info("executed ErrorCommand Error page directed");
        return new ForwardPage(ConstantJSP.ERROR_PAGE);
    }
}
