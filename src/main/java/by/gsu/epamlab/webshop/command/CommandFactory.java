package by.gsu.epamlab.webshop.command;


import by.gsu.epamlab.webshop.servlets.ConstantJSP;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;
import java.util.Optional;


public class CommandFactory {
    private enum EnumCommand {

        ERROR {
            @Override
            protected InterfaceCommand createCommand() {
                return new ErrorCommand();
            }
        },
        LOGOUT {
            @Override
            protected InterfaceCommand createCommand() {
                return new LogoutCommand();
            }
        },

        LOGIN {
            @Override
            protected InterfaceCommand createCommand() {
                return new LoginCommand();
            }
        },
        REGISTRTION {
            @Override
            protected InterfaceCommand createCommand() {
                return new RegistrationCommand();
            }
        };

        abstract protected InterfaceCommand createCommand();

    }

    private CommandFactory() {
    }

    public static Optional<InterfaceCommand> getCommandFromFactory(HttpServletRequest request) {
        InterfaceCommand interfaceCommand;
        Optional<String> optionalCommand = Optional.of(request.getParameter(ConstantJSP.COMMAND));

        if (optionalCommand.isPresent()) {
            interfaceCommand = EnumCommand.valueOf(optionalCommand.get().toUpperCase(Locale.ROOT)).createCommand();
        } else {
            interfaceCommand = EnumCommand.ERROR.createCommand();
        }
        return Optional.of(interfaceCommand);
    }

}
