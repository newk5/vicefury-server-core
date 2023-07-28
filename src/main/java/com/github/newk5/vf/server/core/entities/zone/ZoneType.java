package com.github.newk5.vf.server.core.entities.zone;

public enum ZoneType {
    BLOCKING(1),
    MESH(2),
    SPHERE(3);

    public final Integer value;

    ZoneType(int v) {
        this.value = v;
    }

    public static ZoneType value(int v) {
        for (ZoneType e : values()) {
            if (e.value == v) {
                return e;
            }
        }
        return null;
    }
}