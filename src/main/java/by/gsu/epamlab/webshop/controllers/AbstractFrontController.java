package by.gsu.epamlab.webshop.controllers;

import by.gsu.epamlab.webshop.command.CommandFactory;
import by.gsu.epamlab.webshop.command.InterfaceCommand;
import by.gsu.epamlab.webshop.dao.OrderDaoImpl;
import by.gsu.epamlab.webshop.exceptions.CommandException;
import by.gsu.epamlab.webshop.page.AbstractPage;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


public abstract class AbstractFrontController extends HttpServlet {


    void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final Logger LOGGER = LoggerFactory.getLogger(AbstractFrontController.class);
        try {
            InterfaceCommand command = CommandFactory.getCommandFromFactory(request);
            AbstractPage abstractPage = command.execute(request, response);
            abstractPage.sendRequest(request, response);
        } catch (CommandException e) {
            LOGGER.error("command.execute(req, resp) is failed", e.getCause());
            throw new ServletException(e.getMessage(), e.getCause());
        }
    }

    void processError(HttpServletRequest request, HttpServletResponse response, String msg)
            throws ServletException, IOException {
        final Logger LOGGER = LoggerFactory.getLogger(AbstractFrontController.class);
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, msg);
        LOGGER.info("processError send response Error 400");
    }
}
