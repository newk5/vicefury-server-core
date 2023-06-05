package com.github.newk5.vf.server.core.events.damage;

public enum DamageSource {
    UNDEFINED(-1),
    WEAPON(0),
    VEHICLE(1);

    public final Integer value;

    DamageSource(Integer v) {
        this.value = v;
    }

    public static DamageSource value(int v) {
        for (DamageSource ds : values()) {
            if (ds.value == v) {
                return ds;
            }
        }
        return null;
    }
}