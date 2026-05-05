package com.jared;

import java.util.UUID;

public class Island {

    private final UUID owner;
    private final int x;
    private final int z;

    public Island(UUID owner, int x, int z) {
        this.owner = owner;
        this.x = x;
        this.z =z;
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
}
