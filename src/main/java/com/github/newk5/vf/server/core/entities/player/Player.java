package com.github.newk5.vf.server.core.entities.player;

import com.github.newk5.vf.server.core.InternalServerEvents;
import com.github.newk5.vf.server.core.entities.DamageableEntity;
import com.github.newk5.vf.server.core.entities.GameCharacter;
import com.github.newk5.vf.server.core.entities.GameEntity;
import com.github.newk5.vf.server.core.entities.GameEntityType;
import com.github.newk5.vf.server.core.entities.Rotation;
import com.github.newk5.vf.server.core.entities.Transform;
import com.github.newk5.vf.server.core.entities.Vector;
import com.github.newk5.vf.server.core.entities.VectorWithAngle;
import com.github.newk5.vf.server.core.entities.Weapon;
import com.github.newk5.vf.server.core.entities.gameobject.GameObject;
import com.github.newk5.vf.server.core.entities.vehicle.Vehicle;
import java.util.ArrayList;
import java.util.List;

public class Player extends GameCharacter {

    private List<Weapon> weapons;

    private boolean authenticated;

    private Player(int id) {
        super();
        this.id = id;
        this.type = GameEntityType.PLAYER;
    }

    private native void nativeInterpFreeCamTo(int id, double XPos, double YPos, double ZPos, double Yaw, double Pitch, double Roll, double Speed);

