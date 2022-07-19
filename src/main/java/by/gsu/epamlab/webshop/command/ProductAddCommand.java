package by.gsu.epamlab.webshop.command;

import by.gsu.epamlab.webshop.connection.ConnectionManager;
import by.gsu.epamlab.webshop.controllers.ConstantJSP;
import by.gsu.epamlab.webshop.dao.ProductDaoImpl;
import by.gsu.epamlab.webshop.dao.Utility;
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

public class ProductAddCommand implements InterfaceCommand {
    @Override
    public AbstractPage execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        ConnectionManager connectionManager = new ConnectionManager();
        ProductDaoImpl productDao = new ProductDaoImpl(connectionManager);
        final Logger LOGGER = LoggerFactory.getLogger(ProductAddCommand.class);
        String quantity = request.getParameter(CommandConstant.QUANTITY);
        Product product = Utility.createProductFromRequest(request);
        HttpSession session = request.getSession();
        try {
            int idProduct = productDao.add(product);
            if (idProduct != 0) {
                session.setAttribute(CommandConstant.PRODUCT, product);
                request.setAttribute(CommandConstant.QUANTITY, quantity);
            } else {
                request.setAttribute(CommandConstant.ERROR, "Didn't add Product, returned idProduct == 0");
                return new ForwardPage(ConstantJSP.ERROR_PAGE);
            }
            return new ForwardPage(ConstantJSP.STORAGE_ADD_PAGE);
        } catch (DaoException e) {
            LOGGER.error("Add product to database is not successful", e.getCause());
            throw new CommandException("Add product to database is not successful", e.getCause());
        }
    }
}
