package com.jared;

import com.jared.PirateCommand;
import com.jared.PlayerJoinListener;
import com.jared.ProfileStorage;
import org.bukkit.plugin.java.JavaPlugin;

public class PirateCorePlugin extends JavaPlugin {

    private ProfileStorage profileStorage;
    private IslandManager islandManager;

    public String getServerName() {
        return getServer().getName();
    }

    public String getNetworkServerName() {
        return getConfig().getString("server-name", "unknown");
    }

    @Override
    public void onEnable() {
        this.profileStorage = new ProfileStorage(this);
        profileStorage.load();
        this.islandManager = new IslandManager(this);
        islandManager.load();
        saveDefaultConfig();

        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        getServer().getPluginManager().registerEvents(
                new PlayerJoinListener(this, profileStorage), this
        );

        getCommand("pirate").setExecutor(
                new PirateCommand(profileStorage, this)
        );

        getServer().getPluginManager().registerEvents(
                new IslandProtectionListener(islandManager),
                this
        );


        getLogger().info("PirateCore enabled!");
    }

    public IslandManager getIslandManager() {
        return islandManager;
    }

    @Override
    public void onDisable() {

        islandManager.save();

        if (profileStorage != null) {
            profileStorage.save();
        }
        getLogger().info("PirateCore disabled!");
    }


    public ProfileStorage getProfileStorage() {
        return profileStorage;
    }
}