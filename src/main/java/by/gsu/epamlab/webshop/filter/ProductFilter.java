package by.gsu.epamlab.webshop.filter;

import by.gsu.epamlab.webshop.command.CommandConstant;
import by.gsu.epamlab.webshop.controllers.ConstantJSP;
import by.gsu.epamlab.webshop.dao.Utility;
import by.gsu.epamlab.webshop.model.Product;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;


@WebFilter(urlPatterns = {"/add_product"})
public class ProductFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        Product product = Utility.createProductFromRequest(request);
        int quantity = Integer.parseInt( request.getParameter(CommandConstant.QUANTITY));
        HttpSession session = request.getSession();
        if (!product.isValid() || quantity < 0) {
            session.setAttribute(CommandConstant.PRODUCT, new Product());
            request.setAttribute(CommandConstant.INFO,
                    String.format("Invalid object %s or quantity %s", product, quantity));
            request.getRequestDispatcher(ConstantJSP.ADMIN_PAGE).forward(request, response);
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
