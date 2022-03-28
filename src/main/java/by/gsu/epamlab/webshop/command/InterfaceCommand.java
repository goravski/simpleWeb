package by.gsu.epamlab.webshop.command;


import by.gsu.epamlab.webshop.exceptions.CommandException;

import jakarta.servlet.http.*;

import java.util.Enumeration;


public interface InterfaceCommand {
    String execute(HttpServletRequest request, HttpServletResponse response) throws CommandException;

    default boolean isValidRequest(HttpServletRequest request) {
        boolean valid = false;
        Enumeration <String> paramName = request.getParameterNames();
        while (paramName.hasMoreElements()) {
            String param = paramName.nextElement();
            if (request.getParameter(param).trim().isEmpty()){
                return false;
            }
            valid = true;
        }
        return valid;
    }

}
