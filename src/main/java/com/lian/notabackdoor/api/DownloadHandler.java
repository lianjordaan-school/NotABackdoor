package com.lian.notabackdoor.api;

import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.lian.notabackdoor.NotABackdoor;
import com.lian.notabackdoor.Utils;
import com.lian.notabackdoor.pages.ErrorPageHandler;
import com.lian.notabackdoor.pages.LostPageHandler;
import com.sun.net.httpserver.*;

public class DownloadHandler implements HttpHandler {
    private String rootDirectory = ".";

    private ExecutorService executorService;

    public DownloadHandler() {
        // Create a thread pool with a fixed number of threads
        this.executorService = Executors.newFixedThreadPool(10);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        Utils.checkUserPassword(exchange);

        String requestMethod = exchange.getRequestMethod();
        if (requestMethod.equalsIgnoreCase("GET")) {
            URI uri = exchange.getRequestURI();
            String filePath = uri.getPath().substring("/api/download/".length());
            File file = new File(rootDirectory + File.separator + filePath);

            if (uri.toString().contains("?")) {
                new ErrorPageHandler().DisplayErrorPage(exchange, "Invalid Request", "The requested action is not allowed. Please try again without including the character \"?\" in your request.", 400);
                return;
            }

            if (!file.exists()) {
                new LostPageHandler().DisplayLostPage(exchange);
                return;
            }

            Headers headers = exchange.getResponseHeaders();
            headers.add("Content-Type", "application/octet-stream");
            headers.add("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");

            exchange.sendResponseHeaders(200, file.length());

            this.executorService.submit(() -> {
                try (OutputStream os = exchange.getResponseBody();
                     FileInputStream fis = new FileInputStream(file)) {

                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = fis.read(buffer)) != -1) {
                        os.write(buffer, 0, bytesRead);
                    }
                } catch (IOException e) {
                    if (!(e instanceof SocketException && "Connection reset by peer".equals(e.getMessage())) && !(e.getMessage().contains("Broken pipe") || e.getMessage().contains("insufficient bytes written to stream"))) {
                        // Log the error only if it's not caused by a connection reset by the client or a broken pipe/insufficient bytes written
                        e.printStackTrace();
                    }

                } finally {
                    exchange.close();
                }
            });
        } else {
            //exchange.sendResponseHeaders(405, -1); // Method Not Allowed
            new ErrorPageHandler().DisplayErrorPage(exchange, "405 Method Not Allowed", "Only GET requests are allowed on this resource. Please try a GET request or contact the server administrator for assistance.", 405);
        }
    }
}

