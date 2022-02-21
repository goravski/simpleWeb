package by.gsu.epamlab.webshop.command;


import by.gsu.epamlab.webshop.exceptions.CommandException;

import jakarta.servlet.http.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public interface InterfaceCommand {
    Logger log = LogManager.getLogger();

    String execute(HttpServletRequest request, HttpServletResponse response) throws CommandException;
}
