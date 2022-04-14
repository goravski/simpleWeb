package by.gsu.epamlab.webshop.filter;

import by.gsu.epamlab.webshop.command.CommandConstant;
import by.gsu.epamlab.webshop.controllers.ConstantJSP;
import by.gsu.epamlab.webshop.pagination.Pagination;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter(urlPatterns = {"/get_product_page"})
public class PaginationViewFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        Pagination pagination;
        if (request.getParameter(CommandConstant.CURRENT_PAGE) != null){
            int currentPage = Integer.parseInt(request.getParameter(CommandConstant.CURRENT_PAGE));
            pagination = new Pagination(currentPage);
            request.setAttribute(CommandConstant.PAGINATION, pagination);
        } else {
            request.getRequestDispatcher(ConstantJSP.USER_PAGE).forward(request, response);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
