package com.jared;

import com.jared.PirateCommand;
import com.jared.PlayerJoinListener;
import com.jared.ProfileStorage;
import org.bukkit.plugin.java.JavaPlugin;

public class PirateCorePlugin extends JavaPlugin {

    private ProfileStorage profileStorage;

    @Override
    public void onEnable() {
        this.profileStorage = new ProfileStorage(this);
        profileStorage.load();

        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        getServer().getPluginManager().registerEvents(
                new PlayerJoinListener(profileStorage),
                this
        );

        getCommand("pirate").setExecutor(
                new PirateCommand(profileStorage, this)
        );

        getLogger().info("PirateCore enabled!");
    }

    @Override
    public void onDisable() {
        if (profileStorage != null) {
            profileStorage.save();
        }

        getLogger().info("PirateCore disabled!");
    }

    public ProfileStorage getProfileStorage() {
        return profileStorage;
    }
}