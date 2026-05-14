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

        if (args[0].equalsIgnoreCase("sethome")) {
            if (!server.equalsIgnoreCase("islands")) {
                player.sendMessage("§cYou must be on the islands server to set your island home!");
                return true;
            }

            if (!plugin.getIslandManager().hasIsland(player.getUniqueId())) {
                player.sendMessage("§cYou need to create an island first!");
                return true;
            }

            boolean success = plugin.getIslandManager().setIslandHome(player);

            if (!success) {
                player.sendMessage("§cYou can only set your home inside your own island!");
                return true;
            }

            player.sendMessage("§aIsland home updated!");
            return true;
        }

        if (args[0].equalsIgnoreCase("trust")) {
            if (!server.equalsIgnoreCase("islands")) {
                player.sendMessage("§cYou must be on the islands server to trust players!");
                return true;
            }

            if (args.length < 2) {
                player.sendMessage("§cUsage: /pirate trust <player>");
                return true;
            }

            if (!plugin.getIslandManager().hasIsland(player.getUniqueId())) {
                player.sendMessage("§cYou need an island first!");
                return true;
            }

            Player target = plugin.getServer().getPlayerExact(args[1]);

            if (target == null) {
                player.sendMessage("§cThat player must be online to trust them for now.");
                return true;
            }

            if (target.getUniqueId().equals(player.getUniqueId())) {
                player.sendMessage("§cYou already own this island.");
                return true;
            }

            plugin.getIslandManager().trustPlayer(player.getUniqueId(), target.getUniqueId());
            player.sendMessage("§aTrusted " + target.getName() + " on your island!");
            target.sendMessage("§aYou were trusted on " + player.getName() + "'s island!");
            return true;
        }

        if (args[0].equalsIgnoreCase("untrust")) {
            if (!server.equalsIgnoreCase("islands")) {
                player.sendMessage("§cYou must be on the islands server to untrust players!");
                return true;
            }

            if (args.length < 2) {
                player.sendMessage("§cUsage: /pirate untrust <player>");
                return true;
            }

            if (!plugin.getIslandManager().hasIsland(player.getUniqueId())) {
                player.sendMessage("§cYou need an island first!");
                return true;
            }

            Player target = plugin.getServer().getPlayerExact(args[1]);

            if (target == null) {
                player.sendMessage("§cThat player must be online to untrust them for now.");
                return true;
            }

            plugin.getIslandManager().untrustPlayer(player.getUniqueId(), target.getUniqueId());
            player.sendMessage("§eUntrusted " + target.getName() + " from your island.");
            target.sendMessage("§cYou were untrusted from " + player.getName() + "'s island.");
            return true;
        }

        player.sendMessage("§cUsage: /pirate profile, /pirate island, /pirate home, /pirate sethome, /pirate trust <player>, /pirate untrust <player>");
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