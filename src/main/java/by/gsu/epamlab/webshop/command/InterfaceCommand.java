package by.gsu.epamlab.webshop.command;


import by.gsu.epamlab.webshop.dao.PersonDaoImpl;
import by.gsu.epamlab.webshop.exceptions.CommandException;

import by.gsu.epamlab.webshop.service.Services;
import jakarta.servlet.http.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public interface InterfaceCommand {
    PersonDaoImpl personDao = PersonDaoImpl.getDaoInstance();
    static final Logger LOGGER = LogManager.getLogger();
    Services services = new Services();

    String execute(HttpServletRequest request, HttpServletResponse response) throws CommandException;
}
