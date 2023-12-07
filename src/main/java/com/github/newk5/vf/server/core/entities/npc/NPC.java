package com.github.newk5.vf.server.core.entities.npc;

import com.github.newk5.vf.server.core.InternalServerEvents;
import com.github.newk5.vf.server.core.controllers.NPCController;
import com.github.newk5.vf.server.core.entities.DamageableEntity;
import com.github.newk5.vf.server.core.entities.GameCharacter;
import com.github.newk5.vf.server.core.entities.GameEntity;
import com.github.newk5.vf.server.core.entities.GameEntityType;
import com.github.newk5.vf.server.core.entities.Vector;
import com.github.newk5.vf.server.core.entities.VectorWithAngle;
import com.github.newk5.vf.server.core.entities.Weapon;
import com.github.newk5.vf.server.core.entities.gameobject.GameObject;
import com.github.newk5.vf.server.core.entities.vehicle.Vehicle;
import com.github.newk5.vf.server.core.utils.Log;
import java.util.ArrayList;
import java.util.List;

public class NPC extends GameCharacter {

    public NPCType NPCType;
    private Class controllerClass;
    private NPCController controller;
    private List<Weapon> weapons;

    protected NPC(int id) {
        super();
        this.id = id;
        this.type = GameEntityType.NPC;
    }

