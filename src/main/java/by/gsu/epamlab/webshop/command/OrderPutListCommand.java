package by.gsu.epamlab.webshop.command;

import by.gsu.epamlab.webshop.controllers.ConstantJSP;
import by.gsu.epamlab.webshop.exceptions.CommandException;
import by.gsu.epamlab.webshop.model.Cart;
import by.gsu.epamlab.webshop.model.Order;
import by.gsu.epamlab.webshop.model.Product;
import by.gsu.epamlab.webshop.page.AbstractPage;
import by.gsu.epamlab.webshop.page.ForwardPage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class OrderPutListCommand implements InterfaceCommand {
    @Override
    public AbstractPage execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        final Logger LOGGER = LoggerFactory.getLogger(OrderPutListCommand.class);
        String quantity = request.getParameter(CommandConstant.QUANTITY);
        int idProduct = Integer.parseInt(request.getParameter(CommandConstant.ID));
        HttpSession session = request.getSession();
        Cart cart = (Cart) session.getAttribute(CommandConstant.CART);
        //** list products from paging command: */
        List<Product> products = (List<Product>) session.getAttribute(CommandConstant.PRODUCTS);
        //** Get selected product: */
        Optional<Product> opProduct = products.stream().filter(prod -> prod.getIdProduct() == idProduct).findAny();
        if (opProduct.isPresent()) {
            Product product = opProduct.get();
            //** Add product into virtual (out of database) object: */
            Order order = new Order(Integer.parseInt(quantity), product, LocalDate.now());
            //Add order into virtual Cart
            cart.getOrderList().add(order);
            session.setAttribute(CommandConstant.CART, cart);
            LOGGER.info(String.format("Order %s add to list", order));
            return new ForwardPage(ConstantJSP.USER_PAGE);
        } else {
            request.setAttribute(CommandConstant.ERROR, opProduct);
            return new ForwardPage(ConstantJSP.ERROR_PAGE);
        }
    }
}
