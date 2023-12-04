package com.github.newk5.vf.server.core.entities;

public enum GameEntityType {
    UNDEFINED(0),
    PLAYER(1),
    VEHICLE(2),
    NPC(3),
    OBJECT(4),
    ZONE(5);

    public final Integer value;

    GameEntityType(int v) {
        this.value = v;
    }

    public static GameEntityType value(int v) {
        for (GameEntityType e : values()) {
            if (e.value == v) {
                return e;
            }
        }
        return null;
    }
}