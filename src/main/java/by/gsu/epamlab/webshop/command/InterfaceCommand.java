package by.gsu.epamlab.webshop.command;


import by.gsu.epamlab.webshop.exceptions.CommandException;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public interface InterfaceCommand {
    Logger log = Logger.getLogger(LoginCommand.class);
    String execute (HttpServletRequest request, HttpServletResponse response) throws CommandException;
}