    public Player interpFreeCamTo(Transform transform, double speed) {
        if (isOnMainThread()) {
            nativeInterpFreeCamTo(id, transform.position.x, transform.position.y, transform.position.z, transform.rotation.yaw, transform.rotation.pitch, transform.rotation.roll, speed);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeInterpFreeCamTo(id, transform.position.x, transform.position.y, transform.position.z, transform.rotation.yaw, transform.rotation.pitch, transform.rotation.roll, speed);
            });
        }
        return this;
    }

    private native Rotation nativeGetFreeCamRotation(int id);

    public Rotation getFreeCamRotation() {
        return threadIsValid() ? this.nativeGetFreeCamRotation(this.id) : null;
    }

    private native Vector nativeGetFreeCamPosition(int id);

    public Vector getFreeCamPosition() {
        return threadIsValid() ? this.nativeGetFreeCamPosition(this.id) : null;
    }

    private native Vector nativeGetFreeCamDirection(int id);

    public Vector getFreeCamDirection() {
        return threadIsValid() ? this.nativeGetFreeCamDirection(this.id) : null;
    }

    private native void nativeAllowAccessToRemotePlayerData(int id);

    public Player allowAccessToRemotePlayerData() {
        if (isOnMainThread()) {
            nativeAllowAccessToRemotePlayerData(id);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeAllowAccessToRemotePlayerData(id);
            });
        }
        return this;
    }

    private native void nativeAllowAccessToRemoteVehicleData(int id);

    public Player allowAccessToRemoteVehicleData() {
        if (isOnMainThread()) {
            nativeAllowAccessToRemoteVehicleData(id);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeAllowAccessToRemoteVehicleData(id);
            });
        }
        return this;
    }

    private native void nativeAllowAccessToRemoteNPCData(int id);

    public Player allowAccessToRemoteNPCData() {
        if (isOnMainThread()) {
            nativeAllowAccessToRemoteNPCData(id);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeAllowAccessToRemoteNPCData(id);
            });
        }
        return this;
    }

    private native void nativeAllowAccessToRemoteObjectData(int id);

    public Player allowAccessToRemoteObjectData() {
        if (isOnMainThread()) {
            nativeAllowAccessToRemoteObjectData(id);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeAllowAccessToRemoteObjectData(id);
            });
        }
        return this;
    }

    private native void nativeDisallowAccessToRemotePlayerData(int id);

    public Player disallowAccessToRemotePlayerData() {
        if (isOnMainThread()) {
            nativeDisallowAccessToRemotePlayerData(id);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeDisallowAccessToRemotePlayerData(id);
            });
        }
        return this;
    }

    private native void nativeDisallowAccessToRemoteVehicleData(int id);

    public Player disallowAccessToRemoteVehicleData() {
        if (isOnMainThread()) {
            nativeDisallowAccessToRemoteVehicleData(id);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeDisallowAccessToRemoteVehicleData(id);
            });
        }
        return this;
    }

    private native void nativeDisallowAccessToRemoteNPCData(int id);

    public Player disallowAccessToRemoteNPCData() {
        if (isOnMainThread()) {
            nativeDisallowAccessToRemoteNPCData(id);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeDisallowAccessToRemoteNPCData(id);
            });
        }
        return this;
    }

    private native void nativeDisallowAccessToRemoteObjectData(int id);

    public Player disallowAccessToRemoteObjectData() {
        if (isOnMainThread()) {
            nativeDisallowAccessToRemoteObjectData(id);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeDisallowAccessToRemoteObjectData(id);
            });
        }
        return this;
    }

    private native boolean nativeHasAccessToRemotePlayerData(int id);

    public boolean hasAccessToRemotePlayerData() {
        if (threadIsValid()) {
            return nativeHasAccessToRemotePlayerData(id);
        }
        return false;
    }

    private native boolean nativeHasAccessToRemoteVehicleData(int id);

    public boolean hasAccessToRemoteVehicleData() {
        if (threadIsValid()) {
            return nativeHasAccessToRemoteVehicleData(id);
        }
        return false;
    }

    private native boolean nativeHasAccessToRemoteObjectData(int id);

    public boolean hasAccessToRemoteObjectData() {
        if (threadIsValid()) {
            return nativeHasAccessToRemoteObjectData(id);
        }
        return false;
    }

    private native boolean nativeHasAccessToRemoteNPCData(int id);

    public boolean hasAccessToRemoteNPCData() {
        if (threadIsValid()) {
            return nativeHasAccessToRemoteNPCData(id);
        }
        return false;
    }

    private native void nativeEnableFreeCamWithDisabledControls(int id);

    public Player enableFreeCamWithDisabledControls() {
        if (isOnMainThread()) {
            nativeEnableFreeCamWithDisabledControls(id);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeEnableFreeCamWithDisabledControls(id);
            });
        }
        return this;
    }

    private native void nativeEnableFreeCamWithTransformAndDisableControls(int id, double XPos, double YPos, double ZPos, double Yaw, double Pitch, double Roll);

    public Player enableFreeCamWithTransformAndDisableControls(Transform transform) {
        if (isOnMainThread()) {
            nativeEnableFreeCamWithTransformAndDisableControls(id, transform.position.x, transform.position.y, transform.position.z, transform.rotation.yaw, transform.rotation.pitch, transform.rotation.roll);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeEnableFreeCamWithTransformAndDisableControls(id, transform.position.x, transform.position.y, transform.position.z, transform.rotation.yaw, transform.rotation.pitch, transform.rotation.roll);
            });
        }
        return this;
    }

    private native void nativeEnableFreeCamWithDisabledControlsAndInterpTo(int id, double XPos, double YPos, double ZPos, double Yaw, double Pitch, double Roll, double Speed);

    public Player enableFreeCamWithDisabledControlsAndInterpTo(Transform transform, double speed) {
        if (isOnMainThread()) {
            nativeEnableFreeCamWithDisabledControlsAndInterpTo(id, transform.position.x, transform.position.y, transform.position.z, transform.rotation.yaw, transform.rotation.pitch, transform.rotation.roll, speed);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeEnableFreeCamWithDisabledControlsAndInterpTo(id, transform.position.x, transform.position.y, transform.position.z, transform.rotation.yaw, transform.rotation.pitch, transform.rotation.roll, speed);
            });
        }
        return this;
    }

    private native void nativeSetFreeCamTransform(int player, double XPos, double YPos, double ZPos, double Yaw, double Pitch, double Roll);

    public Player setFreeCamTransform(Transform transform) {
        if (isOnMainThread()) {
            nativeSetFreeCamTransform(id, transform.position.x, transform.position.y, transform.position.z, transform.rotation.yaw, transform.rotation.pitch, transform.rotation.roll);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeSetFreeCamTransform(id, transform.position.x, transform.position.y, transform.position.z, transform.rotation.yaw, transform.rotation.pitch, transform.rotation.roll);
            });
        }
        return this;
    }

    private native boolean nativeIsRevivingSomeone(int id);

    public boolean isRevivingSomeone() {
        if (threadIsValid()) {
            return nativeIsRevivingSomeone(id);
        }
        return false;
    }

    private native boolean nativeIsRevivingCharacter(int id, int charType, int charId);

    public boolean isRevivingCharacter(GameCharacter character) {
        if (character == null) {
            return false;
        }
        if (threadIsValid()) {
            return nativeIsRevivingCharacter(id, character.type.value, character.getId());
        }
        return false;

    }

    private native boolean nativeIsBeingRevived(int id);

    public boolean isBeingRevived() {
        if (threadIsValid()) {
            return nativeIsBeingRevived(id);
        }
        return false;
    }

    private native boolean nativeIsBeingRevivedBy(int id, int charType, int charId);

    public boolean isBeingRevivedBy(GameCharacter by) {
        if (by == null) {
            return false;
        }
        if (threadIsValid()) {
            return nativeIsBeingRevivedBy(id, by.type.value, by.getId());
        }
        return false;
    }

    private native void nativeDownPlayer(int id);

    public Player down() {
        if (isOnMainThread()) {
            nativeDownPlayer(id);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeDownPlayer(id);
            });
        }
        return this;
    }

    private native void nativeRevivePlayer(int id);

    public Player revive() {
        if (isOnMainThread()) {
            nativeRevivePlayer(id);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeRevivePlayer(id);
            });
        }
        return this;
    }

    private native boolean nativeCanBeRevived(int id);

    public boolean canBeRevived() {
        if (threadIsValid()) {
            return nativeCanBeRevived(id);
        }
        return false;
    }

    private native void nativeSetCanBeRevived(int id, boolean Status);

    public Player setCanBeRevived(boolean Status) {
        if (isOnMainThread()) {
            nativeSetCanBeRevived(id, Status);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeSetCanBeRevived(id, Status);
            });

        }
        return this;
    }

    private native boolean nativeIsAwaitingRevive(int id);

    public boolean isAwaitingRevive() {
        if (threadIsValid()) {
            return nativeIsAwaitingRevive(id);
        }
        return false;
    }

    private native float nativeGetReviveWaitTime(int id);

    public float getReviveWaitTime() {
        return threadIsValid() ? this.nativeGetReviveWaitTime(this.id) : -1;
    }

    private native void nativeSetReviveWaitTime(int id, float Seconds);

    public Player setReviveWaitTime(float Seconds) {
        if (isOnMainThread()) {
            nativeSetReviveWaitTime(id, Seconds);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeSetReviveWaitTime(id, Seconds);
            });

        }
        return this;
    }

    private native float nativeGetReviveDuration(int id);

    public float getReviveDuration() {
        return threadIsValid() ? this.nativeGetReviveDuration(this.id) : -1;
    }

    private native void nativeSetReviveDuration(int id, float Seconds);

    public Player setReviveDuration(float Seconds) {
        if (isOnMainThread()) {
            nativeSetReviveDuration(id, Seconds);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeSetReviveDuration(id, Seconds);
            });

        }
        return this;
    }

    private native float nativeGetAwatingReviveHP(int id);

    public float getAwatingReviveHP() {
        return threadIsValid() ? this.nativeGetAwatingReviveHP(this.id) : -1;
    }

    private native void nativeSetAwatingReviveHP(int id, float HP);

    public Player setAwatingReviveHP(float HP) {
        if (isOnMainThread()) {
            nativeSetAwatingReviveHP(id, HP);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeSetAwatingReviveHP(id, HP);
            });

        }
        return this;
    }

    private native float nativeGetHPWhenRevived(int id);

    public float getHPWhenRevived() {
        return threadIsValid() ? this.nativeGetHPWhenRevived(this.id) : -1;
    }

    private native void nativeSetHPWhenRevived(int id, float HP);

    public Player setHPWhenRevived(float HP) {
        if (isOnMainThread()) {
            nativeSetHPWhenRevived(id, HP);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeSetHPWhenRevived(id, HP);
            });

        }
        return this;
    }

    public boolean isAuthenticated() {
        return this.authenticated;
    }

    public Player setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
        return this;
    }

    private native boolean nativeIsSwimming(int id);

    public boolean isSwimming() {
        if (threadIsValid()) {
            return nativeIsSwimming(id);
        }
        return false;
    }

    private native boolean nativeIsUnderwater(int id);

    public boolean isUnderwater() {
        if (threadIsValid()) {
            return nativeIsUnderwater(id);
        }
        return false;
    }

    private native boolean nativeIsSpectatingPlayer(int id, int playerId);

    public boolean isSpectating(int playerId) {
        if (threadIsValid()) {
            return nativeIsSpectatingPlayer(id, playerId);
        }

        return false;
    }

    public boolean isSpectating(Player p) {
        if (threadIsValid()) {
            return nativeIsSpectatingPlayer(id, p.id);
        }
        return false;
    }

    private native int nativeGetSpectateTarget(int id);

    public Player getSpectateTarget() {
        if (threadIsValid()) {
            int playerId = nativeGetSpectateTarget(id);
            if (playerId == 0) {
                return null;
            }
            return InternalServerEvents.server.getPlayer(playerId);
        }
        return null;
    }

    private native void nativeSpectatePlayer(int id, int targetId);

    public Player spectate(int TargetId) {
        if (isOnMainThread()) {
            nativeSpectatePlayer(id, TargetId);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeSpectatePlayer(id, TargetId);
            });

        }
        return this;
    }

    public Player spectate(Player p) {
        if (isOnMainThread()) {
            nativeSpectatePlayer(id, p.id);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeSpectatePlayer(id, p.id);
            });
        }
        return this;
    }

    private native void nativeStopSpectating(int id);

    public Player stopSpectating() {
        if (isOnMainThread()) {
            nativeStopSpectating(id);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeStopSpectating(id);
            });
        }
        return this;
    }

    private native boolean nativePlayerIsSpectating(int id);

    public boolean isSpectating() {
        if (threadIsValid()) {
            return nativePlayerIsSpectating(id);
        }
        return false;
    }

    public Player sendData(String channel, String data) {
        InternalServerEvents.server.sendData(this.id, channel, data);
        return this;
    }

    public Player sendChatMessage(int color, String message, Object... args) {
        InternalServerEvents.server.sendChatMessage(this.id, color, message, args);
        return this;
    }

    public Player sendChatMessage(String message) {
        InternalServerEvents.server.sendChatMessage(this.id, 0xFFFFFFFF, message);
        return this;
    }

    public Player sendChatMessage(String message, Object... args) {
        InternalServerEvents.server.sendChatMessage(this.id, 0xFFFFFFFF, message, args);
        return this;
    }

    public Player giveWeapon(int weaponId, int ammo) {
        InternalServerEvents.server.giveWeaponToPlayer(this.id, weaponId, ammo);
        return this;
    }

    private native void nativeEnableFreeCam(int id);

    public Player enableFreeCam() {
        if (isOnMainThread()) {
            nativeEnableFreeCam(id);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeEnableFreeCam(id);
            });
        }
        return this;
    }

    private native void nativeDisableFreeCam(int id);

    public Player disableFreeCam() {
        if (isOnMainThread()) {
            nativeDisableFreeCam(id);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeDisableFreeCam(id);
            });
        }
        return this;
    }

    private native void nativeSetPlayerStoredWeaponAmmo(int id, int WeaponId, int Ammo);

    public Player setStoredWeaponAmmo(int WeaponId, int Ammo) {
        if (isOnMainThread()) {
            nativeSetPlayerStoredWeaponAmmo(id, WeaponId, Ammo);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeSetPlayerStoredWeaponAmmo(id, WeaponId, Ammo);
            });
        }
        return this;
    }

    private native int nativeGetPlayerStoredWeaponAmmo(int id, int weaponId);

    public int getStoredWeaponAmmo(int weaponId) {
        return threadIsValid() ? this.nativeGetPlayerStoredWeaponAmmo(this.id, weaponId) : -1;
    }

    private native void nativeRemovePlayerWeapon(int id, int WeaponId);

    public Player removeWeapon(int WeaponId) {
        if (isOnMainThread()) {
            nativeRemovePlayerWeapon(id, WeaponId);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeRemovePlayerWeapon(id, WeaponId);
            });
        }
        return this;
    }

    private native void nativeKeepPlayerWeaponsOnRespawn(int id, boolean status);

    public Player keepWeaponsOnRespawn(boolean status) {
        if (isOnMainThread()) {
            nativeKeepPlayerWeaponsOnRespawn(id, status);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeKeepPlayerWeaponsOnRespawn(id, status);
            });
        }
        return this;
    }

    public Weapon getWeapon(int weaponId) {
        return getWeapons().stream().filter(wep -> wep.getWeaponId() == weaponId).findFirst().orElse(null);
    }

    public List<Weapon> getWeapons() {
        if (weapons == null) {
            weapons = new ArrayList<>();
        }
        return weapons;
    }

    private native Vector nativeGetUpVector(int id);

    public Vector getUpVector() {
        return threadIsValid() ? this.nativeGetUpVector(this.id) : null;
    }

    private native float nativeGetWeaponDamage(int id, int weaponId);

    public float getWeaponDamage(int weaponId) {
        return threadIsValid() ? this.nativeGetWeaponDamage(this.id, weaponId) : -1;
    }

    private native void nativeSetWeaponDamage(int id, int WeaponId, float WeaponDmg);

    public Player setWeaponDamage(int WeaponId, float WeaponDmg) {
        if (isOnMainThread()) {
            nativeSetWeaponDamage(id, WeaponId, WeaponDmg);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeSetWeaponDamage(id, WeaponId, WeaponDmg);
            });
        }
        return this;
    }

    private native float nativeGetWeaponRange(int id, int weaponId);

    public float getWeaponRange(int weaponId) {
        return threadIsValid() ? this.nativeGetWeaponRange(this.id, weaponId) : -1;
    }

    private native void nativeSetWeaponRange(int id, int WeaponId, float WeaponRange);

    public Player setWeaponRange(int WeaponId, float WeaponRange) {
        if (isOnMainThread()) {
            nativeSetWeaponRange(id, WeaponId, WeaponRange);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeSetWeaponRange(id, WeaponId, WeaponRange);
            });
        }
        return this;
    }

    private native void nativeLaunch(int id, double directionX, double directionY, double directionZ, double force, boolean additive);

    public Player launch(double directionX, double directionY, double directionZ, double force, boolean additive) {
        if (isOnMainThread()) {
            nativeLaunch(id, directionX, directionY, directionZ, force, additive);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeLaunch(id, directionX, directionY, directionZ, force, additive);
            });
        }
        return this;
    }

    public Player launch(Vector direction, double force, boolean additive) {
        launch(direction.x, direction.y, direction.z, force, additive);
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
