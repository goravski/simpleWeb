package by.gsu.epamlab.webshop.filter;


import by.gsu.epamlab.webshop.command.CommandConstant;
import by.gsu.epamlab.webshop.controllers.ConstantJSP;
import by.gsu.epamlab.webshop.dao.Utility;
import by.gsu.epamlab.webshop.model.Storage;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@WebFilter(urlPatterns = {"/storage"})
public class StorageFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        final Logger LOGGER = LoggerFactory.getLogger(StorageFilter.class);
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        Storage storage = Utility.createStorage(request);
        if (storage.isValid()) {
            request.setAttribute(CommandConstant.STORAGE, storage);
        } else {
            request.setAttribute(CommandConstant.INFO, "Invalid object " + storage);
            LOGGER.info("Invalid object " + storage);
            request.getRequestDispatcher(ConstantJSP.ADMIN_PAGE).forward(request, response);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
