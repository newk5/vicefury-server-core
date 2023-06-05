package com.github.newk5.vf.server.core.entities.npc;

import com.github.newk5.vf.server.core.InternalServerEvents;
import com.github.newk5.vf.server.core.controllers.ai.NPCController;
import com.github.newk5.vf.server.core.entities.GameEntity;
import com.github.newk5.vf.server.core.entities.GameEntityType;
import com.github.newk5.vf.server.core.entities.NPCAction;
import com.github.newk5.vf.server.core.entities.Vector;
import com.github.newk5.vf.server.core.entities.VectorWithAngle;
import java.util.HashSet;
import org.tinylog.Logger;

public class NPC extends GameEntity {

    private NPCType NPCType;
    private HashSet<GameEntity> friends;
    private Class controllerClass;
    private NPCController controller;

    private NPC(int id) {
        super();
        this.id = id;
        type = GameEntityType.NPC;

    }

    public NPC setController(Class controllerClass) {
        this.controllerClass = controllerClass;
        if (controllerClass != null) {
            try {
                Object v = controllerClass.getConstructors()[0].newInstance(this);
                this.controller = (NPCController) v;
            } catch (Exception e) {
                e.printStackTrace();
                Logger.error(e);
            }

        }
        return this;
    }

    public NPCController getController() {
        return controller;
    }

    public <T> T getCastedController() {
        return (T) controller;
    }

    public static NPCSpawnProps SpawnProperties() {
        return new NPCSpawnProps();
    }

    public static NPCSpawnProps SpawnProperties(NPCType type, Integer subtype) {
        return new NPCSpawnProps(type, subtype);
    }

    public static NPCSpawnProps SpawnProperties(NPCType type, Integer subtype, Class controller) {
        return new NPCSpawnProps(type, subtype, controller);
    }

    public NPCType getNPCType() {
        return NPCType;
    }

    private native float nativeGetRespawnDelay(int id);

    public float getRespawnDelay() {
        return threadIsValid() ? this.nativeGetRespawnDelay(this.id) : -1;
    }

    private native void nativeSetRespawnDelay(int id, float Delay);

