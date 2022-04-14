package by.gsu.epamlab.webshop.filter;

import by.gsu.epamlab.webshop.command.CommandConstant;
import by.gsu.epamlab.webshop.model.Cart;
import by.gsu.epamlab.webshop.model.Order;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@WebFilter(urlPatterns = {"/cart_pay"})
public class PaymentFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpSession session = request.getSession();
        Cart cart = (Cart) session.getAttribute(CommandConstant.CART);
        int cartId = cart.getId();
        List<Order> orderList = cart.getOrderList();
        List<Order> newList = orderList.stream().map(s -> new Order(cartId, s)).collect(Collectors.toList());
        request.setAttribute(CommandConstant.ORDER_LIST, newList);
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
