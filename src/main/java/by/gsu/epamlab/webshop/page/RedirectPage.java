package by.gsu.epamlab.webshop.page;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class RedirectPage extends AbstractPage{
    public RedirectPage(String url) {
        super(url);
    }

    @Override
    public void sendRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + getUrl());
    }
}
