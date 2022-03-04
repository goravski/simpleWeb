package by.gsu.epamlab.webshop.command;

import java.util.function.Supplier;

public enum CommandEnum {
    ERROR(ErrorCommand::new),
    LOGOUT(UserLogoutCommand::new),
    LOGIN(LoginCommand::new),
    REGISTRATION(UserRegistrationCommand::new),
    GET_USER (UserGetCommand::new),
    UPDATE_USER (UserUpdateCommand:: new);
    private final InterfaceCommand command;

   CommandEnum(Supplier<InterfaceCommand> command) {
        this.command = command.get();
    }
    public InterfaceCommand createCommand (){
        return command;
    }
}
