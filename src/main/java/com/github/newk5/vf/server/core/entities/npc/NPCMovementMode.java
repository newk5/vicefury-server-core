package com.github.newk5.vf.server.core.entities.npc;

public enum NPCMovementMode {
    WALKING(1),
    RUNNING(2);

    public final Integer value;

    NPCMovementMode(int v) {
        this.value = v;
    }

    public static NPCMovementMode value(int v) {
        for (NPCMovementMode e : values()) {
            if (e.value == v) {
                return e;
            }
        }
        return null;
    }
}
