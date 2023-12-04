package com.github.newk5.vf.server.core.entities.gameobject;

import com.github.newk5.vf.server.core.entities.AttachResult;
import com.github.newk5.vf.server.core.InternalServerEvents;
import com.github.newk5.vf.server.core.entities.GameEntity;
import com.github.newk5.vf.server.core.entities.GameEntityType;
import com.github.newk5.vf.server.core.entities.Rotation;
import com.github.newk5.vf.server.core.entities.Transform;
import com.github.newk5.vf.server.core.entities.Vector;
import static com.github.newk5.vf.server.core.entities.AttachResult.ERROR_CANNOTATTACH;
import static com.github.newk5.vf.server.core.entities.AttachResult.ERROR_INVALIDENTITYTYPE;
import com.github.newk5.vf.server.core.entities.DamageableEntity;
import com.github.newk5.vf.server.core.utils.Log;

public class GameObject extends DamageableEntity {

    public static int SMALL_RED_FLAG = 1;
    public static int SMALL_BLUE_FLAG = 2;
    public static int BLUE_FLAG = 3;
    public static int RED_FLAG = 4;
    public static int WOODEN_FENCE = 5;
    public static int RAMP1 = 6;
    public static int WOODEN_WALL1 = 7;
    public static int RED_ARROW = 8;
    public static int BLUE_ARROW = 9;

    private GameObject(int id) {
        super();
        this.id = id;
        this.type = GameEntityType.OBJECT;

    }

    private native boolean nativeIsOverlapping(int id, int entityType, int entityId);

    public boolean isOverlapping(GameEntity ent) {
        if (isOnMainThread()) {
            return nativeIsOverlapping(id, ent.type.value, ent.getId());
        }
        return false;
    }

    private native void nativeSetOverlapEventsEnabled(int id, boolean Value);

