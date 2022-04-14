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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ProductDeleteCommand implements InterfaceCommand {
    @Override
    public AbstractPage execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        final Logger LOGGER = LogManager.getLogger();
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
