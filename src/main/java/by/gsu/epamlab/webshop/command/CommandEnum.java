package by.gsu.epamlab.webshop.command;

import java.util.function.Supplier;

public enum CommandEnum {
    ERROR(ErrorCommand::new),
    LOGOUT(UserLogoutCommand::new),
    LOGIN(LoginCommand::new),
    REGISTRATION(UserRegistrationCommand::new),
    GET_USER (UserGetCommand::new),
    UPDATE_USER (UserUpdateCommand:: new),
    USER_HOME(UserHomeCommand::new),
    GET_PRODUCT (ProductGetCommand::new),
    GET_PRODUCT_PAGE(ProductGetByPageCommand::new),
    ORDER_ADD (OrderAddCommand::new),
    ORDER_PUT_LIST(OrderPutListCommand::new),
    ORDER_DELETE_LIST(OrderDeleteListCommand::new),
    ADD_PRODUCT (ProductAddCommand::new),
    DELETE_PRODUCT(ProductDeleteCommand::new),
    UPDATE_PRODUCT(ProductUpdateCommand::new),
    GET_STORAGE(StorageGetQuantityCommand::new),
    ADD_STORAGE(StorageAddCommand::new),
    UPDATE_STORAGE(StorageUpdateCommand::new),
    ADD_CART(CartAddCommand::new),
    GET_CART(CartGetCommand::new);


    private final InterfaceCommand command;

   CommandEnum(Supplier<InterfaceCommand> command) {
        this.command = command.get();
    }
    public InterfaceCommand createCommand (){
        return command;
    }
}
