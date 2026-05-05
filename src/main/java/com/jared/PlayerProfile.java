package com.jared;

import java.util.UUID;

public class PlayerProfile {

    private final UUID uuid;
    private String username;
    private int gold;
    private int xp;
    private int level;

    public PlayerProfile(UUID uuid, String username) {
        this.uuid = uuid;
        this.username = username;
        this.gold = 0;
        this.xp = 0;
        this.level = 1;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getUsername() {
        return username;
    }

    public int getGold() {
        return gold;
    }

    public int getXp() {
        return xp;
    }

    public int getLevel() {
        return level;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void addGold(int amount) {
        this.gold += amount;
    }

    public void addXp(int amount) {
        this.xp += amount;

        while (this.xp >= getXpNeededForNextLevel()) {
            this.xp -= getXpNeededForNextLevel();
            this.level++;
        }
    }

    private int getXpNeededForNextLevel() {
        return level * 100;
    }
}