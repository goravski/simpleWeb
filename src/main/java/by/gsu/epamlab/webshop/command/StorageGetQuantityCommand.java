package by.gsu.epamlab.webshop.command;

import by.gsu.epamlab.webshop.connection.ConnectionManager;
import by.gsu.epamlab.webshop.controllers.ConstantJSP;
import by.gsu.epamlab.webshop.dao.StorageDao;
import by.gsu.epamlab.webshop.exceptions.CommandException;
import by.gsu.epamlab.webshop.exceptions.DaoException;
import by.gsu.epamlab.webshop.model.Product;
import by.gsu.epamlab.webshop.model.Storage;
import by.gsu.epamlab.webshop.page.AbstractPage;
import by.gsu.epamlab.webshop.page.ForwardPage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class StorageGetQuantityCommand implements InterfaceCommand {
    @Override
    public AbstractPage execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        final Logger LOGGER = LogManager.getLogger();
        ConnectionManager connectionManager = new ConnectionManager();
        StorageDao storageDao = new StorageDao(connectionManager);
        HttpSession session = request.getSession();
        Product product = (Product) session.getAttribute(CommandConstant.PRODUCT);
        int idProduct = product.getIdProduct();
        try {
            Optional<Storage> optionalStorage = storageDao.getById(idProduct);
            optionalStorage.ifPresent(storage -> request.setAttribute(CommandConstant.STORAGE, storage));
            return new ForwardPage(ConstantJSP.ADMIN_PAGE);
        } catch (DaoException e) {
            LOGGER.error("Get quantity from database is not successful", e.getCause());
            throw new CommandException("Get quantity from database is not successful", e.getCause());
        }
    }
}
