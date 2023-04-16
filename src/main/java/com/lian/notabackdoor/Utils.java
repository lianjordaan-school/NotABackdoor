package com.lian.notabackdoor;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.net.HttpCookie;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.logging.Level;

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

    public static void setNewPassword() {
        String filePath = NotABackdoor.getPlugin(NotABackdoor.class).getDataFolder() + "/pass.txt";
        File file = new File(filePath);

        String password = "";

        if (Objects.equals(NotABackdoor.getPlugin(NotABackdoor.class).setPassword, "0")) {
            // Generate a random password
            String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
            int length = NotABackdoor.getPlugin(NotABackdoor.class).passLength;
            StringBuilder passwordString = new StringBuilder();
            Random random = new Random();
            for (int i = 0; i < length; i++) {
                passwordString.append(characters.charAt(random.nextInt(characters.length())));
            }
            password = passwordString.toString();
        } else {
            password = NotABackdoor.getPlugin(NotABackdoor.class).setPassword;
        }
        // Save the password to the file
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(password);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Send a message to the console with the URL containing the random password
        String url = NotABackdoor.getPlugin(NotABackdoor.class).protocol + "://" + NotABackdoor.getPlugin(NotABackdoor.class).ip + ":" + NotABackdoor.getPlugin(NotABackdoor.class).port + "/api/set-password/" + password;
        if (NotABackdoor.getPlugin(NotABackdoor.class).sendPasswordLink) {
            NotABackdoor.getPlugin(NotABackdoor.class).getLogger().log(Level.INFO, "Password for NotABackdoor: " + url);
        }
    }

    public static boolean isFileExtensionAllowed(String fileName) {
        // Get the file extension from the file name
        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);

        // Check if the file extension is present in the allowedExtensions list
        return NotABackdoor.getPlugin(NotABackdoor.class).allowedFileExtensions.contains(fileExtension);
    }

    public static void generateFiles() throws IOException {

        Class<?> clazz = NotABackdoor.getPlugin(NotABackdoor.class).getClass();

        File file = null;


        new File(NotABackdoor.getPlugin(NotABackdoor.class).getDataFolder(), "pages/").mkdirs();
        file = new File(NotABackdoor.getPlugin(NotABackdoor.class).getDataFolder(), "pages/password.html");

        if (!file.exists()) {
            String content = null;
            // Create the password page file if it doesn't exist
            InputStream inputStream = clazz.getClassLoader().getResourceAsStream("pages/password.html");
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

        file = new File(NotABackdoor.getPlugin(NotABackdoor.class).getDataFolder(), "pages/error.html");

        if (!file.exists()) {
            String content = null;
            // Create the error page file if it doesn't exist
            InputStream inputStream = clazz.getClassLoader().getResourceAsStream("pages/error.html");
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
                content = "<html><body><h1>418 - I'm a teapot</h1></body></html>";
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(content.getBytes());
            fileOutputStream.close();
        }

        file = new File(NotABackdoor.getPlugin(NotABackdoor.class).getDataFolder(), "pages/lost.html");

        if (!file.exists()) {
            String content = null;
            // Create the lost page file if it doesn't exist
            InputStream inputStream = clazz.getClassLoader().getResourceAsStream("pages/lost.html");
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
                content = "<html><body><h1>404 - Not Found</h1></body></html>";
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(content.getBytes());
            fileOutputStream.close();
        }

        new File(NotABackdoor.getPlugin(NotABackdoor.class).getDataFolder(), "pages/file-manager/").mkdirs();
        File pre_html_file = new File(NotABackdoor.getPlugin(NotABackdoor.class).getDataFolder(), "pages/file-manager/pre-html.html");

        if (!pre_html_file.exists()) {
            String content = null;
            InputStream inputStream = clazz.getClassLoader().getResourceAsStream("pages/file-manager/pre-html.html");
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
                content = "<html><body><h1>Failed to load html page, please check files! This is a template!</h1></body></html>";
            }
            FileOutputStream fileOutputStream = new FileOutputStream(pre_html_file);
            fileOutputStream.write(content.getBytes());
            fileOutputStream.close();
        }

        File script_file = new File(NotABackdoor.getPlugin(NotABackdoor.class).getDataFolder(), "pages/file-manager/script.js");

        if (!script_file.exists()) {
            String content = null;
            InputStream inputStream = clazz.getClassLoader().getResourceAsStream("pages/file-manager/script.js");
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
                content = "";
            }
            FileOutputStream fileOutputStream = new FileOutputStream(script_file);
            fileOutputStream.write(content.getBytes());
            fileOutputStream.close();
        }


        File style_file = new File(NotABackdoor.getPlugin(NotABackdoor.class).getDataFolder(), "pages/file-manager/style.css");

        if (!style_file.exists()) {
            String content = null;
            InputStream inputStream = clazz.getClassLoader().getResourceAsStream("pages/file-manager/style.css");
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
                content = "";
            }
            FileOutputStream fileOutputStream = new FileOutputStream(style_file);
            fileOutputStream.write(content.getBytes());
            fileOutputStream.close();
        }

        File folder_icon_file = new File(NotABackdoor.getPlugin(NotABackdoor.class).getDataFolder(), "pages/file-manager/folder-icon.svg");

        if (!folder_icon_file.exists()) {
            String content = null;
            InputStream inputStream = clazz.getClassLoader().getResourceAsStream("pages/file-manager/folder-icon.svg");
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
                content = "";
            }
            FileOutputStream fileOutputStream = new FileOutputStream(folder_icon_file);
            fileOutputStream.write(content.getBytes());
            fileOutputStream.close();
        }

        File file_icon_file = new File(NotABackdoor.getPlugin(NotABackdoor.class).getDataFolder(), "pages/file-manager/file-icon.svg");

        if (!file_icon_file.exists()) {
            String content = null;
            InputStream inputStream = clazz.getClassLoader().getResourceAsStream("pages/file-manager/file-icon.svg");
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
                content = "";
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file_icon_file);
            fileOutputStream.write(content.getBytes());
            fileOutputStream.close();
        }

        File download_file_icon_file = new File(NotABackdoor.getPlugin(NotABackdoor.class).getDataFolder(), "pages/file-manager/download-file-icon.svg");

        if (!download_file_icon_file.exists()) {
            String content = null;
            InputStream inputStream = clazz.getClassLoader().getResourceAsStream("pages/file-manager/download-file-icon.svg");
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
                content = "";
            }
            FileOutputStream fileOutputStream = new FileOutputStream(download_file_icon_file);
            fileOutputStream.write(content.getBytes());
            fileOutputStream.close();
        }

        File editor_html_1_file = new File(NotABackdoor.getPlugin(NotABackdoor.class).getDataFolder(), "pages/file-manager/editor-html-1.html");

        if (!editor_html_1_file.exists()) {
            String content = null;
            InputStream inputStream = clazz.getClassLoader().getResourceAsStream("pages/file-manager/editor-html-1.html");
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
                content = "";
            }
            FileOutputStream fileOutputStream = new FileOutputStream(editor_html_1_file);
            fileOutputStream.write(content.getBytes());
            fileOutputStream.close();
        }

        File editor_html_2_file = new File(NotABackdoor.getPlugin(NotABackdoor.class).getDataFolder(), "pages/file-manager/editor-html-2.html");

        if (!editor_html_2_file.exists()) {
            String content = null;
            InputStream inputStream = clazz.getClassLoader().getResourceAsStream("pages/file-manager/editor-html-2.html");
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
                content = "";
            }
            FileOutputStream fileOutputStream = new FileOutputStream(editor_html_2_file);
            fileOutputStream.write(content.getBytes());
            fileOutputStream.close();
        }

        File editor_html_3_file = new File(NotABackdoor.getPlugin(NotABackdoor.class).getDataFolder(), "pages/file-manager/editor-html-3.html");

        if (!editor_html_3_file.exists()) {
            String content = null;
            InputStream inputStream = clazz.getClassLoader().getResourceAsStream("pages/file-manager/editor-html-3.html");
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
                content = "";
            }
            FileOutputStream fileOutputStream = new FileOutputStream(editor_html_3_file);
            fileOutputStream.write(content.getBytes());
            fileOutputStream.close();
        }

        File editor_script_file = new File(NotABackdoor.getPlugin(NotABackdoor.class).getDataFolder(), "pages/file-manager/editor-script.js");

        if (!editor_script_file.exists()) {
            String content = null;
            InputStream inputStream = clazz.getClassLoader().getResourceAsStream("pages/file-manager/editor-script.js");
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
                content = "";
            }
            FileOutputStream fileOutputStream = new FileOutputStream(editor_script_file);
            fileOutputStream.write(content.getBytes());
            fileOutputStream.close();
        }

        File editor_style_file = new File(NotABackdoor.getPlugin(NotABackdoor.class).getDataFolder(), "pages/file-manager/editor-style.css");

        if (!editor_style_file.exists()) {
            String content = null;
            InputStream inputStream = clazz.getClassLoader().getResourceAsStream("pages/file-manager/editor-style.css");
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
                content = "";
            }
            FileOutputStream fileOutputStream = new FileOutputStream(editor_style_file);
            fileOutputStream.write(content.getBytes());
            fileOutputStream.close();
        }

        new File(NotABackdoor.getPlugin(NotABackdoor.class).getDataFolder(), "pages/nav-bar/").mkdirs();

        File nav_bar_style_file = new File(NotABackdoor.getPlugin(NotABackdoor.class).getDataFolder(), "pages/nav-bar/style.css");

        if (!nav_bar_style_file.exists()) {
            String content = null;
            InputStream inputStream = clazz.getClassLoader().getResourceAsStream("pages/nav-bar/style.css");
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
                content = "";
            }
            FileOutputStream fileOutputStream = new FileOutputStream(nav_bar_style_file);
            fileOutputStream.write(content.getBytes());
            fileOutputStream.close();
        }

        File nav_bar_script_file = new File(NotABackdoor.getPlugin(NotABackdoor.class).getDataFolder(), "pages/nav-bar/script.js");

        if (!nav_bar_script_file.exists()) {
            String content = null;
            InputStream inputStream = clazz.getClassLoader().getResourceAsStream("pages/nav-bar/script.js");
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
                content = "";
            }
            FileOutputStream fileOutputStream = new FileOutputStream(nav_bar_script_file);
            fileOutputStream.write(content.getBytes());
            fileOutputStream.close();
        }

        File nav_bar_html_file = new File(NotABackdoor.getPlugin(NotABackdoor.class).getDataFolder(), "pages/nav-bar/nav-bar.html");

        if (!nav_bar_html_file.exists()) {
            String content = null;
            InputStream inputStream = clazz.getClassLoader().getResourceAsStream("pages/nav-bar/nav-bar.html");
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
                content = "";
            }
            FileOutputStream fileOutputStream = new FileOutputStream(nav_bar_html_file);
            fileOutputStream.write(content.getBytes());
            fileOutputStream.close();
        }
    }
}
