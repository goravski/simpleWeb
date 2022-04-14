package by.gsu.epamlab.webshop.page;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public abstract class AbstractPage {
    private final String url;

    protected AbstractPage(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public abstract void sendRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
}
