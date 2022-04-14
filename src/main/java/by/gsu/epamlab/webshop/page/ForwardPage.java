package by.gsu.epamlab.webshop.page;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class ForwardPage extends AbstractPage {
    public ForwardPage(String url) {
        super(url);
    }

    @Override
    public void sendRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher(getUrl()).forward(request, response);
    }
}