    public NPCType getNPCType() {
        return this.NPCType;
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

    public NPCController getController() {
        return this.controller;
    }

    public <T> T getCastedController() {
        return (T) this.controller;
    }

    private native void nativeEnableAvoidance(int id);

    public NPC enableAvoidance() {
        if (isOnMainThread()) {
            nativeEnableAvoidance(id);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeEnableAvoidance(id);
            });
        }
        return this;
    }

    private native void nativeDisableAvoidance(int id);

    public NPC disableAvoidance() {
        if (isOnMainThread()) {
            nativeDisableAvoidance(id);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeDisableAvoidance(id);
            });
        }
        return this;
    }

    private native boolean nativeHasAvoidanceEnabled(int id);

    public boolean hasAvoidanceEnabled() {
        if (threadIsValid()) {
            return nativeHasAvoidanceEnabled(id);
        }
        return false;
    }

    private native void nativeSetNpcStoredWeaponAmmo(int id, int WeaponId, int Ammo);

    public NPC setStoredWeaponAmmo(int WeaponId, int Ammo) {
        if (isOnMainThread()) {
            nativeSetNpcStoredWeaponAmmo(id, WeaponId, Ammo);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeSetNpcStoredWeaponAmmo(id, WeaponId, Ammo);
            });
        }
        return this;
    }

    private native int nativeGetNpcStoredWeaponAmmo(int id, int weaponId);

    public int getStoredWeaponAmmo(int weaponId) {
        return threadIsValid() ? this.nativeGetNpcStoredWeaponAmmo(this.id, weaponId) : -1;
    }

    private native void nativeKeepNpcWeaponsOnRespawn(int id, boolean status);

    public NPC keepWeaponsOnRespawn(boolean status) {
        if (isOnMainThread()) {
            nativeKeepNpcWeaponsOnRespawn(id, status);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeKeepNpcWeaponsOnRespawn(id, status);
            });
        }
        return this;
    }

    private native Vector nativeGetUpVector(int id);

    public Vector getUpVector() {
        return threadIsValid() ? this.nativeGetUpVector(this.id) : null;
    }

    private native int nativeGetVehicle(int id);

    public int getVehicle() {
        return threadIsValid() ? this.nativeGetVehicle(this.id) : -1;
    }

    private native float nativeGetWeaponDamage(int id, int weaponId);

    public float getWeaponDamage(int weaponId) {
        return threadIsValid() ? this.nativeGetWeaponDamage(this.id, weaponId) : -1;
    }

    private native void nativeSetWeaponDamage(int id, int WeaponId, float WeaponDmg);

    public NPC setWeaponDamage(int WeaponId, float WeaponDmg) {
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

    public NPC setWeaponRange(int WeaponId, float WeaponRange) {
        if (isOnMainThread()) {
            nativeSetWeaponRange(id, WeaponId, WeaponRange);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeSetWeaponRange(id, WeaponId, WeaponRange);
            });
        }
        return this;
    }

    private native boolean nativeIsOnTheGround(int id);

    public boolean isOnTheGround() {
        if (threadIsValid()) {
            return nativeIsOnTheGround(id);
        }
        return false;
    }

    private native boolean nativeIsInVehicle(int id);

    public boolean isInVehicle() {
        if (threadIsValid()) {
            return nativeIsInVehicle(id);
        }
        return false;
    }

    private native boolean nativeIsBeingFollowedByNpc(int id, int targetNpcId);

    private native boolean nativeIsFalling(int id);

    public boolean isFalling() {
        if (threadIsValid()) {
            return nativeIsFalling(id);
        }
        return false;
    }

    private native boolean nativeIsNameTagVisible(int id);

    public boolean isNameTagVisible() {
        if (threadIsValid()) {
            return nativeIsNameTagVisible(id);
        }
        return false;
    }

    private native void nativeShowNameTag(int id);

    public NPC showNameTag() {
        if (isOnMainThread()) {
            nativeShowNameTag(id);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeShowNameTag(id);
            });
        }
        return this;
    }

    private native void nativeHideNameTag(int id);

    public NPC hideNameTag() {
        if (isOnMainThread()) {
            nativeHideNameTag(id);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeHideNameTag(id);
            });
        }
        return this;
    }

    public boolean isBeingFollowedBy(NPC npc) {
        if (npc == null) {
            return false;
        }
        if (threadIsValid()) {
            return nativeIsBeingFollowedByNpc(id, npc.id);
        }
        return false;
    }

    private native boolean nativeIsBeingAttackedByNpc(int id, int targetNpcId);

    public boolean isBeingAttackedBy(NPC npc) {
        if (npc == null) {
            return false;
        }
        if (threadIsValid()) {
            return nativeIsBeingAttackedByNpc(id, npc.id);
        }
        return false;
    }

    private native boolean nativeIsMovingToLocation(int id, String location);

    public boolean isMovingToLocation(String location) {
        if (threadIsValid()) {
            return nativeIsMovingToLocation(id, location);
        }
        return false;
    }

    private native boolean nativeIsInvestigating(int id);

    public boolean isInvestigating() {
        if (threadIsValid()) {
            return nativeIsInvestigating(id);
        }
        return false;
    }

    private native void nativeStopMoving(int id);

    public NPC stopMoving() {
        if (isOnMainThread()) {
            nativeStopMoving(id);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeStopMoving(id);
            });
        }
        return this;
    }

    private native boolean nativeIsMoving(int id);

    public boolean isMoving() {
        if (threadIsValid()) {
            return nativeIsMoving(id);
        }
        return false;
    }

    private native int nativeGetAttackStrategy(int id);

    public ShooterAttackStrategy getAttackStrategy() {
        int v = threadIsValid() ? this.nativeGetAttackStrategy(this.id) : 1;
        return ShooterAttackStrategy.value(v);
    }

    private native void nativeSetAttackStrategy(int id, int Strategy);

    public NPC setAttackStrategy(ShooterAttackStrategy strategy) {
        if (isOnMainThread()) {
            nativeSetAttackStrategy(id, strategy.value);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeSetAttackStrategy(id, strategy.value);
            });

        }
        return this;
    }

    public NPC setController(Class controllerClass) {
        this.controllerClass = controllerClass;
        if (controllerClass != null) {
            try {
                Object v = controllerClass.getConstructors()[0].newInstance(this);
                this.controller = (NPCController) v;
            } catch (Exception e) {
                Log.exception(e);
            }
        }
        return this;
    }

    public boolean isDead() {
        if (threadIsValid()) {
            return getHealth() <= 0;
        }
        return false;
    }

    private native void nativeSetColor(int id, int color);

    public NPC setColor(int color) {
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

    private native float nativeGetArmour(int id);

    public float getArmour() {
        return threadIsValid() ? this.nativeGetArmour(this.id) : -1;
    }

    private native void nativeSetArmour(int id, float armour);

    public NPC setArmour(float armour) {
        if (isOnMainThread()) {
            nativeSetArmour(id, armour);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeSetArmour(id, armour);
            });

        }
        return this;
    }

    private native String nativeGetNameTag(int id);

    public String getNameTag() {
        return threadIsValid() ? this.nativeGetNameTag(this.id) : "";
    }

    private native void nativeSetNameTag(int id, String Text);

    public NPC setNameTag(String text) {
        if (isOnMainThread()) {
            nativeSetNameTag(id, text);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeSetNameTag(id, text);
            });

        }
        return this;
    }

    private native void nativeLaunch(int id, double directionX, double directionY, double directionZ, double force, boolean additive);

    public NPC launch(double directionX, double directionY, double directionZ, double force, boolean additive) {
        if (isOnMainThread()) {
            nativeLaunch(id, directionX, directionY, directionZ, force, additive);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeLaunch(id, directionX, directionY, directionZ, force, additive);
            });
        }
        return this;
    }

    public NPC launch(Vector direction, double force, boolean additive) {
        if (isOnMainThread()) {
            nativeLaunch(id, direction.x, direction.y, direction.z, force, additive);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeLaunch(id, direction.x, direction.y, direction.z, force, additive);
            });
        }
        return this;
    }

    private native GameEntity nativeGetFollowTarget(int id);

    public GameEntity getFollowTarget() {
        return threadIsValid() ? this.nativeGetFollowTarget(this.id) : null;
    }

    private native GameEntity nativeGetEnemy(int id);

    public GameEntity getEnemy() {
        return threadIsValid() ? this.nativeGetEnemy(this.id) : null;
    }

    private native void nativeSetMovementMode(int id, int Mode);

    public NPC setMovementMode(NPCMovementMode mode) {
        if (isOnMainThread()) {
            nativeSetMovementMode(id, mode.value);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeSetMovementMode(id, mode.value);
            });

        }
        return this;
    }

    private native int nativeGetMovementMode(int id);

    public NPCMovementMode getMovementMode() {
        return threadIsValid() ? NPCMovementMode.value(this.nativeGetMovementMode(this.id)) : NPCMovementMode.WALKING;
    }

    private native void nativeSetNpcAcceptanceRadius(int id, float Radius);

    public NPC setAcceptanceRadius(float radius) {
        if (isOnMainThread()) {
            nativeSetNpcAcceptanceRadius(id, radius);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeSetNpcAcceptanceRadius(id, radius);
            });

        }
        return this;
    }

    private native float nativeGetNpcAcceptanceRadius(int id);

    public float getNpcAcceptanceRadius() {
        return threadIsValid() ? this.nativeGetNpcAcceptanceRadius(this.id) : -1;
    }

    private native String nativeGetNPCTargetLocation(int id);

    public String getTargetLocation() {
        return threadIsValid() ? this.nativeGetNPCTargetLocation(this.id) : "";
    }

    private native void nativeMoveToNamedLocation(int id, double x, double y, double z, String loc);

    public NPC moveToLocation(Vector loc, String locationName) {
        return moveToLocation(loc.x, loc.y, loc.z, locationName);
    }

    public NPC moveToLocation(double x, double y, double z, String loc) {
        if (isOnMainThread()) {
            nativeMoveToNamedLocation(id, x, y, z, loc);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeMoveToNamedLocation(id, x, y, z, loc);
            });
        }
        return this;
    }

    private native void nativeStartShooting(int id, double directionX, double directionY, double directionZ);

    public NPC startShooting(double directionX, double directionY, double directionZ) {
        if (isOnMainThread()) {
            nativeStartShooting(id, directionX, directionY, directionZ);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeStartShooting(id, directionX, directionY, directionZ);
            });
        }
        return this;
    }

    public NPC startShooting(Vector direction) {
        return startShooting(direction.x, direction.y, direction.z);
    }

    private native void nativeStopShooting(int id);

    public NPC stopShooting() {
        if (isOnMainThread()) {
            nativeStopShooting(id);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeStopShooting(id);
            });
        }
        return this;
    }

    private native boolean nativeIsShooting(int id);

    public boolean isShooting() {
        if (threadIsValid()) {
            return nativeIsShooting(id);
        }
        return false;
    }

    private native void nativeRemoveFromVehicle(int id);

    public NPC removeFromVehicle() {
        if (isOnMainThread()) {
            nativeRemoveFromVehicle(id);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeRemoveFromVehicle(id);
            });
        }
        return this;
    }

    public NPC putInVehicle(Vehicle v) {
        InternalServerEvents.server.putNpcInVehicle(this, v);
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

    private native boolean nativeHasWeapon(int id, int wepId);

    public boolean hasWeapon(int wepId) {
        if (threadIsValid()) {
            return nativeHasWeapon(id, wepId);
        }
        return false;
    }

    private native void nativeRemoveWeapon(int id, int WeaponId);

    public NPC removeWeapon(int wepId) {
        if (isOnMainThread()) {

            nativeRemoveWeapon(id, wepId);
        } else {
            InternalServerEvents.server.mainThread(() -> {

                nativeRemoveWeapon(id, wepId);
            });
        }
        return this;
    }

    private native void nativeSwitchToWeapon(int id, int WeaponId);

    public NPC switchToWeapon(int wepId) {
        if (isOnMainThread()) {
            nativeSwitchToWeapon(id, wepId);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeSwitchToWeapon(id, wepId);
            });
        }
        return this;
    }

    private native double nativeGetAccuracy(int id);

    public double getShooterAccuracy() {
        return threadIsValid() ? this.nativeGetAccuracy(this.id) : -1;
    }

    private native void nativeSetAccuracy(int id, double Accuracy);

    public NPC setShooterAccuracy(double Accuracy) {
        if (isOnMainThread()) {
            nativeSetAccuracy(id, Accuracy);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeSetAccuracy(id, Accuracy);
            });

        }
        return this;
    }

    private native float nativeGetWalkSpeed(int id);

    public float getShooterWalkSpeed() {
        return threadIsValid() ? this.nativeGetWalkSpeed(this.id) : -1;
    }

    private native void nativeSetWalkSpeed(int id, float speed);

    public NPC setShooterWalkSpeed(float speed) {
        if (isOnMainThread()) {
            nativeSetWalkSpeed(id, speed);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeSetWalkSpeed(id, speed);
            });

        }
        return this;
    }

    private native float nativeGetRunSpeed(int id);

    public float getShooterRunSpeed() {
        return threadIsValid() ? this.nativeGetRunSpeed(this.id) : -1;
    }

    private native void nativeSetRunSpeed(int id, float speed);

    public NPC setShooterRunSpeed(float speed) {
        if (isOnMainThread()) {
            nativeSetRunSpeed(id, speed);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeSetRunSpeed(id, speed);
            });

        }
        return this;
    }

    private native int nativeGetTeam(int id);

    public int getTeam() {
        return threadIsValid() ? this.nativeGetTeam(this.id) : -1;
    }

    private native void nativeSetTeam(int id, int Team);

    public NPC setTeam(int Team) {
        if (isOnMainThread()) {
            nativeSetTeam(id, Team);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeSetTeam(id, Team);
            });

        }
        return this;
    }

    public NPC giveWeapon(int weaponId, int ammo) {
        InternalServerEvents.server.giveWeaponToNPC(this, weaponId, ammo, false);
        return this;
    }

    public NPC giveWeapon(int weaponId, int ammo, boolean equipWeapon) {
        InternalServerEvents.server.giveWeaponToNPC(this, weaponId, ammo, equipWeapon);
        return this;
    }

    private native boolean nativeIsFriendly(int id, int entType, int entId);

    public boolean isFriendly(GameEntity ent) {
        if (threadIsValid()) {
            return nativeIsFriendly(id, ent.type.value, ent.getId());
        }
        return false;
    }

    private native void nativeDetachObject(int id, int ObjectId);

    public NPC detachObject(GameObject obj) {
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

    public native boolean nativeCanMoveTo(int id, double x, double y, double z);

    public boolean canMoveTo(Vector location) {
        if (threadIsValid()) {
            return nativeCanMoveTo(id, location.x, location.y, location.z);
        }
        return false;
    }

    private native void nativeDetachAllObjects(int id);

    public NPC detachAllObjects() {
        if (isOnMainThread()) {
            nativeDetachAllObjects(id);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeDetachAllObjects(id);
            });
        }
        return this;
    }

    private native String nativeGetBoneNames(int id);

    public String getBoneNames() {
        return threadIsValid() ? this.nativeGetBoneNames(this.id) : null;
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

    private native void nativeAttack(int id, int entityType, int entityId);

    public NPC attack(GameEntity e) {

        if (isOnMainThread()) {
            nativeAttack(id, e == null ? 0 : e.type.value, e == null ? 0 : e.getId());
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeAttack(id, e == null ? 0 : e.type.value, e == null ? 0 : e.getId());
            });
        }
        return this;
    }

    private native boolean nativeIsBeingFollowedByNpcs(int id);

    public boolean isBeingFollowedByNpcs() {
        if (threadIsValid()) {
            return nativeIsBeingFollowedByNpcs(id);
        }
        return false;
    }

    private native boolean nativeIsBeingAttackedByNpcs(int id);

    public boolean isBeingAttackedByNpcs() {
        if (threadIsValid()) {
            return nativeIsBeingAttackedByNpcs(id);
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

    public boolean isFollowingSomething() {
        return getFollowTarget() != null;
    }

    public boolean isFollowing(GameEntity ent) {
        if (ent == null) {
            return false;
        }
        GameEntity currentlyFollowing = getFollowTarget();
        if (currentlyFollowing == null) {
            return false;
        }
        return ent.equals(currentlyFollowing);
    }

    public NPC stopAttacking() {
        if (isOnMainThread()) {
            nativeAttack(id, 0, 0);
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeAttack(id, 0, 0);
            });
        }
        return this;
    }

    private native void nativeFollow(int id, int entityType, int entityId);

    public NPC follow(GameEntity e) {

        if (isOnMainThread()) {
            nativeFollow(id, e == null ? -1 : e.type.value, e == null ? -1 : e.getId());
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeFollow(id, e == null ? -1 : e.type.value, e == null ? -1 : e.getId());
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

    private native void nativeSetPosition(int id, double X, double Y, double Z);

    public NPC setPosition(Vector v) {
        return this.setPosition(v.getX(), v.getY(), v.getZ());
    }

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

    private native void nativeAddFriend(int id, int ent, int entID);

    public NPC addFriend(GameEntity ent) {
        if (ent == null) {
            return this;
        }
        if (isOnMainThread()) {

            nativeAddFriend(id, ent.type.value, ent.getId());
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeAddFriend(id, ent.type.value, ent.getId());
            });
        }
        return this;
    }

    private native void nativeRemoveFriend(int id, int ent, int entID);

    public NPC removeFriend(GameEntity ent) {
        if (ent == null) {
            return this;
        }
        if (isOnMainThread()) {
            nativeRemoveFriend(id, ent.type.value, ent.getId());
        } else {
            InternalServerEvents.server.mainThread(() -> {
                nativeRemoveFriend(id, ent.type.value, ent.getId());
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
