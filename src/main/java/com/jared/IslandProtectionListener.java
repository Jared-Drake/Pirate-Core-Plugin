package com.jared;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class IslandProtectionListener implements Listener {

    private final IslandManager islandManager;

    public IslandProtectionListener(IslandManager islandManager) {
        this.islandManager = islandManager;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (!islandManager.canBuild(event.getPlayer(), event.getBlock().getLocation())) {
            event.setCancelled(true);
            event.getPlayer().sendMessage("§cYou cannot break blocks here!");
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (!islandManager.canBuild(event.getPlayer(), event.getBlock().getLocation())) {
            event.setCancelled(true);
            event.getPlayer().sendMessage("§cYou cannot place blocks here!");
        }
    }
}