    public NPC setRespawnDelay(float Delay) {
        if (isOnMainThread()) {
            nativeSetRespawnDelay(id, Delay);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeSetRespawnDelay(id, Delay);
            });
        }
        return this;
    }

    private native float nativeGetMaxHealth(int id);

    public float getMaxHealth() {
        return threadIsValid() ? this.nativeGetMaxHealth(this.id) : -1;
    }

    private native void nativeSetMaxHealth(int id, float HP);

    public NPC setMaxHealth(float HP) {
        if (isOnMainThread()) {
            nativeSetMaxHealth(id, HP);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeSetMaxHealth(id, HP);
            });
        }
        return this;
    }

    private native float nativeGetHealth(int id);

    public float getHealth() {
        return threadIsValid() ? this.nativeGetHealth(this.id) : -1;
    }

    private native void nativeSetHealth(int id, float HP);

    public NPC setHealth(float HP) {
        if (isOnMainThread()) {
            nativeSetHealth(id, HP);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeSetHealth(id, HP);
            });
        }
        return this;
    }

    private native void nativeSetTarget(int id, int entityType, int entityId);

    public NPC setTarget(GameEntity e) {

        if (isOnMainThread()) {

            nativeSetTarget(id, e == null ? -1 : e.type.value, e == null ? -1 : e.getId());
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeSetTarget(id, e == null ? -1 : e.type.value, e == null ? -1 : e.getId());
            });
        }
        return this;
    }

    private native float nativeGetMovementSpeed(int id);

    public float getMovementSpeed() {
        return threadIsValid() ? this.nativeGetMovementSpeed(this.id) : -1;
    }

    private native void nativeSetMovementSpeed(int id, float Speed);

    public NPC setMovementSpeed(float Speed) {
        if (isOnMainThread()) {
            nativeSetMovementSpeed(id, Speed);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeSetMovementSpeed(id, Speed);
            });
        }
        return this;
    }

    private native Vector nativeGetPosition(int id);

    public Vector getPosition() {
        return threadIsValid() ? this.nativeGetPosition(this.id) : null;
    }

    private native Vector nativeGetForwardVector(int id);

    public Vector getForwardVector() {
        return threadIsValid() ? this.nativeGetForwardVector(this.id) : null;
    }

    private native Vector nativeGetRightVector(int id);

    public Vector getRightVector() {
        return threadIsValid() ? this.nativeGetRightVector(this.id) : null;
    }

    public void setPosition(Vector v) {
        setPosition(v.getX(), v.getY(), v.getZ());
    }

    private native void nativeSetPosition(int id, double X, double Y, double Z);

    public NPC setPosition(double X, double Y, double Z) {
        if (isOnMainThread()) {
            nativeSetPosition(id, X, Y, Z);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeSetPosition(id, X, Y, Z);
            });
        }
        return this;
    }

    private native boolean nativeHasAutoRespawnEnabled(int id);

    public boolean hasAutoRespawnEnabled() {
        if (threadIsValid()) {
            return nativeHasAutoRespawnEnabled(id);
        }
        return false;
    }

    private native void nativeEnableAutoRespawn(int id);

    public NPC enableAutoRespawn() {
        if (isOnMainThread()) {
            nativeEnableAutoRespawn(id);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeEnableAutoRespawn(id);
            });
        }
        return this;
    }

    private native void nativeDisableAutoRespawn(int id);

    public NPC disableAutoRespawn() {
        if (isOnMainThread()) {
            nativeDisableAutoRespawn(id);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeDisableAutoRespawn(id);
            });
        }

        return this;
    }

    private native float nativeGetSightAngle(int id);

    public float getSightAngle() {
        return threadIsValid() ? this.nativeGetSightAngle(this.id) : -1;
    }

    private native void nativeSetSightAngle(int id, float Angle);

    public NPC setSightAngle(float Angle) {
        if (isOnMainThread()) {
            nativeSetSightAngle(id, Angle);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeSetSightAngle(id, Angle);
            });
        }
        return this;
    }

    private native float nativeGetSightDistance(int id);

    public float getSightDistance() {
        return threadIsValid() ? this.nativeGetSightDistance(this.id) : -1;
    }

    private native void nativeSetSightDistance(int id, float Distance);

    public NPC setSightDistance(float Distance) {
        if (isOnMainThread()) {
            nativeSetSightDistance(id, Distance);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeSetSightDistance(id, Distance);
            });
        }
        return this;
    }

    private native float nativeGetHearingDistance(int id);

    public float getHearingDistance() {
        return threadIsValid() ? this.nativeGetHearingDistance(this.id) : -1;
    }

    private native void nativeSetHearingDistance(int id, float Distance);

    public NPC setHearingDistance(float Distance) {
        if (isOnMainThread()) {
            nativeSetHearingDistance(id, Distance);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeSetHearingDistance(id, Distance);
            });
        }
        return this;
    }

    private native void nativeRespawn(int id);

    public NPC respawn() {
        if (isOnMainThread()) {
            nativeRespawn(id);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeRespawn(id);
            });
        }
        return this;
    }

    private native void nativeSetSpawnpoint(int id, double x, double y, double z, double yawAngle);

    public NPC setSpawnpoint(double x, double y, double z, double yawAngle) {
        if (isOnMainThread()) {
            nativeSetSpawnpoint(id, x, y, z, yawAngle);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeSetSpawnpoint(id, x, y, z, yawAngle);
            });
        }
        return this;
    }

    public NPC setSpawnpoint(VectorWithAngle v) {
        setSpawnpoint(v.getX(), v.getY(), v.getZ(), v.getYawAngle());
        return this;
    }

    private native void nativeDestroy(int id);

    public NPC destroy() {
        if (isOnMainThread()) {
            nativeDestroy(id);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeDestroy(id);

            });
        }
        return this;
    }

    private native VectorWithAngle nativeGetSpawnpoint(int id);

    public VectorWithAngle getSpawnpoint() {
        return threadIsValid() ? this.nativeGetSpawnpoint(this.id) : null;
    }

    private native void nativeSetYawAngle(int id, double Yaw);

    public NPC setYawAngle(double Yaw) {
        if (isOnMainThread()) {
            nativeSetYawAngle(id, Yaw);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeSetYawAngle(id, Yaw);
            });
        }
        return this;
    }

    private native double nativeGetYawAngle(int id);

    public double getYawAngle() {
        return threadIsValid() ? this.nativeGetYawAngle(this.id) : -1;
    }

    private native boolean nativeIsWithinLineOfSight(int id, int gameEntityType, int gameEntityId);

    public boolean isWithinLineOfSight(GameEntity ent) {
        if (ent == null) {
            return false;
        }
        if (threadIsValid()) {
            return nativeIsWithinLineOfSight(id, ent.type.value, ent.getId());
        }

        return false;
    }

    private native int nativeGetAction(int id);

    public NPCAction getAction() {
        if (threadIsValid()) {
            return NPCAction.value(nativeGetAction(id));
        }
        return NPCAction.NONE;
    }

    private native void nativeSetFriend(int id, int ent, int entID);

    public NPC setFriend(GameEntity ent) {
        if (ent == null) {
            return this;
        }
        if (isOnMainThread()) {

            nativeSetFriend(id, ent.type.value, ent.getId());
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeSetFriend(id, ent.type.value, ent.getId());
            });
        }
        return this;
    }

    private native void nativeSetEnemy(int id, int ent, int entID);

    public NPC setEnemy(GameEntity ent) {
        if (ent == null) {
            return this;
        }
        if (isOnMainThread()) {
            nativeSetEnemy(id, ent.type.value, ent.getId());
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeSetEnemy(id, ent.type.value, ent.getId());
            });
        }
        return this;
    }

    private native void nativeMoveToLocation(int id, double x, double y, double z);

    public NPC moveToLocation(double x, double y, double z) {
        if (isOnMainThread()) {
            nativeMoveToLocation(id, x, y, z);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeMoveToLocation(id, x, y, z);
            });
        }
        return this;
    }

    public NPC moveToLocation(Vector v) {
        if (isOnMainThread()) {
            nativeMoveToLocation(id, v.getX(), v.getY(), v.getZ());
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeMoveToLocation(id, v.getX(), v.getY(), v.getZ());
            });
        }
        return this;
    }

    private native void nativeInvestigate(int id, float radius);

    public NPC investigate(float radius) {
        if (isOnMainThread()) {
            nativeInvestigate(id, radius);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeInvestigate(id, radius);
            });
        }
        return this;
    }

    private native void nativeInvestigateLocation(int id, double x, double y, double z, float radius);

    public NPC investigateLocation(Vector v, float radius) {
        if (isOnMainThread()) {
            nativeInvestigateLocation(id, v.getX(), v.getY(), v.getZ(), radius);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeInvestigateLocation(id, v.getX(), v.getY(), v.getZ(), radius);
            });
        }
        return this;
    }

    public NPC investigateLocation(double x, double y, double z, float radius) {
        if (isOnMainThread()) {
            nativeInvestigateLocation(id, x, y, z, radius);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeInvestigateLocation(id, x, y, z, radius);
            });
        }
        return this;
    }

    @Override
    public String toString() {
        return "NPC{" + "id=" + id + '}';
    }

}
