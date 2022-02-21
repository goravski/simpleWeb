package by.gsu.epamlab.webshop.command;

import by.gsu.epamlab.webshop.servlets.ConstantJSP;

import jakarta.servlet.http.*;

public class RegistrationCommand implements InterfaceCommand {
    private HttpServletRequest request;

    public HttpServletRequest getRequest() {
        return request;
    }

    public RegistrationCommand() {
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        log.info("execute RegistrationCommand, Registration page directed");
        return ConstantJSP.REGISTRATION_PAGE;
    }
}
