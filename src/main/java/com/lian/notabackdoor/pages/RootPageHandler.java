package com.lian.notabackdoor.pages;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class RootPageHandler implements HttpHandler {
    public void handle(HttpExchange exchange) throws IOException {
        String requestedPath = exchange.getRequestURI().getPath();
        if (requestedPath.equals("/")) {
            exchange.getResponseHeaders().add("Location", "/main");
            exchange.sendResponseHeaders(302, -1);
        } else {
            new LostPageHandler().DisplayLostPage(exchange);
        }
        exchange.close();
    }
}
