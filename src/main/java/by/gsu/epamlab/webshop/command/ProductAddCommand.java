package by.gsu.epamlab.webshop.command;

import by.gsu.epamlab.webshop.controllers.ConstantJSP;
import by.gsu.epamlab.webshop.dao.ProductDaoImpl;
import by.gsu.epamlab.webshop.dao.Utility;
import by.gsu.epamlab.webshop.exceptions.CommandException;
import by.gsu.epamlab.webshop.exceptions.DaoException;
import by.gsu.epamlab.webshop.model.Product;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ProductAddCommand implements InterfaceCommand {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        final Logger LOGGER = LogManager.getLogger();
        ProductDaoImpl productDao = new ProductDaoImpl();
        if (isValidRequest(request)) {
            String quantity = request.getParameter(CommandConstant.QUANTITY);
            Product product = Utility.createProductFromRequest(request);
            HttpSession session = request.getSession();
            if (product.isValid()) {
                try {
                    int idProduct = productDao.add(product);
                    if (idProduct != 0) {
                        session.setAttribute(CommandConstant.PRODUCT, product);
                        request.setAttribute(CommandConstant.QUANTITY, quantity);
                    } else {
                        request.setAttribute(CommandConstant.ERROR, "Didn't add Product, returned idProduct == 0");
                        return ConstantJSP.ERROR_PAGE;
                    }
                } catch (DaoException e) {
                    LOGGER.error("Add product to database is not successful", e.getCause());
                    throw new CommandException("Add product to database is not successful", e.getCause());
                }
            } else {
                request.setAttribute(CommandConstant.INFO, "Invalid object " + product);
                LOGGER.info("Invalid object " + product);
                return ConstantJSP.ADMIN_PAGE;
            }
        } else {
            request.setAttribute(CommandConstant.INFO, "Fields of product can't be empty");
            LOGGER.info("Invalid fields for init Product");
            return ConstantJSP.ADMIN_PAGE;
        }
        return ConstantJSP.STORAGE_ADD_PAGE;
    }
}
