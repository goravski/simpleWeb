package by.gsu.epamlab.webshop.command;

import by.gsu.epamlab.webshop.exceptions.CommandException;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommandCheck {
    public static String commandCheck(HttpServletRequest request) throws CommandException {
        final Logger LOGGER = LoggerFactory.getLogger(CommandCheck.class);
        Optional<String> optionalCommand = Optional.of(request.getParameter("command"));
        LOGGER.trace("command from request received: " + optionalCommand.get());
        if (optionalCommand.isPresent()) {
            LOGGER.trace("Command for CommandFabric from request received");
           return optionalCommand.get();
        } else {
            LOGGER.trace("Command for CommandFabric from request didn't received, ErrorCommand set");
            return CommandEnum.ERROR.name();
        }
    }
}
