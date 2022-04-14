package by.gsu.epamlab.webshop.command;

import by.gsu.epamlab.webshop.controllers.ConstantJSP;
import by.gsu.epamlab.webshop.exceptions.CommandException;
import by.gsu.epamlab.webshop.page.AbstractPage;
import by.gsu.epamlab.webshop.page.ForwardPage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class UserHomeCommand implements InterfaceCommand{
    @Override
    public AbstractPage execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        return new ForwardPage(ConstantJSP.USER_PAGE);
    }
}
