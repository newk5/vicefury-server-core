package com.github.newk5.vf.server.core.entities.player;

import com.github.newk5.vf.server.core.InternalServerEvents;
import com.github.newk5.vf.server.core.entities.GameEntity;
import com.github.newk5.vf.server.core.entities.GameEntityType;
import com.github.newk5.vf.server.core.entities.Vector;
import com.github.newk5.vf.server.core.entities.VectorWithAngle;
import com.github.newk5.vf.server.core.entities.gameobject.GameObject;
import com.github.newk5.vf.server.core.entities.vehicle.Vehicle;

public class Player extends GameEntity {

    private boolean authenticated;

    private Player(int id) {
        super();
        this.id = id;
        this.type = GameEntityType.PLAYER;
    }

    public boolean isAuthenticated() {
        return this.authenticated;
    }

    public Player setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
        return this;
    }

    public Player sendData(String channel, String data) {
        InternalServerEvents.server.sendData(this.id, channel, data);
        return this;
    }

    public Player sendChatMessage(int color, String message, Object... args) {
        InternalServerEvents.server.sendChatMessage(this.id, color, message, args);
        return this;
    }

    public Player sendChatMessage(String message, Object... args) {
        InternalServerEvents.server.sendChatMessage(this.id, message, args);
        return this;
    }

    public Player giveWeapon(int weaponId, int ammo) {
        InternalServerEvents.server.giveWeaponToPlayer(this.id, weaponId, ammo);
        return this;
    }

    private native boolean nativeHasWeapon(int id, int weaponId);

    public boolean hasWeapon(int weaponId) {
        if (threadIsValid()) {
            return nativeHasWeapon(id, weaponId);
        }
        return false;
    }

    private native void nativeKick(int id, String Reason);

    public Player kick(String Reason) {
        if (isOnMainThread()) {
            nativeKick(id, Reason);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeKick(id, Reason);
            });
        }
        return this;
    }

    private native double nativeGetMoveDirection(int id);

    public double getMoveDirection() {
        return threadIsValid() ? this.nativeGetMoveDirection(this.id) : -1;
    }

    private native String nativeGetBoneNames(int id);

    public String getBoneNames() {
        return threadIsValid() ? this.nativeGetBoneNames(this.id) : null;
    }

    private native void nativeDetachObject(int id, int ObjectId);

    public Player detachObject(GameObject o) {
        if (isOnMainThread()) {
            nativeDetachObject(id, o.getId());
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeDetachObject(id, o.getId());
            });
        }
        return this;
    }

    private native void nativeDetachAllObjects(int id);

    public Player detachAllObjects() {
        if (isOnMainThread()) {
            nativeDetachAllObjects(id);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeDetachAllObjects(id);
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

    private native Vehicle nativeGetVehicle(int id);

    public Vehicle getVehicle() {
        return threadIsValid() ? this.nativeGetVehicle(this.id) : null;
    }

    private native int nativeGetPing(int var1);

    public int getPing() {
        return threadIsValid() ? this.nativeGetPing(this.id) : -1;
    }

    private native String nativeGetIP(int id);

    public String getIP() {
        return this.threadIsValid() ? this.nativeGetIP(this.id) : "";
    }

    private native String nativeGetPlayerName(int id);

    public String getName() {
        return this.threadIsValid() ? this.nativeGetPlayerName(this.id) : "";
    }

    private native Vector nativeGetPosition(int id);

    public Vector getPosition() {
        if (threadIsValid()) {
            return this.nativeGetPosition(this.id);
        }
        return null;
    }

    private native Vector nativeGetForwardVector(int id);

    public Vector getForwardVector() {
        if (threadIsValid()) {
            return this.nativeGetForwardVector(this.id);
        }
        return null;
    }

    private native Vector nativeGetRightVector(int id);

    public Vector getRightVector() {
        if (threadIsValid()) {
            return this.nativeGetRightVector(this.id);
        }
        return null;
    }

    private native void nativeSetPosition(int PlayerID, double X, double Y, double Z);

    public Player setPosition(double X, double Y, double Z) {
        if (isOnMainThread()) {
            nativeSetPosition(id, X, Y, Z);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeSetPosition(id, X, Y, Z);
            });
        }
        return this;
    }

    public Player setPosition(Vector v) {
        if (isOnMainThread()) {
            nativeSetPosition(id, v.getX(), v.getY(), v.getZ());
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeSetPosition(id, v.getX(), v.getY(), v.getZ());
            });
        }
        return this;
    }

    private native double nativeGetYawAngle(int id);

    public double getYawAngle() {
        return threadIsValid() ? this.nativeGetYawAngle(this.id) : -1;
    }

    private native void nativeSetYawAngle(int id, double YawAngle);

    public Player setYawAngle(double YawAngle) {
        if (isOnMainThread()) {
            nativeSetYawAngle(id, YawAngle);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeSetYawAngle(id, YawAngle);
            });
        }
        return this;
    }

    private native float nativeGetHealth(int id);

    public float getHealth() {
        return threadIsValid() ? this.nativeGetHealth(this.id) : -1;
    }

    private native float nativeGetArmour(int id);

    public float getArmour() {
        return threadIsValid() ? this.nativeGetArmour(this.id) : -1;
    }

    private native void nativeSetHealth(int id, float Health);

    public Player setHealth(float Health) {
        if (isOnMainThread()) {
            nativeSetHealth(id, Health);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeSetHealth(id, Health);
            });
        }
        return this;
    }

    private native float nativeGetMovementSpeed(int id);

    public float getMovementSpeed() {
        return threadIsValid() ? this.nativeGetMovementSpeed(this.id) : -1;
    }

    private native void nativeSetMovementSpeed(int id, float MovementSpeed);

    public Player setMovementSpeed(float MovementSpeed) {
        if (isOnMainThread()) {
            nativeSetMovementSpeed(id, MovementSpeed);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeSetMovementSpeed(id, MovementSpeed);
            });
        }
        return this;
    }

    private native void nativeSetArmour(int id, float Armour);

    public Player setArmour(float Armour) {
        if (isOnMainThread()) {
            nativeSetArmour(id, Armour);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeSetArmour(id, Armour);
            });
        }
        return this;
    }

    private native int nativeGetSkin(int id);

    public int getSkin() {
        return threadIsValid() ? this.nativeGetSkin(this.id) : -1;
    }

    private native void nativeSetTeam(int id, int Team);

    public Player setTeam(int team) {
        if (isOnMainThread()) {
            nativeSetTeam(id, team);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeSetTeam(id, team);
            });
        }
        return this;
    }

    private native int nativeGetTeam(int id);

    public int getTeam() {
        return threadIsValid() ? this.nativeGetTeam(this.id) : -1;
    }

    private native void nativeSetColor(int id, int TeamColor);

    public Player setColor(int color) {
        if (isOnMainThread()) {
            nativeSetColor(id, color);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeSetColor(id, color);
            });
        }
        return this;
    }

    private native int nativeGetColor(int id);

    public int getColor() {
        return threadIsValid() ? this.nativeGetColor(this.id) : -1;
    }

    private native void nativeSetSpawnpoint(int playerId, double X, double Y, double Z, double yawAngle);

    public Player setSpawnpoint(double X, double Y, double Z, double yawAngle) {
        if (isOnMainThread()) {
            nativeSetSpawnpoint(id, X, Y, Z, yawAngle);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeSetSpawnpoint(id, X, Y, Z, yawAngle);
            });
        }
        return this;
    }

    public Player setSpawnpoint(VectorWithAngle pos) {
        if (isOnMainThread()) {
            nativeSetSpawnpoint(id, pos.x, pos.y, pos.z, pos.yawAngle);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeSetSpawnpoint(id, pos.x, pos.y, pos.z, pos.yawAngle);
            });
        }
        return this;
    }

    private native VectorWithAngle nativeGetSpawnpoint(int id);

    public VectorWithAngle getSpawnpoint() {
        return threadIsValid() ? this.nativeGetSpawnpoint(this.id) : null;
    }

    private native boolean nativeIsSpawned(int id);

    public boolean isSpawned() {
        if (threadIsValid()) {
            return nativeIsSpawned(id);
        }
        return false;
    }

    private native boolean nativeIsShooting(int id);

    public boolean isShooting() {
        if (threadIsValid()) {
            return nativeIsShooting(id);
        }
        return false;
    }

    private native boolean nativeIsRunning(int id);

    public boolean isRunning() {
        if (threadIsValid()) {
            return nativeIsRunning(id);
        }
        return false;
    }

    private native boolean nativeIsWalking(int id);

    public boolean isWalking() {
        if (threadIsValid()) {
            return nativeIsWalking(id);
        }
        return false;
    }

    private native boolean nativeIsCrouched(int id);

    public boolean isCrouched() {
        if (threadIsValid()) {
            return nativeIsCrouched(id);
        }
        return false;
    }

    private native boolean nativeIsDriving(int id);

    public boolean isDriving() {
        if (threadIsValid()) {
            return nativeIsDriving(id);
        }
        return false;
    }

    private native boolean nativeIsPassenger(int id);

    public boolean isPassenger() {
        if (threadIsValid()) {
            return nativeIsPassenger(id);
        }
        return false;
    }

    private native boolean nativeIsFalling(int id);

    public boolean isFalling() {
        if (threadIsValid()) {
            return nativeIsFalling(id);
        }
        return false;
    }

    private native boolean nativeIsReloading(int id);

    public boolean isReloading() {
        if (threadIsValid()) {
            return nativeIsReloading(id);
        }
        return false;
    }

    private native boolean nativeIsKnocked(int id);

    public boolean isKnocked() {
        if (threadIsValid()) {
            return nativeIsKnocked(id);
        }
        return false;
    }

    private native boolean nativeIsStandingOnVehicle(int id, int vehicleId);

    public boolean isStandingOnVehicle(Vehicle v) {
        if (threadIsValid()) {
            return nativeIsStandingOnVehicle(id, v.getId());
        }
        return false;
    }

    @Override
    public String toString() {
        return "Player{ id=" + id + '}';
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + this.id;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Player other = (Player) obj;
        return this.id == other.id;
    }
}
