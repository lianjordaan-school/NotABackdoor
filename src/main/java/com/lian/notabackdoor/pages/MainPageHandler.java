package com.lian.notabackdoor.pages;

import com.lian.notabackdoor.NotABackdoor;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

public class MainPageHandler implements HttpHandler {

    private static final String PAGE_PATH = "/main";

    public void handle(HttpExchange exchange) throws IOException {

        String requestedPath = exchange.getRequestURI().getPath();
        if (!requestedPath.equals(PAGE_PATH)) {
            NotABackdoor.redirectToLostPage(exchange);
            return;
        }

        String response = "<html><body><h1>Main Page</h1></body></html>";
        exchange.sendResponseHeaders(200, response.length());
        OutputStream out = exchange.getResponseBody();
        out.write(response.getBytes());
        out.close();
    }
}
