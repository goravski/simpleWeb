package by.gsu.epamlab.webshop.command;

import by.gsu.epamlab.webshop.exceptions.CommandException;
import jakarta.servlet.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;


public class CommandFactory {
    public static InterfaceCommand getCommandFromFactory(HttpServletRequest request) throws CommandException {
        final Logger LOGGER = LoggerFactory.getLogger(CommandFactory.class);
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
