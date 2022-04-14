package by.gsu.epamlab.webshop.command;


import by.gsu.epamlab.webshop.connection.ConnectionManager;
import by.gsu.epamlab.webshop.exceptions.CommandException;

import by.gsu.epamlab.webshop.page.AbstractPage;
import jakarta.servlet.http.*;

import java.util.Enumeration;


public interface InterfaceCommand {
    AbstractPage execute(HttpServletRequest request, HttpServletResponse response) throws CommandException;


}
