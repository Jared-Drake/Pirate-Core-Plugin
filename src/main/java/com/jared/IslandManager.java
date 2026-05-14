package com.jared;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.UUID;

public class IslandManager {

    private final PirateCorePlugin plugin;
    private final HashMap<UUID, Island> islands = new HashMap<>();
    private final File file;
    private final Gson gson = new Gson();

    private int currentIndex = 0;
    private final int spacing = 5000;

    public IslandManager(PirateCorePlugin plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "islands.json");
    }

    public void load() {
        try {
            if (!file.exists()) {
                plugin.getDataFolder().mkdirs();
                file.createNewFile();
                return;
            }

            FileReader reader = new FileReader(file);
            Type type = new TypeToken<HashMap<UUID, Island>>(){}.getType();
            HashMap<UUID, Island> data = gson.fromJson(reader, type);

            if (data != null) {
                islands.putAll(data);
                currentIndex = islands.size();
            }

            reader.close();
            plugin.getLogger().info("Islands loaded.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            FileWriter writer = new FileWriter(file);
            gson.toJson(islands, writer);
            writer.flush();
            writer.close();

            plugin.getLogger().info("Islands saved.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean hasIsland(UUID uuid) {
        return islands.containsKey(uuid);
    }

    public Island createIsland(UUID uuid) {
        int x = (currentIndex % 10) * spacing;
        int z = (currentIndex / 10) * spacing;

        Island island = new Island(uuid, x, z);
        islands.put(uuid, island);
        currentIndex++;

        generateIsland(island);
        save();

        return island;
    }

    public void teleportToIsland(UUID uuid) {
        Island island = islands.get(uuid);
        if (island == null) return;

        World world = Bukkit.getWorld("world");
        if (world == null) return;

        int x = (int) Math.floor(island.getHomeX());
        int z = (int) Math.floor(island.getHomeZ());

        int safeY = world.getHighestBlockYAt(x, z) + 1;

        Location loc = new Location(
                world,
                x + 0.5,
                safeY,
                z + 0.5,
                island.getHomeYaw(),
                island.getHomePitch()
        );

        var player = Bukkit.getPlayer(uuid);
        if (player != null) {
            player.teleport(loc);
            player.setFallDistance(0);
        }
    }

    public boolean setIslandHome(org.bukkit.entity.Player player) {
        Island island = islands.get(player.getUniqueId());

        if (island == null) {
            return false;
        }

        if (!canBuild(player, player.getLocation())) {
            return false;
        }

        Location loc = player.getLocation();

        island.setHome(
                loc.getX(),
                loc.getY(),
                loc.getZ(),
                loc.getYaw(),
                loc.getPitch()
        );

        save();
        return true;
    }


    private void generateIsland(Island island) {
        World world = Bukkit.getWorld("world");
        if (world == null) return;

        int baseY = 64;

        for (int x = -5; x <= 5; x++) {
            for (int z = -5; z <= 5; z++) {
                world.getBlockAt(island.getX() + x, baseY, island.getZ() + z)
                        .setType(Material.GRASS_BLOCK);
            }
        }
    }

    public boolean canBuild(org.bukkit.entity.Player player, org.bukkit.Location location) {
        Island island = getIslandAt(location);

        if (island == null) {
            return false;
        }

        return island.getOwner().equals(player.getUniqueId());
    }

    public Island getIslandAt(org.bukkit.Location location) {
        for (Island island : islands.values()) {
            int radius = 250;

            boolean withinX = Math.abs(location.getBlockX() - island.getX()) <= radius;
            boolean withinZ = Math.abs(location.getBlockZ() - island.getZ()) <= radius;

            if (withinX && withinZ) {
                return island;
            }
        }

        return null;
    }
}