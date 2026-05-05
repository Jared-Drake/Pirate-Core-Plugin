package com.jared;

import java.util.HashMap;
import java.util.UUID;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.io.*;

public class ProfileStorage {

    private final PirateCorePlugin plugin;
    private final HashMap<UUID, PlayerProfile> profiles = new HashMap<>();
    private final File file;
    private final Gson gson = new Gson();

    public ProfileStorage(PirateCorePlugin plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "profiles.json");
    }

    public void load() {
        try {
            if (!file.exists()) {
                plugin.getDataFolder().mkdirs();
                file.createNewFile();
                return;
            }

            FileReader reader = new FileReader(file);
            Type type = new TypeToken<HashMap<UUID, PlayerProfile>>(){}.getType();
            HashMap<UUID, PlayerProfile> data = gson.fromJson(reader, type);

            if (data != null) {
                profiles.putAll(data);
            }

            reader.close();
            plugin.getLogger().info("Profiles loaded.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            FileWriter writer = new FileWriter(file);
            gson.toJson(profiles, writer);
            writer.flush();
            writer.close();

            plugin.getLogger().info("Profiles saved.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public PlayerProfile getProfile(UUID uuid) {
        return profiles.get(uuid);
    }

    public PlayerProfile createProfile(UUID uuid, String username) {
        PlayerProfile profile = new PlayerProfile(uuid, username);
        profiles.put(uuid, profile);
        return profile;
    }

    public boolean hasProfile(UUID uuid) {
        return profiles.containsKey(uuid);
    }
}