package com.github.newk5.vf.server.core.entities;

public enum AttachResult {
    SUCCESS(0, "Success"),
    ERROR_FAILEDTOFINDSOCKET(1, "Failed to find socket %s"),
    ERROR_FAILEDTOFINDSOURCEENTITY(2, "Failed to find source entity %s"),
    ERROR_FAILEDTOFINDTARGETENTITY(3, "Failed to find target entity %s"),
    ERROR_INVALIDENTITYTYPE(4, "Invalid entity type %s"),
    ERROR_CANNOTATTACH(5, "Cannot attach %s"),
    ERROR_ALREADYATTACHED(6,"Object already attached to %s");

    private final int code;
    private final String description;

    AttachResult(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public static AttachResult value(int v) {
        for (AttachResult e : values()) {
            if (e.getCode() == v) {
                return e;
            }
        }
        return null;
    }

    public String getDescription() {
        return description;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return code + ": " + description;
    }
}
