package by.gsu.epamlab.webshop.command;

import by.gsu.epamlab.webshop.servlets.ConstantJSP;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RegistrationCommand implements InterfaceCommand {
    public RegistrationCommand() {
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        return ConstantJSP.REGISTRATION_PAGE;
    }
}
