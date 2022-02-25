package by.gsu.epamlab.webshop.command;

import by.gsu.epamlab.webshop.controllers.ConstantJSP;
import jakarta.servlet.http.*;

public class ErrorCommand implements InterfaceCommand {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        log.info("executed ErrorCommand Error page directed");
        return ConstantJSP.ERROR_PAGE;
    }
}
