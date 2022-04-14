package by.gsu.epamlab.webshop.filter;

import static by.gsu.epamlab.webshop.dao.Utility.*;

import by.gsu.epamlab.webshop.command.CommandConstant;
import by.gsu.epamlab.webshop.controllers.ConstantJSP;
import by.gsu.epamlab.webshop.model.Product;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter(urlPatterns = {"/update_user", "/registration", "/update_product", "/add_product", "/get_product"})
public class RequestEmptyFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession();
        if (!isValidRequest(request)) {
            switch (request.getParameter("command")) {
                case CommandConstant.ADD_PRODUCT:
                case CommandConstant.UPDATE_PRODUCT:
                case CommandConstant.GET_PRODUCT:
                    session.setAttribute(CommandConstant.PRODUCT, new Product());
                    request.setAttribute(CommandConstant.INFO, "Fields can't be empty ");
                    request.getRequestDispatcher(ConstantJSP.ADMIN_PAGE).forward(request, response);
                    break;
                case CommandConstant.REGISTRATION:
                    request.setAttribute(CommandConstant.INFO, "Fields of user can't be empty");
                    request.getRequestDispatcher(ConstantJSP.REGISTRATION_PAGE).forward(request, response);
                    break;
                case CommandConstant.UPDATE_USER:
                    request.setAttribute(CommandConstant.INFO, "Fields of user can't be empty");
                    request.getRequestDispatcher(ConstantJSP.ADMIN_PAGE).forward(request, response);
                    break;
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
