package by.gsu.epamlab.webshop.command;

import by.gsu.epamlab.webshop.controllers.ConstantJSP;
import by.gsu.epamlab.webshop.dao.ProductDaoImpl;
import by.gsu.epamlab.webshop.dao.Utility;
import by.gsu.epamlab.webshop.exceptions.CommandException;
import by.gsu.epamlab.webshop.exceptions.DaoException;
import by.gsu.epamlab.webshop.model.Product;
import by.gsu.epamlab.webshop.pagination.Pagination;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class ProductGetByPageCommand implements InterfaceCommand {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        final Logger LOGGER = LogManager.getLogger();
        ProductDaoImpl productDao = new ProductDaoImpl();
        Pagination pagination = new Pagination();
        HttpSession session = request.getSession();
        if (request.getParameter(CommandConstant.CURRENT_PAGE) != null){
            int currentPage = Integer.parseInt(request.getParameter(CommandConstant.CURRENT_PAGE));
            pagination = new Pagination(currentPage);
        }
        int rows = pagination.getMaxRows();
        try {
            List<Product> products = productDao.getAllByPage(pagination.getOffset(), rows);
            session.setAttribute(CommandConstant.PRODUCTS, products);
            request.setAttribute(CommandConstant.TOTAL_PAGES, Utility.getTotalPages(productDao, rows));
            request.setAttribute(CommandConstant.PAGINATION, pagination);
        } catch (DaoException e) {
            LOGGER.error("Can't get Product List from database", e.getCause());
            throw new CommandException(e.getMessage(), e.getCause());
        }
        return ConstantJSP.USER_PAGE;
    }
}
