package com.lian.notabackdoor.pages;

import com.lian.notabackdoor.NotABackdoor;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

public class LostPageHandler implements HttpHandler {

    private static final String PAGE_PATH = "/lost";

    public void handle(HttpExchange exchange) throws IOException {

        String requestedPath = exchange.getRequestURI().getPath();
        if (!requestedPath.equals(PAGE_PATH)) {
            NotABackdoor.redirectToLostPage(exchange);
            return;
        }

        new File(NotABackdoor.getPlugin(NotABackdoor.class).getDataFolder(), "pages/").mkdirs();
        File file = new File(NotABackdoor.getPlugin(NotABackdoor.class).getDataFolder(), "pages/lost.html");

        if (!file.exists()) {
            // Create the lost page file if it doesn't exist
            String defaultLostPageContent = "<html><body><h1>404 - Not Found</h1></body></html>";
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(defaultLostPageContent.getBytes());
            fileOutputStream.close();
        }

        //WebServerPlugin.getPlugin(WebServerPlugin.class).getLogger().log(Level.INFO, file.toString());

        String response = new String(Files.readAllBytes(file.toPath()));
        exchange.sendResponseHeaders(404, response.length());
        exchange.getResponseBody().write(response.getBytes());
        exchange.getResponseBody().close();
    }
}
