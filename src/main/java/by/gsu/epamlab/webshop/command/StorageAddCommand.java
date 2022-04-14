package by.gsu.epamlab.webshop.command;

import by.gsu.epamlab.webshop.connection.ConnectionManager;
import by.gsu.epamlab.webshop.controllers.ConstantJSP;
import by.gsu.epamlab.webshop.dao.StorageDao;
import by.gsu.epamlab.webshop.exceptions.CommandException;
import by.gsu.epamlab.webshop.exceptions.DaoException;
import by.gsu.epamlab.webshop.model.Storage;
import by.gsu.epamlab.webshop.page.AbstractPage;
import by.gsu.epamlab.webshop.page.ForwardPage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class StorageAddCommand implements InterfaceCommand {
    @Override
    public AbstractPage execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        final Logger LOGGER = LogManager.getLogger();
        ConnectionManager connectionManager = new ConnectionManager();
        StorageDao storageDao = new StorageDao(connectionManager);
        Storage storage = (Storage) request.getAttribute(CommandConstant.STORAGE);
        try {
            int idStorage = storageDao.add(storage);
            if (idStorage != 0) {
                request.setAttribute(CommandConstant.STORAGE, storage);
            } else {
                request.setAttribute(CommandConstant.ERROR, storage);
                request.setAttribute(CommandConstant.DAO_METHOD, idStorage);
                return new ForwardPage(ConstantJSP.ERROR_PAGE);
            }
            return new ForwardPage(ConstantJSP.ADMIN_PAGE);
        } catch (DaoException e) {
            LOGGER.error("Add quantity to database is not successful", e.getCause());
            throw new CommandException("Add quantity to database is not successful", e.getCause());
        }
    }
}
