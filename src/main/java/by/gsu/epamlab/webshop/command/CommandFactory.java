package by.gsu.epamlab.webshop.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.servlet.http.*;
import java.util.Locale;
import java.util.Optional;

public class CommandFactory {
    private static final Logger LOGGER = LogManager.getLogger();

    public static Optional<InterfaceCommand> getCommandFromFactory(HttpServletRequest request) {
        InterfaceCommand interfaceCommand;
        Optional<String> optionalCommand = Optional.of(request.getParameter("command"));
        LOGGER.trace("command from request received: " + optionalCommand.get());
        if (optionalCommand.isPresent()) {
            interfaceCommand = CommandEnum.valueOf(optionalCommand.get().toUpperCase(Locale.ROOT)).createCommand();
            LOGGER.trace("Command from CommandFabric received");
        } else {
            interfaceCommand = CommandEnum.ERROR.createCommand();
            LOGGER.trace("Command from CommandFabric didn't received, ErrorCommand set");
        }
        return Optional.of(interfaceCommand);
    }

}
