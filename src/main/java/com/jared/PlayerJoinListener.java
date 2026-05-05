package com.jared;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private final ProfileStorage profileStorage;

    public PlayerJoinListener(ProfileStorage profileStorage) {
        this.profileStorage = profileStorage;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        var player = event.getPlayer();

        if (!profileStorage.hasProfile(player.getUniqueId())) {
            profileStorage.createProfile(
                    player.getUniqueId(),
                    player.getName()
            );
            player.sendMessage("§aProfile created!");
        } else {
            player.sendMessage("§eWelcome back!");
        }
    }
}