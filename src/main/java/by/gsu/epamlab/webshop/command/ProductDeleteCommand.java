package by.gsu.epamlab.webshop.command;

import by.gsu.epamlab.webshop.connection.ConnectionManager;
import by.gsu.epamlab.webshop.controllers.ConstantJSP;
import by.gsu.epamlab.webshop.dao.ProductDaoImpl;
import by.gsu.epamlab.webshop.exceptions.CommandException;
import by.gsu.epamlab.webshop.exceptions.DaoException;
import by.gsu.epamlab.webshop.model.Product;
import by.gsu.epamlab.webshop.page.AbstractPage;
import by.gsu.epamlab.webshop.page.ForwardPage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProductDeleteCommand implements InterfaceCommand {
    @Override
    public AbstractPage execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        final Logger LOGGER = LoggerFactory.getLogger(ProductDeleteCommand.class);
        ConnectionManager connectionManager = new ConnectionManager();
        ProductDaoImpl productDao = new ProductDaoImpl(connectionManager);
        HttpSession session = request.getSession();
        Product product = (Product) session.getAttribute(CommandConstant.PRODUCT);
        try {
            productDao.delete(product);
            LOGGER.info("Product" + product + "deleted");
            session.setAttribute(CommandConstant.PRODUCT, new Product());
            return new ForwardPage(ConstantJSP.ADMIN_PAGE);
        } catch (DaoException e) {
            LOGGER.error("Can't delete " + product + " from database", e.getCause());
            throw new CommandException(e.getMessage(), e.getCause());
        }
    }
}
