package by.gsu.epamlab.webshop.servlets;

import by.gsu.epamlab.webshop.command.CommandFactory;
import by.gsu.epamlab.webshop.command.InterfaceCommand;
import by.gsu.epamlab.webshop.exceptions.CommandException;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class AbstractFrontController extends HttpServlet {

    public void init() throws ServletException {
    }

    private static final Logger log = Logger.getLogger(AbstractFrontController.class);

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        InterfaceCommand command;
        Optional optionalCommand = CommandFactory.getCommandFromFactory(request);
        if (optionalCommand.isPresent()) {
            command = (InterfaceCommand) optionalCommand.get();
            String page = null;
            try {
                page = command.execute(request, response);
            } catch (CommandException e) {
                log.error("command.execut(req, resp) is failed", e.getCause());
                throw new ServletException(e.getMessage(), e.getCause());
            }
            if (page.equals(ConstantJSP.REGISTRATION_PAGE)) {
                response.sendRedirect(request.getContextPath() + page);
                log.info("processRequest sendRedirect to " + page);
            } else {
                getServletContext().getRequestDispatcher(page).forward(request, response);
                log.info("processRequest forward to " + page);
            }
        } else {
            processError(request, response, "command from factory isEmpty");
        }

    }

    protected void processError(HttpServletRequest request, HttpServletResponse response, String msg)
            throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, msg);
        log.info("processError send response Error 400");
    }


    public void destroy() {
    }
}
