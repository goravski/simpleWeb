package by.gsu.epamlab.webshop.command;

import by.gsu.epamlab.webshop.controllers.ConstantJSP;
import by.gsu.epamlab.webshop.dao.StorageDao;
import by.gsu.epamlab.webshop.exceptions.CommandException;
import by.gsu.epamlab.webshop.exceptions.DaoException;
import by.gsu.epamlab.webshop.model.Product;
import by.gsu.epamlab.webshop.model.Storage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class StorageGetQuantityCommand implements InterfaceCommand {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        final Logger LOGGER = LogManager.getLogger();
        StorageDao storageDao = new StorageDao();
        HttpSession session = request.getSession();
        Product product = (Product)session.getAttribute(CommandConstant.PRODUCT);
        int idProduct = product.getIdProduct();
        try {
           Optional<Storage> optionalStorage = storageDao.getById(idProduct);
           if (optionalStorage.isPresent()){
               request.setAttribute(CommandConstant.STORAGE, optionalStorage.get());
           }
        } catch (DaoException e) {
            e.printStackTrace();
        }

        return ConstantJSP.ADMIN_PAGE;
    }
}
