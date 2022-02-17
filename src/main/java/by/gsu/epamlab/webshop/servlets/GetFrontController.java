package by.gsu.epamlab.webshop.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet( urlPatterns = {"/admin_menu", "/user_menu", "/error404"}) // записать все command {"/cmd1", "/cmd2", "/cmd3"}
public class GetFrontController extends AbstractFrontController {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processError(request, response, "method post is wrong for command: " +
                request.getPathInfo());
    }
}
