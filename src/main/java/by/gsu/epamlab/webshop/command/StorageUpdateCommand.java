package by.gsu.epamlab.webshop.command;

import by.gsu.epamlab.webshop.controllers.ConstantJSP;
import by.gsu.epamlab.webshop.dao.StorageDao;
import by.gsu.epamlab.webshop.dao.Utility;
import by.gsu.epamlab.webshop.exceptions.CommandException;
import by.gsu.epamlab.webshop.exceptions.DaoException;
import by.gsu.epamlab.webshop.model.Storage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class StorageUpdateCommand implements InterfaceCommand {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        final Logger LOGGER = LogManager.getLogger();
        StorageDao storageDao = new StorageDao();
        Storage storage = Utility.createStorage(request);
        if(storage.isValid()){
            try {
                storageDao.update(storage);
                request.setAttribute(CommandConstant.STORAGE, storage);
            } catch (DaoException e) {
                LOGGER.error("Add quantity to database is not successful", e.getCause());
                throw new CommandException("Add quantity to database is not successful", e.getCause());
            }
        } else {
            request.setAttribute(CommandConstant.INFO, "Invalid object " + storage);
            LOGGER.info("Invalid object " + storage);
            return ConstantJSP.ADMIN_PAGE;
        }

        return ConstantJSP.ADMIN_PAGE;
    }
}
