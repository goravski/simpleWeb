package by.gsu.epamlab.webshop.servlets;

import by.gsu.epamlab.webshop.command.CommandFactory;
import by.gsu.epamlab.webshop.command.InterfaceCommand;
import by.gsu.epamlab.webshop.exceptions.AuthorizationException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class AbstractFrontController extends HttpServlet {

    public void init() throws ServletException {
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        InterfaceCommand command;
        Optional optionalCommand = CommandFactory.getCommandFromFactory(request);
        if (optionalCommand.isPresent()) {
            command = (InterfaceCommand) optionalCommand.get();
            String page = null;
            try {
                page = command.execute(request, response);
            } catch (AuthorizationException e) {
                throw new ServletException(e.getMessage(), e.getCause());
            }
            if (page.equals(ConstantJSP.REGISTRATION_PAGE)) {
                response.sendRedirect(request.getContextPath() + page);
            } else {
                getServletContext().getRequestDispatcher(page).forward(request, response);
            }
        } else {
            processError(request, response, "command from factory isEmpty");
        }

    }

    protected void processError(HttpServletRequest request, HttpServletResponse response, String msg)
            throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, msg);
    }


    public void destroy() {
    }
}
