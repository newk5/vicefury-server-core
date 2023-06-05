package com.github.newk5.vf.server.core.entities;

public enum NPCAction {
    NONE(0), 
    ATTACKING(1),
    INVESTIGATING(2),
    MOVING_TO_TARGET(3),
    MOVING_TO_LOCATION(4);

    public final Integer value;

    NPCAction(int v) {
        this.value = v;
    }

    public static NPCAction value(int v) {
        for (NPCAction e : values()) {
            if (e.value == v) {
                return e;
            }
        }
        return null;
    }
}