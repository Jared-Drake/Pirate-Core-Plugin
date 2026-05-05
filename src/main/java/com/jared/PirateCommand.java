package com.jared;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PirateCommand implements CommandExecutor {

    private final ProfileStorage profileStorage;
    private final PirateCorePlugin plugin;

    public PirateCommand(ProfileStorage profileStorage, PirateCorePlugin plugin) {
        this.profileStorage = profileStorage;
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("Players only!");
            return true;
        }

        String server = plugin.getNetworkServerName();

        if (args.length == 0 || args[0].equalsIgnoreCase("profile")) {
            showProfile(player);
            return true;
        }

        if (args[0].equalsIgnoreCase("island") || args[0].equalsIgnoreCase("islands")) {
            sendToServer(player, "islands");
            player.sendMessage("§bSending you to the islands...");
            return true;
        }

        if (args[0].equalsIgnoreCase("island") || args[0].equalsIgnoreCase("islands")) {

            if (!server.equalsIgnoreCase("islands")) {
                player.sendMessage("§bSending you to your island...");
                sendToServer(player, "islands");
                return true;
            }

            if (!plugin.getIslandManager().hasIsland(player.getUniqueId())) {
                plugin.getIslandManager().createIsland(player.getUniqueId());
                player.sendMessage("§aNew island created!");
            }

            plugin.getIslandManager().teleportToIsland(player.getUniqueId());
            return true;
        }

        if (args[0].equalsIgnoreCase("create")) {

            if (!server.equalsIgnoreCase("islands")) {
                player.sendMessage("§cYou must be on the islands server to create an island!");
                return true;
            }

            if (plugin.getIslandManager().hasIsland(player.getUniqueId())) {
                player.sendMessage("§cYou already have an island!");
                return true;
            }

            plugin.getIslandManager().createIsland(player.getUniqueId());
            player.sendMessage("§aIsland created!");
            return true;
        }

        if (args[0].equalsIgnoreCase("home")) {

            if (!server.equalsIgnoreCase("islands")) {
                player.sendMessage("§bSending you to your island...");
                sendToServer(player, "islands");
                return true;
            }

            if (!plugin.getIslandManager().hasIsland(player.getUniqueId())) {
                player.sendMessage("§cYou don't have an island!");
                return true;
            }

            plugin.getIslandManager().teleportToIsland(player.getUniqueId());
            return true;
        }

        player.sendMessage("§cUsage: /pirate profile, /pirate island, /pirate lobby");
        return true;


    }

    private void showProfile(Player player) {
        var profile = profileStorage.getProfile(player.getUniqueId());

        if (profile == null) {
            player.sendMessage("§cNo profile found.");
            return;
        }

        player.sendMessage("§6=== Your Pirate Profile ===");
        player.sendMessage("§eGold: §f" + profile.getGold());
        player.sendMessage("§eXP: §f" + profile.getXp());
        player.sendMessage("§eLevel: §f" + profile.getLevel());
    }

    private void sendToServer(Player player, String serverName) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(serverName);

        player.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
    }
}