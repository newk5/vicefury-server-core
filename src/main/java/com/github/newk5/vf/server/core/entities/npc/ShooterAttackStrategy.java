package com.github.newk5.vf.server.core.entities.npc;

public enum ShooterAttackStrategy {
    STATIC(0),
    MOVEABLE(1),
    EVASIVE(2);

    public final Integer value;

    ShooterAttackStrategy(int v) {
        this.value = v;
    }

    public static ShooterAttackStrategy value(int v) {
        for (ShooterAttackStrategy e : values()) {
            if (e.value == v) {
                return e;
            }
        }
        return null;
    }
}
