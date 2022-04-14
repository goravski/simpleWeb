package by.gsu.epamlab.webshop.command;

import by.gsu.epamlab.webshop.exceptions.CommandException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.servlet.http.*;
import java.util.Locale;


public class CommandFactory {
    public static InterfaceCommand getCommandFromFactory(HttpServletRequest request) throws CommandException {
        final Logger LOGGER = LogManager.getLogger();
        InterfaceCommand interfaceCommand;
        try {
            String command = CommandCheck.commandCheck(request);
            interfaceCommand =CommandEnum.valueOf(command.toUpperCase(Locale.ROOT)).createCommand();
            LOGGER.trace("Command from CommandFabric received");
        } catch (CommandException e) {
            LOGGER.error("Command isempty", e.getCause());
            throw  new CommandException("Command fron request is empty");
        }

        return interfaceCommand;
    }
}
