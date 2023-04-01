package com.autogeyserupdater;

import net.md_5.bungee.api.plugin.*;

import java.io.*;
import java.net.*;

public class AutoGeyserUpdater extends Plugin {
    private static final String GEYSER_URL = "https://ci.opencollab.dev/job/GeyserMC/job/Geyser/job/master/lastSuccessfulBuild/artifact/bootstrap/bungee/target/Geyser-BungeeCord.jar";
    private static final String GEYSER_FILE = "plugins/Geyser-BungeeCord.jar";

    @Override
    public void onEnable() {
        getLogger().info("GeyserUpdater has been enabled.");
        try {
            checkForUpdate();
        } catch (IOException e) {
            getLogger().warning("Failed to check for Geyser update: " + e.getMessage());
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("GeyserUpdater has been disabled.");
    }

    private void checkForUpdate() throws IOException {
        URL url = new URL(GEYSER_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("HEAD");
        long remoteTimestamp = connection.getLastModified();

        File localFile = new File(GEYSER_FILE);
        if (localFile.exists()) {
            long localTimestamp = localFile.lastModified();
            if (remoteTimestamp > localTimestamp) {
                getLogger().info("Geyser update available, downloading...");
                downloadGeyser(url);
            } else {
                getLogger().info("Geyser is up to date.");
            }
        } else {
            getLogger().info("Geyser not found, downloading...");
            downloadGeyser(url);
        }
    }

    private void downloadGeyser(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        try (InputStream in = connection.getInputStream();
             FileOutputStream out = new FileOutputStream(GEYSER_FILE)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }
        getLogger().info("Geyser downloaded successfully.");
    }
}
