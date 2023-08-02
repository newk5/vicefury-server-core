package com.github.newk5.vf.server.core.entities.vehicle;

import com.github.newk5.vf.server.core.InternalServerEvents;
import com.github.newk5.vf.server.core.entities.*;
import com.github.newk5.vf.server.core.entities.gameobject.GameObject;
import com.github.newk5.vf.server.core.entities.player.Player;

public class Vehicle extends GameEntity {

    private Vehicle(int id) {
        super();
        this.id = id;
        this.type = GameEntityType.VEHICLE;
    }

    private native void nativeDetachObject(int id, int ObjectId);

    public Vehicle detachObject(GameObject obj) {
        if (isOnMainThread()) {
            nativeDetachObject(id, obj.getId());
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeDetachObject(id, obj.getId());
            });
        }
        return this;
    }

    private native boolean nativeHasObjectAttached(int id, int objectId);

    public boolean hasObjectAttached(GameObject obj) {
        if (threadIsValid()) {
            return nativeHasObjectAttached(id, obj.getId());
        }
        return false;
    }

    private native double nativeGetMoveDirection(int id);

    public double getMoveDirection() {
        return threadIsValid() ? this.nativeGetMoveDirection(this.id) : -1;
    }

    private native String nativeGetBoneNames(int id);

    public String getBoneNames() {
        return threadIsValid() ? this.nativeGetBoneNames(this.id) : null;
    }

    private native void nativeDetachAllObjects(int id);

    public Vehicle detachAllObjects() {
        if (isOnMainThread()) {
            nativeDetachAllObjects(id);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeDetachAllObjects(id);
            });
        }
        return this;
    }

    private native int nativeGetDriver(int id);

    public Player getDriver() {
        if (threadIsValid()) {
            int playerID = this.nativeGetDriver(this.id);
            if (playerID == -1) {
                return null;
            }
            return InternalServerEvents.server.getPlayer(playerID);
        }
        return null;
    }

    private native void nativeDestroyVehicle(int id);

    public Vehicle destroy() {
        if (isOnMainThread()) {
            nativeDestroyVehicle(id);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeDestroyVehicle(id);
            });
        }
        return this;
    }

    private native float nativeGetHealth(int id);

    public float getHealth() {
        return threadIsValid() ? this.nativeGetHealth(this.id) : -1;
    }

    private native void nativeSetHealth(int id, float Health);

    public Vehicle setHealth(float Health) {
        if (isOnMainThread()) {
            nativeSetHealth(id, Health);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeSetHealth(id, Health);
            });
        }
        return this;
    }

    private native Vector nativeGetPosition(int id);

    public Vector getPosition() {
        return threadIsValid() ? this.nativeGetPosition(this.id) : null;
    }

    private native void nativeSetPosition(int id, double x, double y, double z);

    public Vehicle setPosition(double x, double y, double z) {
        if (isOnMainThread()) {
            nativeSetPosition(id, x, y, z);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeSetPosition(id, x, y, z);
            });
        }
        return this;
    }

    public Vehicle setPosition(Vector pos) {
        if (isOnMainThread()) {
            nativeSetPosition(id, pos.x, pos.y, pos.z);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeSetPosition(id, pos.x, pos.y, pos.z);
            });
        }
        return this;
    }

    private native boolean nativeIsPassenger(int id, int playerID);

    public boolean isPassenger(Player p) {
        if (threadIsValid()) {
            return nativeIsPassenger(id, p.getId());
        }
        return false;
    }

    public boolean isPassenger(int playerID) {
        if (threadIsValid()) {
            return nativeIsPassenger(id, playerID);
        }
        return false;
    }

    private native boolean nativeIsExploded(int id);

    public boolean isExploded() {
        if (threadIsValid()) {
            return nativeIsExploded(id);
        }
        return false;
    }

    private native Transform nativeGetSpawnpoint(int id);

    public Transform getSpawnpoint() {
        return threadIsValid() ? this.nativeGetSpawnpoint(this.id) : null;
    }

    private native void nativeSetSpawnPoint(int id, double x, double y, double z, double yaw, double pitch, double roll);

    public Vehicle setSpawnPoint(double x, double y, double z, double yaw, double pitch, double roll) {
        if (isOnMainThread()) {
            nativeSetSpawnPoint(id, x, y, z, yaw, pitch, roll);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeSetSpawnPoint(id, x, y, z, yaw, pitch, roll);
            });
        }
        return this;
    }

    private native Rotation nativeGetRotation(int id);

    public Rotation getRotation() {
        return threadIsValid() ? this.nativeGetRotation(this.id) : null;
    }

    private native void nativeSetRotation(int id, double yaw, double pitch, double roll);

    public Vehicle setRotation(double yaw, double pitch, double roll) {
        if (isOnMainThread()) {
            nativeSetRotation(id, yaw, pitch, roll);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeSetRotation(id, yaw, pitch, roll);
            });
        }
        return this;
    }

    public Vehicle setRotation(Rotation rotation) {
        setRotation(rotation.yaw, rotation.pitch, rotation.roll);
        return this;
    }

    public Transform getTransform() {
        if (threadIsValid()) {
            return new Transform(getPosition(), getRotation());
        }
        return null;
    }

    private native void nativeSetTransform(int id, double x, double y, double z, double yaw, double pitch, double roll);

    public Vehicle setTransform(double x, double y, double z, double yaw, double pitch, double roll) {
        if (isOnMainThread()) {
            nativeSetTransform(id, x, y, z, yaw, pitch, roll);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeSetTransform(id, x, y, z, yaw, pitch, roll);
            });
        }
        return this;
    }

    public Vehicle setTransform(Transform transform) {
        setTransform(transform.position.x, transform.position.y, transform.position.z, transform.rotation.yaw, transform.rotation.pitch, transform.rotation.roll);
        return this;
    }

    @Override
    public String toString() {
        return "Vehicle{ id=" + id + '}';
    }
}
