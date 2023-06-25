package com.lian.notabackdoor.pages;

import com.lian.notabackdoor.NotABackdoor;
import com.lian.notabackdoor.Utils;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

public class MainPageHandler implements HttpHandler {

    private static final String PAGE_PATH = "/main";

    public void handle(HttpExchange exchange) throws IOException {

        Utils.checkUserPassword(exchange);

        String requestMethod = exchange.getRequestMethod();
        if (requestMethod.equalsIgnoreCase("GET")) {

            String requestedPath = exchange.getRequestURI().getPath();
            if (!requestedPath.equals(PAGE_PATH)) {
                Utils.redirectToLostPage(exchange);
                return;
            }

            String response = "<html><head><title>Main Page</title><style>body{background-color:#242424;color: #fff;display:flex;flex-direction:column;align-items:center;justify-content:center;height:100vh;margin:0;}.button{padding:20px 40px;font-size:18px;background-color:#4CAF50;color:#fff;border:none;border-radius:5px;cursor:pointer;}</style></head><body><h1>Main Page</h1><button class=\"button\" onclick=\"location.href='/filemanager'\">Go to FileManager</button></body></html>";
            exchange.sendResponseHeaders(200, response.length());
            OutputStream out = exchange.getResponseBody();
            out.write(response.getBytes());
            out.close();
        } else {
            new ErrorPageHandler().DisplayErrorPage(exchange, "405 Method Not Allowed", "Only GET requests are allowed on this resource. Please try a GET request or contact the server administrator for assistance.", 405);
        }
    }
}
