package com.github.newk5.vf.server.core.entities.npc;

public enum NPCType {
    ZOMBIE(1),
    SHOOTER(2);

    public final Integer value;

    NPCType(int v) {
        this.value = v;
    }

    public static NPCType value(int v) {
        for (NPCType e : values()) {
            if (e.value == v) {
                return e;
            }
        }
        return null;
    }
}
