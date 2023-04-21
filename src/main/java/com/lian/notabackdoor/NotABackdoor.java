package com.lian.notabackdoor;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.lian.notabackdoor.api.DownloadHandler;
import com.lian.notabackdoor.api.SaveFileHandler;
import com.lian.notabackdoor.api.SetPasswordHandler;
import com.lian.notabackdoor.pages.*;
import com.sun.net.httpserver.HttpServer;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class NotABackdoor extends JavaPlugin {
    private HttpServer server;
    public String pluginName = getName();
    public Integer port;
    public Integer backlog;
    public String ip;
    public Integer passLength;
    public String protocol;
    public Boolean clearFiles;
    public String setPassword;
    public Boolean sendPasswordLink;
    public List<String> allowedFileExtensions;

    public void onEnable() {
        saveDefaultConfig();
        FileConfiguration config = getConfig();
        port = config.getInt("port");
        backlog = config.getInt("backlog");
        ip = config.getString("ip");
        passLength = config.getInt("passLength");
        protocol = config.getString("protocol");
        clearFiles = config.getBoolean("clearFiles");
        setPassword = config.getString("setPassword");
        sendPasswordLink = config.getBoolean("sendPasswordLink");
        allowedFileExtensions = config.getStringList("allowedFileExtensions");

        InetSocketAddress address = new InetSocketAddress(port);

        if (clearFiles) {
            Path directoryPath = Paths.get(getDataFolder() + "/pages");
            try {
                Files.walk(directoryPath)
                        .sorted(java.util.Comparator.reverseOrder())
                        .map(Path::toFile)
                        .forEach(File::delete);
            } catch (IOException ignored) {

            }
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
        server.createContext("/api/save-file/", new SaveFileHandler());
        server.createContext("/api/set-password/", new SetPasswordHandler());
        server.createContext("/error", new ErrorPageHandler());
        server.createContext("/password", new PasswordPageHandler());
        server.setExecutor(null); // Use the default executor
        server.start();

        getLogger().info("Web server started on port " + port);
        Utils.setNewPassword();
        try {
            Utils.generateFiles();
        } catch (IOException ignored) {

        }
    }

    public void onDisable() {
        if (server != null) {
            server.stop(0); // Stop the server with no delay
        }
    }
}

