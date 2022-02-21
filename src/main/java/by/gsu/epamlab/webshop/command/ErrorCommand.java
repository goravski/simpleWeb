package by.gsu.epamlab.webshop.command;

import by.gsu.epamlab.webshop.servlets.ConstantJSP;
import jakarta.servlet.http.*;

public class ErrorCommand implements InterfaceCommand {
    public ErrorCommand() {
    }


    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        log.info("executed ErrorCommand Error page directed");
        return ConstantJSP.ERROR_PAGE;
    }
}
