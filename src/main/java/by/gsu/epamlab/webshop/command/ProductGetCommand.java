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

import java.util.Optional;

public class ProductGetCommand implements InterfaceCommand {

    @Override
    public AbstractPage execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        final Logger LOGGER = LogManager.getLogger();
        ConnectionManager connectionManager = new ConnectionManager();
        ProductDaoImpl productDao = new ProductDaoImpl(connectionManager);
        String id = request.getParameter(CommandConstant.ID);
        HttpSession session = request.getSession();
        try {
            Optional<Product> optionalProduct = productDao.getById(Integer.parseInt(id));
            if (optionalProduct.isPresent()) {
                session.setAttribute(CommandConstant.PRODUCT, optionalProduct.get());
            } else {
                session.setAttribute(CommandConstant.PRODUCT, new Product());
            }

        } catch (DaoException e) {
            LOGGER.error("Can't get Product from database", e.getCause());
            throw new CommandException(e.getMessage(), e.getCause());
        }
        return new ForwardPage(ConstantJSP.ADMIN_PAGE);
    }
}
