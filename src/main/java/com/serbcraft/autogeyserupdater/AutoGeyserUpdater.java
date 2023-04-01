package com.serbcraft.autogeyserupdater;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.plugin.*;

import java.io.*;
import java.net.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AutoGeyserUpdater extends Plugin {
    private static final String GEYSER_URL = "https://ci.opencollab.dev/job/GeyserMC/job/Geyser/job/master/lastSuccessfulBuild/artifact/bootstrap/bungeecord/build/libs/Geyser-BungeeCord.jar";
    private static final String GEYSER_FILE = "plugins/Geyser-BungeeCord.jar";

    @Override
    public void onEnable() {
        getLogger().info(ChatColor.translateAlternateColorCodes('&',
                "&4\n" +
                "▒█▀▀▀█ ▒█▀▀▀ ▒█▀▀█ ▒█▀▀█ ░░ ▒█▀▀█ ▒█▀▀█ ░█▀▀█ ▒█▀▀▀ ▀▀█▀▀ \n" +
                "░▀▀▀▄▄ ▒█▀▀▀ ▒█▄▄▀ ▒█▀▀▄ ▀▀ ▒█░░░ ▒█▄▄▀ ▒█▄▄█ ▒█▀▀▀ ░▒█░░ \n" +
                "▒█▄▄▄█ ▒█▄▄▄ ▒█░▒█ ▒█▄▄█ ░░ ▒█▄▄█ ▒█░▒█ ▒█░▒█ ▒█░░░ ░▒█░░ \n" +
                "\n" +
                "░█▀▀█ ── ─█▀▀█ ░█─░█ ▀▀█▀▀ ░█▀▀▀█ ░█─░█ ░█▀▀█ ░█▀▀▄ ─█▀▀█ ▀▀█▀▀ ░█▀▀▀ \n" +
                "░█─▄▄ ▀▀ ░█▄▄█ ░█─░█ ─░█── ░█──░█ ░█─░█ ░█▄▄█ ░█─░█ ░█▄▄█ ─░█── ░█▀▀▀ \n" +
                "░█▄▄█ ── ░█─░█ ─▀▄▄▀ ─░█── ░█▄▄▄█ ─▀▄▄▀ ░█─── ░█▄▄▀ ░█─░█ ─░█── ░█▄▄▄"));
        try {
            checkForUpdate();
        } catch (IOException e) {
            getLogger().warning(ChatColor.translateAlternateColorCodes('&',"&4Failed to check for Geyser update: " + e.getMessage()));
        }
    }

    @Override
    public void onDisable() {
        getLogger().info(ChatColor.translateAlternateColorCodes('&', "&4Disabling SERB-CRAFT Geyser Auto Updater!"));
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
                DateFormat f = new SimpleDateFormat("dd.MM.yy");
                Calendar obj = Calendar.getInstance();
                localFile.renameTo(new File(GEYSER_FILE + "#" + f.format(obj.getTime())));
                getLogger().info(ChatColor.translateAlternateColorCodes('&', "&aGeyser update available, downloading..."));
                downloadGeyser(url);
            } else {
                getLogger().info(ChatColor.translateAlternateColorCodes('&', "&6Geyser is up to date!"));
            }
        } else {
            getLogger().info(ChatColor.translateAlternateColorCodes('&', "&aGeyser not found, downloading..."));
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
        getLogger().info(ChatColor.translateAlternateColorCodes('&', "&aGeyser downloaded successfully."));
    }
}
