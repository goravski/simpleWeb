package by.gsu.epamlab.webshop.filter;

import by.gsu.epamlab.webshop.command.CommandConstant;
import by.gsu.epamlab.webshop.controllers.ConstantJSP;
import by.gsu.epamlab.webshop.model.Cart;
import by.gsu.epamlab.webshop.model.Order;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@WebFilter(urlPatterns = {"/order"})
public class OrderFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        final Logger LOGGER = LogManager.getLogger();
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession();
        int quantity = Integer.parseInt(request.getParameter(CommandConstant.QUANTITY));
        int idProduct = Integer.parseInt(request.getParameter(CommandConstant.ID));
        Cart cart = (Cart) session.getAttribute(CommandConstant.CART);
        List<Order> orderList = cart.getOrderList();
        Optional<Order> optionalOrder = orderList.stream()
                .filter(order -> order.getProduct().getIdProduct() == idProduct)
                .filter(order -> order.getOrderQuantity() == quantity)
                .findFirst();
        Order order =null;
        if (optionalOrder.isPresent()) {
            order = optionalOrder.get();
        } else {
            LOGGER.error(String.format("Cant get Order from list orders: %s", orderList));
            request.setAttribute(CommandConstant.ERROR, orderList);
            request.getRequestDispatcher(ConstantJSP.ERROR_PAGE).forward(request, response);
        }
        if (order.isValid()) {
            request.setAttribute(CommandConstant.ORDER, order);
        } else {
            LOGGER.error(String.format("Order %s is not valid with person %s",
                    order, cart.getPersonId()));
            request.setAttribute(CommandConstant.ERROR, order);
            request.getRequestDispatcher(ConstantJSP.ERROR_PAGE).forward(request, response);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

}
