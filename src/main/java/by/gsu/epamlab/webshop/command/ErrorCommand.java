package by.gsu.epamlab.webshop.command;

import by.gsu.epamlab.webshop.servlets.ConstantJSP;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ErrorCommand implements InterfaceCommand {
    public ErrorCommand() {
    }


    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {

        return ConstantJSP.ERROR_PAGE;
    }
}
