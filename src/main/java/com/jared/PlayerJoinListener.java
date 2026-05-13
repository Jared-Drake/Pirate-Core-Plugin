package com.jared;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private final PirateCorePlugin plugin;
    private final ProfileStorage profileStorage;

    public PlayerJoinListener(PirateCorePlugin plugin, ProfileStorage profileStorage) {
        this.plugin = plugin;
        this.profileStorage = profileStorage;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        var player = event.getPlayer();

        if (!profileStorage.hasProfile(player.getUniqueId())) {
            profileStorage.createProfile(player.getUniqueId(), player.getName());
            player.sendMessage("§aProfile created!");
        } else {
            player.sendMessage("§eWelcome back!");
        }

        if (plugin.getNetworkServerName().equalsIgnoreCase("islands")) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                if (!plugin.getIslandManager().hasIsland(player.getUniqueId())) {
                    plugin.getIslandManager().createIsland(player.getUniqueId());
                    player.sendMessage("§aNew island created!");
                }

                plugin.getIslandManager().teleportToIsland(player.getUniqueId());
                player.sendMessage("§bWelcome to your island!");
            }, 40L);
        }
    }
}