package by.gsu.epamlab.webshop.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet( urlPatterns = {"/admin_menu", "/user_menu", "/error404", "/get_user", "/get_product", "/get_storage",
        "/get_product_page"})
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
