package by.gsu.epamlab.webshop.command;

import by.gsu.epamlab.webshop.controllers.ConstantJSP;
import by.gsu.epamlab.webshop.exceptions.CommandException;
import by.gsu.epamlab.webshop.model.Cart;
import by.gsu.epamlab.webshop.model.Order;
import by.gsu.epamlab.webshop.model.Product;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class OrderPutListCommand implements InterfaceCommand {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        final Logger LOGGER = LogManager.getLogger();
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
        } else {
            request.setAttribute(CommandConstant.ERROR, opProduct);
            return ConstantJSP.ERROR_PAGE;
        }
        return ConstantJSP.USER_PAGE;

    }
}
