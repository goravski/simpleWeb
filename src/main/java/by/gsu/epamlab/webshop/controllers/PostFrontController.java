package by.gsu.epamlab.webshop.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet(urlPatterns = {"/login", "/registration", "/logout", "/update_user", "/update_product", "/add_product",
        "/buy_product", "/storage", "/product", "/cart_buy" , "/order"})
public class PostFrontController extends AbstractFrontController{

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processError(request, response, "method get is wrong for command: " +
                request.getPathInfo());
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}
