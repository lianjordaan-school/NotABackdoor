package com.lian.notabackdoor.pages;

import com.lian.notabackdoor.NotABackdoor;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.logging.Level;

public class FileManagerPageHandler implements HttpHandler {
    private static final String PAGE_PATH = "/filemanager";

    public void handle(HttpExchange exchange) throws IOException {
        String requestedPath = exchange.getRequestURI().getPath();
        if (!requestedPath.startsWith(PAGE_PATH + "/")) {
            if (!requestedPath.equals(PAGE_PATH)) {
                NotABackdoor.redirectToLostPage(exchange);

                return;
            }
        }

        new File(NotABackdoor.getPlugin(NotABackdoor.class).getDataFolder(), "pages/file-manager/").mkdirs();
        File pre_html_file = new File(NotABackdoor.getPlugin(NotABackdoor.class).getDataFolder(), "pages/file-manager/pre-html.html");

        if (!pre_html_file.exists()) {
            String content = null;
            // Create the lost page file if it doesn't exist
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("pages/file-manager/pre-html.html");
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
            // Create the lost page file if it doesn't exist
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("pages/file-manager/script.js");
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
            // Create the lost page file if it doesn't exist
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("pages/file-manager/style.css");
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
            // Create the lost page file if it doesn't exist
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("pages/file-manager/folder-icon.svg");
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
            // Create the lost page file if it doesn't exist
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("pages/file-manager/file-icon.svg");
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

        File editor_html_1_file = new File(NotABackdoor.getPlugin(NotABackdoor.class).getDataFolder(), "pages/file-manager/editor-html-1.html");

        if (!editor_html_1_file.exists()) {
            String content = null;
            // Create the lost page file if it doesn't exist
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("pages/file-manager/editor-html-1.html");
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
            // Create the lost page file if it doesn't exist
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("pages/file-manager/editor-html-2.html");
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
            // Create the lost page file if it doesn't exist
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("pages/file-manager/editor-html-3.html");
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
            // Create the lost page file if it doesn't exist
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("pages/file-manager/editor-script.js");
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
            // Create the lost page file if it doesn't exist
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("pages/file-manager/editor-style.css");
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

        String response = "<html><body style='background-color: #1e182e; font-family: ui-sans-serif, system-ui, -apple-system, BlinkMacSystemFont, \"Segoe UI\", Roboto, \"Helvetica Neue\", Arial, \"Noto Sans\", sans-serif, \"Apple Color Emoji\", \"Segoe UI Emoji\", \"Segoe UI Symbol\", \"Noto Color Emoji\"; letter-spacing: 0.015em;'>" + new String(Files.readAllBytes(pre_html_file.toPath())) + "<div style='display: flex; flex-direction: column;'>";

        String path = requestedPath.replaceFirst("^/filemanager(/.*)?$", "$1");

        if (path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        } else {
            exchange.getResponseHeaders().add("Location", requestedPath + "/");
            exchange.sendResponseHeaders(302, -1);
        }
        path = "." + path;

        File serverDir = new File(path);

        File[] files = serverDir.listFiles();
        if (files != null) {
            Arrays.sort(files, (f1, f2) -> {
                if (f1.isDirectory() && !f2.isDirectory()) {
                    return -1;
                } else if (!f1.isDirectory() && f2.isDirectory()) {
                    return 1;
                } else {
                    return f1.getName().compareToIgnoreCase(f2.getName());
                }
            });
            for (File file : files) {
                response += "<div class=\"file\" style='padding: 10px; margin: 5px; border-radius: 5px; background-color: #262339; transition: background-color 0.3s, transform 0.2s;'>";
                if (file.isDirectory()) {
                    response += new String(Files.readAllBytes(folder_icon_file.toPath()));
                } else {
                    response += new String(Files.readAllBytes(file_icon_file.toPath()));
                }

                response += "<a style='text-decoration: none; color: #c996cc; cursor: pointer;' href='./" + file.getName() + "/'>" + file.getName() + "</a></div>";
            }

            response += "</div></body><style>" + new String(Files.readAllBytes(style_file.toPath())) + "</style><script>" + new String(Files.readAllBytes(script_file.toPath())) + "</script></html>";

            exchange.sendResponseHeaders(200, response.length());
            OutputStream out = exchange.getResponseBody();
            out.write(response.getBytes());
            out.close();
        } else {
            if (serverDir.exists()){

                response = new String(Files.readAllBytes(editor_html_1_file.toPath()));
                response += serverDir.getName();
                response += new String(Files.readAllBytes(editor_html_2_file.toPath()));
                response += new String(Files.readAllBytes(serverDir.toPath()));
                response += new String(Files.readAllBytes(editor_html_3_file.toPath()));
                response += "<style>" + new String(Files.readAllBytes(editor_style_file.toPath())) + "</style>";
                response += "<script>" + new String(Files.readAllBytes(editor_script_file.toPath())) + "</script>";

                exchange.sendResponseHeaders(200, response.length());
                OutputStream out = exchange.getResponseBody();
                out.write(response.getBytes());
                out.close();
            } else {
                NotABackdoor.redirectToLostPage(exchange);
            }
        }

    }
}
