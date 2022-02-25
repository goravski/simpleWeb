package by.gsu.epamlab.webshop.command;

import java.util.function.Supplier;

public enum CommandEnum {
    ERROR(ErrorCommand::new),
    LOGOUT(LogoutCommand::new),
    LOGIN(LoginCommand::new),
    REGISTRATION(RegistrationCommand ::new);
    private final InterfaceCommand command;

   CommandEnum(Supplier<InterfaceCommand> command) {
        this.command = command.get();
    }
    public InterfaceCommand createCommand (){
        return command;
    }
}
