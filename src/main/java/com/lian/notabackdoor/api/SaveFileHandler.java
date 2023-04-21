package com.lian.notabackdoor.api;

import com.lian.notabackdoor.Utils;
import com.lian.notabackdoor.pages.ErrorPageHandler;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;

public class SaveFileHandler implements HttpHandler {
    private String rootDirectory = ".";

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Utils.checkUserPassword(exchange);

        String requestMethod = exchange.getRequestMethod();
        if (requestMethod.equalsIgnoreCase("POST")) {
            URI uri = exchange.getRequestURI();
            String filePath = uri.getPath().substring("/api/save-file/".length());

            URI oldUri = exchange.getRequestURI();
            String oldFilePath = oldUri.getPath().substring("/api/save-file/".length());


            try {
                String path = uri.getPath();
                if (path != null && !path.isEmpty()) {
                    int lastSlashIndex = path.lastIndexOf('/');
                    if (lastSlashIndex != -1) {
                        // Remove the last segment (file name or directory name) from the path
                        path = path.substring(0, lastSlashIndex + 1);
                    }
                    // Reconstruct the URL with the updated path
                    uri = new URI(uri.getScheme(), uri.getUserInfo(), uri.getHost(), uri.getPort(), path, uri.getQuery(), uri.getFragment());
                }
                filePath = uri.getPath().substring("/api/save-file/".length());
            } catch (URISyntaxException e) {
                // Handle any URISyntaxException as needed
                e.printStackTrace();
                filePath = uri.getPath().substring("/api/save-file/".length());
            }

            new File(rootDirectory + File.separator + filePath).mkdirs();
            File file = new File(rootDirectory + File.separator + oldFilePath);

            if (oldUri.toString().contains("?")) {
                new ErrorPageHandler().DisplayErrorPage(exchange, "Invalid Request", "The requested action is not allowed. Please try again without including the character \"?\" in your request.", 400);
                return;
            }

            // Read the request body and save it to the file
            InputStream inputStream = exchange.getRequestBody();
            try (OutputStream outputStream = Files.newOutputStream(file.toPath())) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }

            exchange.sendResponseHeaders(201, -1);
        } else {
            new ErrorPageHandler().DisplayErrorPage(exchange, "405 Method Not Allowed", "Only POST requests are allowed on this resource. Please try a POST request or contact the server administrator for assistance.", 405);
        }
    }

}

