package by.gsu.epamlab.webshop.controllers;

import by.gsu.epamlab.webshop.command.CommandFactory;
import by.gsu.epamlab.webshop.command.InterfaceCommand;
import by.gsu.epamlab.webshop.exceptions.CommandException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.servlet.*;
import jakarta.servlet.http.*;

import java.io.IOException;


public abstract class AbstractFrontController extends HttpServlet {


    void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final Logger LOGGER = LogManager.getLogger();
        try {
            InterfaceCommand command = CommandFactory.getCommandFromFactory(request);
            String pageName = command.execute(request, response);
            if (pageName.equals(ConstantJSP.REGISTRATION_PAGE)) {
                response.sendRedirect(request.getContextPath() + pageName);
                LOGGER.trace("processRequest sendRedirect to " + pageName);
            } else {
                request.getRequestDispatcher(pageName).forward(request, response);
                LOGGER.trace("processRequest forward to " + pageName);
            }
        } catch (CommandException e) {
            LOGGER.error("command.execute(req, resp) is failed", e.getCause());
            throw new ServletException(e.getMessage(), e.getCause());
        }


    }

    void processError(HttpServletRequest request, HttpServletResponse response, String msg)
            throws ServletException, IOException {
        final Logger LOGGER = LogManager.getLogger();
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, msg);
        LOGGER.info("processError send response Error 400");
    }
}