    public GameObject setOverlapEventsEnabled(boolean Value) {
        if (isOnMainThread()) {
            nativeSetOverlapEventsEnabled(id, Value);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeSetOverlapEventsEnabled(id, Value);
            });

        }
        return this;
    }

    private native boolean nativeHasOverlapEventsEnabled(int id);

    public boolean hasOverlapEventsEnabled() {
        if (threadIsValid()) {
            return nativeHasOverlapEventsEnabled(id);
        }
        return false;
    }

    @Override
    public String toString() {
        return "GameObject{ id=" + id + '}';
    }

    private native boolean nativeIsVisible(int id);

    public boolean isVisible() {
        if (threadIsValid()) {
            return nativeIsVisible(id);
        }
        return false;
    }

    private native void nativeSetVisible(int id, boolean Value);

    public GameObject setVisible(boolean Value) {
        if (isOnMainThread()) {
            nativeSetVisible(id, Value);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeSetVisible(id, Value);
            });

        }
        return this;
    }

    private native void nativeSetShowHealthBarWhenDamaged(int id, boolean Value);

    public GameObject setShowHealthBarWhenDamaged(boolean Value) {
        if (isOnMainThread()) {
            nativeSetShowHealthBarWhenDamaged(id, Value);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeSetShowHealthBarWhenDamaged(id, Value);
            });

        }
        return this;
    }

    private native void nativeSetDamageable(int id, boolean value);

    public GameObject setDamageable(boolean value) {
        if (isOnMainThread()) {
            nativeSetDamageable(id, value);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeSetDamageable(id, value);
            });

        }
        return this;
    }

    private native boolean nativeIsBreakable(int id);

    public boolean isBreakable() {
        if (threadIsValid()) {
            return nativeIsBreakable(id);
        }
        return false;
    }

    private native boolean nativeIsBroken(int id);

    public boolean isBroken() {
        if (threadIsValid()) {
            return nativeIsBroken(id);
        }
        return false;
    }

    private native void nativeRestore(int id);

    public GameObject restore() {
        if (isOnMainThread()) {
            nativeRestore(id);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeRestore(id);
            });
        }
        return this;
    }

    private native void nativeSetRestoreAfterBreaking(int id, boolean Value);

    public GameObject setRestoreAfterBreaking(boolean Value) {
        if (isOnMainThread()) {
            nativeSetRestoreAfterBreaking(id, Value);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeSetRestoreAfterBreaking(id, Value);
            });

        }
        return this;
    }

    private native void nativeSetRestoreAfterBreakingDelay(int id, float Value);

    public GameObject setRestoreAfterBreakingDelay(float Value) {
        if (isOnMainThread()) {
            nativeSetRestoreAfterBreakingDelay(id, Value);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeSetRestoreAfterBreakingDelay(id, Value);
            });

        }
        return this;
    }

    private native void nativeSetCollisionEnabled(int id, boolean value);

    public GameObject setCollisionEnabled(boolean value) {
        if (isOnMainThread()) {
            nativeSetCollisionEnabled(id, value);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeSetCollisionEnabled(id, value);
            });

        }
        return this;
    }

    private native void nativeDestroy(int id);

    public GameObject destroy() {
        if (isOnMainThread()) {
            nativeDestroy(id);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeDestroy(id);
            });
        }
        return this;
    }

    private native void nativeDetach(int id);

    public GameObject detach() {
        if (isOnMainThread()) {
            nativeDetach(id);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeDetach(id);
            });
        }
        return this;
    }

    private native boolean nativeIsAttached(int id);

    public boolean isAttached() {
        if (threadIsValid()) {
            return nativeIsAttached(id);
        }
        return false;
    }

    private native GameEntity nativeGetAttachedEntity(int id);

    public GameEntity getAttachedEntity() {
        if (threadIsValid()) {
            return nativeGetAttachedEntity(id);
        }
        return null;
    }

    public boolean isAttachedTo(GameEntity e) {
        if (threadIsValid()) {
            return (nativeIsAttached(id) && nativeGetAttachedEntity(id).equals(e));
        }
        return false;
    }

    private native int nativeAttachToEntity(int id, int entType, int entId, String socketName);

    public AttachResult attachTo(GameEntity entity, String socketName) {
        if (threadIsValid()) {
            int r = nativeAttachToEntity(id, entity.type.value, entity.getId(), socketName);
            AttachResult result = AttachResult.value(r);
            if (result.getCode() > 0) {
                switch (result) {
                    case ERROR_FAILEDTOFINDSOCKET:
                        Log.error(result.getDescription(), socketName);
                        break;
                    case ERROR_FAILEDTOFINDSOURCEENTITY:
                        Log.error(result.getDescription(), toString());
                        break;
                    case ERROR_FAILEDTOFINDTARGETENTITY:
                        Log.error(result.getDescription(), entity.toString());
                        break;
                    case ERROR_INVALIDENTITYTYPE:
                        Log.error(result.getDescription(), entity.type.value + "");
                        break;
                    case ERROR_CANNOTATTACH:
                        Log.error(result.getDescription(), toString());
                        break;
                    case ERROR_ALREADYATTACHED:
                        Log.error(result.getDescription(), getAttachedEntity().toString());
                        break;
                    default:
                        break;
                }
            }
            return result;
        }
        return null;
    }

    private native boolean nativeIsDamageable(int id);

    public boolean isDamageable() {
        if (threadIsValid()) {
            return nativeIsDamageable(id);
        }
        return false;
    }

    private native boolean nativeHasCollision(int id);

    public boolean hasCollision() {
        if (threadIsValid()) {
            return nativeHasCollision(id);
        }
        return false;
    }

    private native boolean nativeHasTouchEventsEnabled(int id);

    public boolean hasTouchEventsEnabled() {
        if (threadIsValid()) {
            return nativeHasTouchEventsEnabled(id);
        }
        return false;
    }

    private native double nativeGetHealth(int id);

    @Override
    public float getHealth() {
        return (float) (threadIsValid() ? this.nativeGetHealth(this.id) : -1);
    }

    private native String nativeGetName(int id);

    public String getName() {
        return threadIsValid() ? this.nativeGetName(this.id) : "";
    }

    private native void nativeSetTouchEventsEnabled(int id, boolean Value);

    public GameObject setTouchEventsEnabled(boolean Value) {
        if (isOnMainThread()) {
            nativeSetTouchEventsEnabled(id, Value);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeSetTouchEventsEnabled(id, Value);
            });

        }
        return this;
    }

    private native void nativeSetHealth(int id, double Value);

    public GameObject setHealth(double Value) {
        if (isOnMainThread()) {
            nativeSetHealth(id, Value);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeSetHealth(id, Value);
            });

        }
        return this;
    }

    private native int nativeGetModelId(int id);

    public int getModelId() {
        return threadIsValid() ? this.nativeGetModelId(this.id) : -1;
    }

    private native Vector nativeGetPosition(int id);

    @Override
    public Vector getPosition() {
        return threadIsValid() ? this.nativeGetPosition(this.id) : null;
    }

    private native void nativeSetPosition(int ObjectId, double x, double y, double z);

    public GameObject setPosition(Vector v) {
        if (isOnMainThread()) {
            nativeSetPosition(id, v.x, v.y, v.z);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeSetPosition(id, v.x, v.y, v.z);
            });
        }
        return this;
    }

    private native Rotation nativeGetRotation(int id);

    public Rotation getRotation() {
        return threadIsValid() ? this.nativeGetRotation(this.id) : null;
    }

    private native void nativeSetRotation(int ObjectId, double yaw, double pitch, double roll);

    public GameObject setRotation(Rotation r) {
        if (isOnMainThread()) {
            nativeSetRotation(id, r.yaw, r.pitch, r.roll);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeSetRotation(id, r.yaw, r.pitch, r.roll);
            });
        }
        return this;
    }

    private native void nativeSetTransform(int ObjectId, double x, double y, double z, double yaw, double pitch, double roll);

    public GameObject setTransform(Transform t) {
        if (isOnMainThread()) {
            nativeSetTransform(id, t.position.x, t.position.y, t.position.z, t.rotation.yaw, t.rotation.pitch, t.rotation.roll);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeSetTransform(id, t.position.x, t.position.y, t.position.z, t.rotation.yaw, t.rotation.pitch, t.rotation.roll);
            });
        }
        return this;
    }

}
