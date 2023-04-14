package com.lian.notabackdoor.pages;

import com.lian.notabackdoor.NotABackdoor;
import com.lian.notabackdoor.Utils;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class PasswordPageHandler implements HttpHandler {

    private static final String PAGE_PATH = "/password";

    public void handle(HttpExchange exchange) throws IOException {

        String requestMethod = exchange.getRequestMethod();
        if (requestMethod.equalsIgnoreCase("GET")) {

            String requestedPath = exchange.getRequestURI().getPath();
            if (!requestedPath.equals(PAGE_PATH)) {
                Utils.redirectToLostPage(exchange);
                return;
            }

            // check if password cookie is set
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
                DisplayPasswordPage(exchange);
            }
        } else {
            new ErrorPageHandler().DisplayErrorPage(exchange, "405 Method Not Allowed", "Only GET requests are allowed on this resource. Please try a GET request or contact the server administrator for assistance.", 405);
        }
    }

    public void DisplayPasswordPage(HttpExchange exchange) throws IOException {
        new File(NotABackdoor.getPlugin(NotABackdoor.class).getDataFolder(), "pages/").mkdirs();
        File file = new File(NotABackdoor.getPlugin(NotABackdoor.class).getDataFolder(), "pages/password.html");

        if (!file.exists()) {
            String content = null;
            // Create the password page file if it doesn't exist
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("pages/password.html");
            //NotABackdoor.getPlugin(NotABackdoor.class).getLogger().log(Level.INFO, inputStream.toString());
            if (inputStream != null) {
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                int nRead;
                byte[] data = new byte[1024];
                while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                    buffer.write(data, 0, nRead);
                }
                buffer.flush();
                content = new String(buffer.toByteArray(), StandardCharsets.UTF_8);
                inputStream.close();
            } else {
                content = "<html><body><form method=\"post\" action=\"/check-password\"> <input type=\"password\" name=\"password\" required> <button type=\"submit\">Submit</button></form></body></html>";
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(content.getBytes());
            fileOutputStream.close();
        }

        String response = new String(Files.readAllBytes(file.toPath()));
        exchange.sendResponseHeaders(200, response.length());
        exchange.getResponseBody().write(response.getBytes());
        exchange.getResponseBody().close();
    }
}
