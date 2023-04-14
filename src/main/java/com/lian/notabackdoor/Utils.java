package com.lian.notabackdoor;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.HttpCookie;
import java.util.List;

public class Utils {
    public static String formatFileSize(long bytes) {
        if (bytes < 1024) {
            return bytes + "B";
        }
        int exp = (int) (Math.log(bytes) / Math.log(1024));
        char unit = "KMGTPE".charAt(exp-1);
        double size = bytes / Math.pow(1024, exp);
        return String.format("%.2f%cB", size, unit);
    }
    public static String encodeHtmlEntities(String input) {
        StringBuilder output = new StringBuilder();
        for (char c : input.toCharArray()) {
            if (c > 127) {
                output.append("&#");
                output.append((int) c);
                output.append(";");
            } else {
                output.append(c);
            }
        }
        return output.toString();
    }
    public static void redirectToLostPage(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().add("Location", "/lost");
        exchange.sendResponseHeaders(302, -1);
        exchange.close();
    }
    public static void redirectToErrorPage(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().add("Location", "/error");
        exchange.sendResponseHeaders(302, -1);
        exchange.close();
    }
    public static void redirectToPasswordPage(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().add("Location", "/password");
        exchange.sendResponseHeaders(302, -1);
        exchange.close();
    }
    public static void redirectToMainPage(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().add("Location", "/main");
        exchange.sendResponseHeaders(302, -1);
        exchange.close();
    }
    public static String getPasswordFromFile() throws IOException {
        String filePath = NotABackdoor.getPlugin(NotABackdoor.class).getDataFolder() + "/pass.txt";
        File file = new File(filePath);
        if (!file.exists()) {
            throw new IOException("Password file does not exist: " + filePath);
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String password = reader.readLine();
            if (password == null || password.trim().isEmpty()) {
                throw new IOException("Password file is empty: " + filePath);
            }
            return password.trim();
        }
    }
    public static void checkUserPassword(HttpExchange exchange) throws IOException {
        Headers requestHeaders = exchange.getRequestHeaders();
        List<String> cookies = requestHeaders.get("Cookie");

        if (cookies != null) {
            for (String cookie : cookies) {
                HttpCookie httpCookie = HttpCookie.parse(cookie).get(0);
                if (httpCookie.getName().equals("password")) {
                    String password = httpCookie.getValue();
                    String passwordFromFile = getPasswordFromFile();
                    if (password.equals(passwordFromFile)) {
                        return;
                    }
                }
            }
        }

        redirectToPasswordPage(exchange);
    }
}
