package com.lian.notabackdoor.api;

import com.lian.notabackdoor.Utils;
import com.lian.notabackdoor.pages.ErrorPageHandler;
import com.lian.notabackdoor.pages.LostPageHandler;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketException;
import java.net.URI;

public class SetPasswordHandler implements HttpHandler {

    private String rootDirectory = ".";
    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String requestMethod = exchange.getRequestMethod();
        if (requestMethod.equalsIgnoreCase("GET") || requestMethod.equalsIgnoreCase("POST")) {
            URI uri = exchange.getRequestURI();
            String userPassword = uri.getPath().substring("/api/set-password/".length());

            String cookieValue = "password=" + userPassword + "; path=/";
            Headers headers = exchange.getResponseHeaders();
            headers.add("Set-Cookie", cookieValue);

            String cookie = exchange.getRequestHeaders().getFirst("Cookie");
            boolean isPasswordSet = false;
            if (cookie != null) {
                String[] cookieParts = cookie.split(";");
                for (String part : cookieParts) {
                    String[] keyValue = part.split("=");
                    if (keyValue.length == 2 && keyValue[0].trim().equals("password")) {
                        String password = Utils.getPasswordFromFile();
                        if (password.equals(keyValue[1].trim())) {
                            isPasswordSet = true;
                        }
                        break;
                    }
                }
            }

            if (isPasswordSet) {
                // password is correct, redirect to main page
                Utils.redirectToMainPage(exchange);
            } else {
                // password is not set or incorrect, display password page
                Utils.redirectToPasswordPage(exchange);
            }
        } else {
            //exchange.sendResponseHeaders(405, -1); // Method Not Allowed
            new ErrorPageHandler().DisplayErrorPage(exchange, "405 Method Not Allowed", "Only GET and POST requests are allowed on this resource. Please try a GET or POST request or contact the server administrator for assistance.", 405);
        }
    }
}
