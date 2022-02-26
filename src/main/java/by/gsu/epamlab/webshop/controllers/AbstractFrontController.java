package by.gsu.epamlab.webshop.controllers;

import by.gsu.epamlab.webshop.command.CommandFactory;
import by.gsu.epamlab.webshop.command.InterfaceCommand;
import by.gsu.epamlab.webshop.exceptions.CommandException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.servlet.*;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.Optional;

public interface AbstractFrontController {

    Logger log = LogManager.getLogger();

    default void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        InterfaceCommand command;
        Optional optionalCommand = CommandFactory.getCommandFromFactory(request);
        String pageName;
        if (optionalCommand.isPresent()) {
            command = (InterfaceCommand) optionalCommand.get();
            try {
                pageName = command.execute(request, response);
                if (pageName.equals(ConstantJSP.REGISTRATION_PAGE)) {
                    response.sendRedirect(request.getContextPath() + pageName);
                    log.info("processRequest sendRedirect to " + pageName);
                } else {
                    request.getRequestDispatcher(pageName).forward(request, response);
                    log.info("processRequest forward to " + pageName);
                }
            } catch (CommandException e) {
                log.error("command.execute(req, resp) is failed", e.getCause());
                throw new ServletException(e.getMessage(), e.getCause());
            }

        } else {
            processError(request, response, "command from factory isEmpty");
        }

    }

    default void processError(HttpServletRequest request, HttpServletResponse response, String msg)
            throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, msg);
        log.info("processError send response Error 400");
    }
}
