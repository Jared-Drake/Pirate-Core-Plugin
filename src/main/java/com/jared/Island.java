package com.jared;

import java.util.UUID;

public class Island {

    private final UUID owner;
    private final int x;
    private final int z;

    private double homeX;
    private double homeY;
    private double homeZ;
    private float homeYaw;
    private float homePitch;

    public Island(UUID owner, int x, int z) {
        this.owner = owner;
        this.x = x;
        this.z = z;

        this.homeX = x + 0.5;
        this.homeY = 65;
        this.homeZ = z + 0.5;
        this.homeYaw = 0;
        this.homePitch = 0;
    }

    public UUID getOwner() {
        return owner;
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    public double getHomeX() {
        return homeX;
    }

    public double getHomeY() {
        return homeY;
    }

    public double getHomeZ() {
        return homeZ;
    }

    public float getHomeYaw() {
        return homeYaw;
    }

    public float getHomePitch() {
        return homePitch;
    }

    public void setHome(double homeX, double homeY, double homeZ, float homeYaw, float homePitch) {
        this.homeX = homeX;
        this.homeY = homeY;
        this.homeZ = homeZ;
        this.homeYaw = homeYaw;
        this.homePitch = homePitch;
    }
}