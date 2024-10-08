package com.github.newk5.vf.server.core;

import com.github.newk5.vf.server.core.commands.CommandRegistry;
import com.github.newk5.vf.server.core.controllers.BaseEventController;
import com.github.newk5.vf.server.core.entities.*;
import com.github.newk5.vf.server.core.entities.gameobject.EntitySocket;
import com.github.newk5.vf.server.core.entities.gameobject.GameObject;
import com.github.newk5.vf.server.core.entities.gameobject.ObjectSpawnProps;
import com.github.newk5.vf.server.core.entities.npc.NPC;
import com.github.newk5.vf.server.core.entities.npc.NPCSpawnProps;
import com.github.newk5.vf.server.core.entities.npc.NPCType;
import com.github.newk5.vf.server.core.entities.player.Player;
import com.github.newk5.vf.server.core.entities.vehicle.Vehicle;
import com.github.newk5.vf.server.core.entities.vehicle.VehicleSpawnProps;
import com.github.newk5.vf.server.core.entities.zone.Zone;
import com.github.newk5.vf.server.core.entities.zone.ZoneSpawnProps;
import com.github.newk5.vf.server.core.exceptions.InvalidThreadException;
import com.github.newk5.vf.server.core.utils.CoreUtils;
import com.github.newk5.vf.server.core.utils.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Server {

    private long threadId = Thread.currentThread().getId();
    private long timerCounter;
    public CommandRegistry commandRegistry;
    private InternalServerEvents baseServerEvents;

    protected Server(InternalServerEvents internalEvents) {
        commandRegistry = new CommandRegistry();
        baseServerEvents = internalEvents;
    }

    private native String nativeGetServerVersion();

    public String getServerVersion() {
        if (threadIsValid()) {
            return nativeGetServerVersion();
        }
        return "";
    }

    private native String nativeGetServerName();

    public String getServerName() {
        if (threadIsValid()) {
            nativeGetServerName();
        }
        return "";

    }

    private native String nativeGetServerPassword();

    public String getServerPassword() {
        if (threadIsValid()) {
            return nativeGetServerPassword();
        }
        return "";
    }

    private native void nativeSetServerPassword(String pwd);

    private native void nativeSetServerName(String name);

    public void setServerPassword(String pwd) {
        if (isOnMainThread()) {
            nativeSetServerPassword(pwd);
        } else {
            mainThread(() -> {
                nativeSetServerPassword(pwd);
            });
        }
    }

    public Server unlock() {
        setServerPassword("");
        return this;
    }

    public boolean isLocked() {
        if (isOnMainThread()) {
            return !getServerPassword().equals("");
        }
        return false;
    }

    public void setServerName(String name) {
        if (isOnMainThread()) {
            nativeSetServerName(name);
        } else {
            mainThread(() -> {
                nativeSetServerName(name);
            });
        }
    }

    private native Rotation nativeCreateRotationFromAxes(double fwdX, double fwdY, double fwdZ, double rightX, double rightY, double rightZ, double upX, double upY, double upZ);

    public Rotation createRotationFromAxes(Vector forwardDirection, Vector rightDirection, Vector upDirection) {
        if (threadIsValid()) {
            return nativeCreateRotationFromAxes(forwardDirection.x, forwardDirection.y, forwardDirection.z, rightDirection.x, rightDirection.y, rightDirection.z, upDirection.x, upDirection.y, upDirection.z);
        }
        return null;
    }

    private native int nativeSpawnVehicle(double x, double y, double z, double yaw, double pitch, double roll, int modelId);

    public Vehicle spawnVehicle(VehicleSpawnProps props) {
        if (threadIsValid()) {
            Integer id = nativeSpawnVehicle(props.position.x, props.position.y, props.position.z, props.rotation.yaw, props.rotation.pitch, props.rotation.roll, props.getModelId());
            return getVehicle(id);
        }
        return null;
    }

    private native String nativeGetConfigProperty(String section, String property);

    public String getConfigProperty(String section, String property) {
        return threadIsValid() ? this.nativeGetConfigProperty(section, property) : null;
    }

    public String getConfigProperty(String section, String property, String defaultValue) {
        if (threadIsValid()) {
            String v = this.nativeGetConfigProperty(section, property);
            if (v.equals("")) {
                return defaultValue;
            }
            return v;
        }
        return null;
    }

    private native double nativeGetWaterLevel();

    public double getWaterLevel() {
        return threadIsValid() ? this.nativeGetWaterLevel() : -1;
    }

    private native void nativeSetWaterLevel(double level);

    public Server setWaterLevel(double level) {
        if (threadIsValid()) {
            nativeSetWaterLevel(level);
        }
        return this;
    }

    private boolean isOnMainThread() {
        return (this.threadId == Thread.currentThread().getId());
    }

    private boolean threadIsValid() {
        if (this.threadId != Thread.currentThread().getId()) {
            InvalidThreadException e = new InvalidThreadException("You cannot use the server API from outside the main server thread, use server.mainThread(()-> {  }) to run this on the main thread");
            Log.exception(e);
            return false;
        } else {
            return true;
        }
    }

    public void mainThread(Runnable r) {
        InternalServerEvents.queue.offer(r);
    }

    public BaseEventController getEventHandler(String name) {
        return baseServerEvents.getEventHandler(name);
    }

    public List<Player> getAllPlayers() {
        return new ArrayList<>(InternalServerEvents.allPlayers);
    }

    public List< NPC> getAllNPCs() {
        return new ArrayList<>(InternalServerEvents.allNpcs);
    }

    public List<Vehicle> getAllVehicles() {
        return new ArrayList<>(InternalServerEvents.allVehicles);
    }

    public List<GameObject> getAllObjects() {
        return new ArrayList<>(InternalServerEvents.allObjects);
    }

    public List<Zone> getAllZones() {
        return new ArrayList<>(InternalServerEvents.allZones);
    }

    public void forEachPlayer(Consumer<Player> c) {
        for (Player p : InternalServerEvents.allPlayers) {
            c.accept(p);
        }
    }

    public void forEachNpc(Consumer<NPC> c) {
        for (NPC p : InternalServerEvents.allNpcs) {
            c.accept(p);
        }
    }

    public void forEachVehicle(Consumer<Vehicle> c) {
        for (Vehicle v : InternalServerEvents.allVehicles) {
            c.accept(v);
        }
    }

    public void forEachObject(Consumer<GameObject> obj) {
        for (GameObject o : InternalServerEvents.allObjects) {
            obj.accept(o);
        }
    }

    public void forEachZone(Consumer<Zone> obj) {
        for (Zone o : InternalServerEvents.allZones) {
            obj.accept(o);
        }
    }

    public Stream<Player> players() {
        return InternalServerEvents.allPlayers.stream();
    }

    public Stream<NPC> npcs() {
        return InternalServerEvents.allNpcs.stream();
    }

    public Stream<Vehicle> vehicles() {
        return InternalServerEvents.allVehicles.stream();
    }

    public Stream<GameObject> objects() {
        return InternalServerEvents.allObjects.stream();
    }

    public Stream<Zone> zones() {
        return InternalServerEvents.allZones.stream();
    }

    public Player findPlayer(Predicate<Player> condition) {
        return InternalServerEvents.allPlayers.stream().filter(condition).findFirst().orElse(null);
    }

    public List<Player> findPlayers(Predicate<Player> condition) {
        return InternalServerEvents.allPlayers.stream().filter(condition).collect(Collectors.toList());
    }

    public Vehicle findVehicle(Predicate<Vehicle> condition) {
        return InternalServerEvents.allVehicles.stream().filter(condition).findFirst().orElse(null);
    }

    public List<Vehicle> findVehicles(Predicate<Vehicle> condition) {
        return InternalServerEvents.allVehicles.stream().filter(condition).collect(Collectors.toList());
    }

    public NPC findNpc(Predicate<NPC> condition) {
        return InternalServerEvents.allNpcs.stream().filter(condition).findFirst().orElse(null);
    }

    public List<NPC> findNpcs(Predicate<NPC> condition) {
        return InternalServerEvents.allNpcs.stream().filter(condition).collect(Collectors.toList());
    }

    public GameObject findObject(Predicate<GameObject> condition) {
        return InternalServerEvents.allObjects.stream().filter(condition).findFirst().orElse(null);
    }

    public List<GameObject> findObjects(Predicate<GameObject> condition) {
        return InternalServerEvents.allObjects.stream().filter(condition).collect(Collectors.toList());
    }

    private native void nativeGiveWeaponToShooterNPC(int id, int weaponId, int ammo);

    public Server giveWeaponToNPC(NPC npc, int weaponId, int ammo, boolean equip) {
        if (npc.getNPCType() != NPCType.SHOOTER) {
            return this;
        }
        if (isOnMainThread()) {
            nativeGiveWeaponToShooterNPC(npc.getId(), weaponId, ammo);
            if (equip) {
                npc.switchToWeapon(weaponId);
            }
        } else {
            mainThread(() -> {
                nativeGiveWeaponToShooterNPC(npc.getId(), weaponId, ammo);
                if (equip) {
                    npc.switchToWeapon(weaponId);
                }
            });
        }
        return this;
    }

    private native void nativeSetLowTPSWarningLimit(int limit);

    public Server setLowTPSWarningLimit(int limit) {
        if (isOnMainThread()) {
            nativeSetLowTPSWarningLimit(limit);
        } else {
            mainThread(() -> {
                nativeSetLowTPSWarningLimit(limit);
            });
        }
        return this;
    }

    private native int nativeGetTPS();

    public int getTPS() {
        return threadIsValid() ? this.nativeGetTPS() : -1;
    }

    private native void nativeSetSpawnScreenLocation(double X, double Y, double Z, double yawLookAt);

    public Server setSpawnScreenLocation(VectorWithAngle pos) {
        if (isOnMainThread()) {
            nativeSetSpawnScreenLocation(pos.x, pos.y, pos.z, pos.yawAngle);
        } else {
            mainThread(() -> {
                nativeSetSpawnScreenLocation(pos.x, pos.y, pos.z, pos.yawAngle);
            });
        }
        return this;
    }

    private native void nativeSpawnWeapon(int id, double x, double y, double z, int ammo);

    public Server spawnWeapon(int weaponId, double x, double y, double z, int ammo) {
        if (isOnMainThread()) {
            nativeSpawnWeapon(weaponId, x, y, z, ammo);
        } else {
            mainThread(() -> {
                nativeSpawnWeapon(weaponId, x, y, z, ammo);
            });
        }
        return this;
    }

    public Server spawnWeapon(int weaponId, Vector position, int ammo) {
        if (isOnMainThread()) {
            nativeSpawnWeapon(weaponId, position.x, position.y, position.z, ammo);
        } else {
            mainThread(() -> {
                nativeSpawnWeapon(weaponId, position.x, position.y, position.z, ammo);
            });
        }
        return this;
    }

    private native void nativeGiveWeaponToPlayer(int id, int PlayerId, int ammo);

    public Server giveWeaponToPlayer(Player player, int weaponId, int ammo) {
        return this.giveWeaponToPlayer(player.getId(), weaponId, ammo);
    }

    public Server giveWeaponToPlayer(int playerId, int weaponId, int ammo) {
        if (isOnMainThread()) {
            nativeGiveWeaponToPlayer(weaponId, playerId, ammo);
        } else {
            mainThread(() -> {
                nativeGiveWeaponToPlayer(weaponId, playerId, ammo);
            });
        }
        return this;
    }

    public <T> T findClosestTo(List<? extends GameEntity> entities, Vector position) {
        return (T) entities.stream().min(Comparator.comparingDouble(ent -> {
            return position.distanceTo(ent.getPosition());
        })).orElse(null);
    }

    private native Zone nativeGetZone(int id);

    private native float nativeGetTime();

    public float getTime() {
        return threadIsValid() ? nativeGetTime() : 0;
    }

    private native float nativeSetTime(float time);

    public Server setTime(float time) {
        if (isOnMainThread()) {
            nativeSetTime(time);
        } else {
            mainThread(() -> {
                nativeSetTime(time);
            });
        }
        return this;
    }

    private native void nativeCreateEntitySocket(int entityType, String socketName, String socketBone, double x, double y, double z, double yaw, double pitch, double roll);

    public Server createEntitySocket(GameEntityType type, EntitySocket socket) {
        if (isOnMainThread()) {
            nativeCreateEntitySocket(
                    type.value,
                    socket.getName(), socket.getBone(),
                    socket.getTransform().position.x, socket.getTransform().position.y, socket.getTransform().position.z,
                    socket.getTransform().rotation.yaw, socket.getTransform().rotation.pitch, socket.getTransform().rotation.roll
            );
        } else {
            mainThread(() -> {
                nativeCreateEntitySocket(
                        type.value,
                        socket.getName(), socket.getBone(),
                        socket.getTransform().position.x, socket.getTransform().position.y, socket.getTransform().position.z,
                        socket.getTransform().rotation.yaw, socket.getTransform().rotation.pitch, socket.getTransform().rotation.roll
                );
            });
        }
        return this;
    }

    private native void nativeWarningLog(String var1);

    public Server warningLog(String msg) {
        if (this.threadIsValid()) {
            this.nativeWarningLog(msg);
        }
        return this;
    }

    private native void nativeSendChatMessageToAll(int var1, String var2);

    public Server sendChatMessage(String message, Object... args) {
        return this.sendChatMessage(0xFFFFFFFF, message, args);
    }

    public Server sendChatMessage(String message) {
        return this.sendChatMessage(0xFFFFFFFF, message);
    }

    public Server sendChatMessage(int color, String message, Object... args) {
        String msg = CoreUtils.format(message, args);

        if (isOnMainThread()) {
            this.nativeSendChatMessageToAll(color, msg);
        } else {
            mainThread(() -> {
                this.nativeSendChatMessageToAll(color, msg);
            });
        }
        return this;
    }

    private native void nativeSendChatMessageToPlayer(int var1, int var2, String var3);

    public Server sendChatMessage(Player player, String message, Object... args) {
        return this.sendChatMessage(player.getId(), 0xFFFFFFFF, message, args);
    }

    public Server sendChatMessage(Player player, int color, String message, Object... args) {
        return this.sendChatMessage(player.getId(), color, message, args);
    }

    public Server sendChatMessage(int playerId, int color, String message, Object... args) {
        String msg = CoreUtils.format(message, args);

        if (isOnMainThread()) {
            this.nativeSendChatMessageToPlayer(playerId, color, msg);
        } else {
            mainThread(() -> {
                this.nativeSendChatMessageToPlayer(playerId, color, msg);
            });
        }
        return this;
    }

    private native Player nativeGetPlayer(int var1);

    public Player getPlayer(int id) {
        if (this.threadIsValid()) {
            Player p = nativeGetPlayer(id);
            if (p == null || !p.isValid()) {
                return null;
            } else {
                return p;
            }
        }
        return null;
    }

    public GameEntity getGameEntity(int type, int id) {
        GameEntityType t = GameEntityType.value(type);

        if (t != null) {
            switch (t) {
                case PLAYER:
                    return getPlayer(id);
                case VEHICLE:
                    return getVehicle(id);
                case NPC:
                    return getNPC(id);
                case OBJECT:
                    return getObject(id);
                case ZONE:
                    return getZone(id);
            }
        }
        return null;
    }

    private native Vehicle nativeGetVehicle(int id);

    public Vehicle getVehicle(int id) {
        if (this.threadIsValid()) {
            Vehicle v = nativeGetVehicle(id);
            if (v == null || !v.isValid()) {
                return null;
            } else {
                return v;
            }
        }
        return null;
    }

    private native NPC nativeGetNPC(int ID);

    public NPC getNPC(int id) {
        if (this.threadIsValid()) {
            NPC npc = nativeGetNPC(id);
            if (npc == null || !npc.isValid()) {
                return null;
            } else {
                return npc;
            }
        }
        return null;
    }

    private native GameObject nativeGetObject(int ID);

    public GameObject getObject(int id) {
        if (this.threadIsValid()) {
            GameObject o = nativeGetObject(id);
            if (o == null || !o.isValid()) {
                return null;
            } else {
                return o;
            }
        }
        return null;
    }

    public Zone getZone(int id) {
        if (this.threadIsValid()) {
            Zone z = nativeGetZone(id);

            if (z == null || !z.isValid()) {
                return null;
            } else {
                return z;
            }
        }
        return null;
    }

    private native int nativeSpawnNPC(int NPCType, int Subtype, double x, double y, double z, double angle);

    public NPC spawnNPC(NPCSpawnProps SpawnProperties) {
        if (threadIsValid()) {
            int type = SpawnProperties.getType().ordinal() + 1;
            int subtype = SpawnProperties.getSubtype();
            Integer id = nativeSpawnNPC(type, subtype, SpawnProperties.getSpawnpoint().getX(), SpawnProperties.getSpawnpoint().getY(), SpawnProperties.getSpawnpoint().getZ(), SpawnProperties.getSpawnpoint().getYawAngle());
            if (id > 0) {
                NPC npc = getNPC(id);
                if (SpawnProperties.getControllerClass() != null) {
                    npc.setController(SpawnProperties.getControllerClass());
                    if (npc.getController() != null) {
                        npc.getController().onCreated();
                    }
                }
                return npc;
            }
        }
        return null;
    }

    private native int nativeSpawnObject(int modelId, double x, double y, double z, double yaw, double pitch, double roll, boolean withCollision, boolean damageable);

    public GameObject spawnObject(ObjectSpawnProps props) {
        if (threadIsValid()) {
            Integer id = nativeSpawnObject(props.getModelId(), props.getTransform().position.x, props.getTransform().position.y, props.getTransform().position.z, props.getTransform().rotation.yaw, props.getTransform().rotation.pitch, props.getTransform().rotation.roll, props.isCollisionEnabled(), props.isDamageable());
            if (id > 0) {
                GameObject o = getObject(id);
                return o;
            }
        }
        return null;
    }

    private native int nativeSpawnZone(int type, double x, double y, double z, double yaw, double pitch, double roll, double sizeX, double sizeY, double height, int color);

    public Zone spawnZone(ZoneSpawnProps spawnProps) {
        if (threadIsValid()) {
            Vector pos = spawnProps.getTransform().position;
            Rotation rot = spawnProps.getTransform().rotation;
            Integer id = nativeSpawnZone(spawnProps.getType().value, pos.x, pos.y, pos.z, rot.yaw, rot.pitch, rot.roll, spawnProps.getSizeX(), spawnProps.getSizeY(), spawnProps.getHeight(), spawnProps.getColor());

            if (id > 0) {
                Zone z = nativeGetZone(id);
                z.zoneType = spawnProps.getType();
                return z;
            }
        }
        return null;
    }

    private native void nativeSendData(int playerID, String channel, String data);

    public Server sendData(Player p, String channel, String data) {
        sendData(p.getId(), channel, data);
        return this;
    }

    public Server sendData(int playerID, String channel, String data) {
        if (data != null) {
            if (!isOnMainThread()) {
                mainThread(() -> {
                    nativeSendData(playerID, channel, data);
                });
            } else {
                nativeSendData(playerID, channel, data);
            }
        } else {
            Log.exception("Unable to send client data to player: %d, channel: %s (Invalid data)", playerID, channel);
        }
        return this;
    }

    public Server sendDataToAll(String channel, String data) {
        if (data != null) {
            if (!isOnMainThread()) {
                mainThread(() -> {
                    players().forEach(p -> {
                        nativeSendData(p.getId(), channel, data);
                    });
                });
            } else {
                players().forEach(p -> {
                    nativeSendData(p.getId(), channel, data);
                });
            }
        } else {
            Log.exception("Unable to send client data to all players, channel: %s (Invalid data)", channel);
        }
        return this;
    }

    public List<GameTimer> getAllTimers() {
        return Collections.unmodifiableList(InternalServerEvents.timers);

    }

    public GameTimer getTimer(String name) {
        return InternalServerEvents.timers.stream().filter(t -> t.getName().equals(name)).findFirst().orElse(null);
    }

    public GameTimer createTimer(String name, long interval, boolean recurring, Consumer<GameTimer> r) {
        if (threadIsValid()) {
            if (InternalServerEvents.timers.stream().anyMatch(t -> t.getName().equalsIgnoreCase(name))) {
                Log.exception("Unable to create timer with name: %s (Timer already exists)", name);
                return null;
            }
            GameTimer timer = new GameTimer(name, recurring, interval, r);
            timerCounter++;
            mainThread(() -> {
                InternalServerEvents.timers.add(timer);
            });
            return timer;
        } else {
            return null;
        }
    }

    public GameTimer createTimer(long interval, boolean recurring, Consumer<GameTimer> r) {
        if (threadIsValid()) {
            GameTimer timer = new GameTimer(System.nanoTime() + timerCounter + "", recurring, interval, r);
            timerCounter++;
            mainThread(() -> {
                InternalServerEvents.timers.add(timer);
            });
            return timer;
        } else {
            return null;
        }
    }

    public GameTimer repeatUntil(long interval, Consumer<GameTimer> r, Predicate<GameTimer> stopCondition) {
        if (threadIsValid()) {
            GameTimer timer = new GameTimer(System.nanoTime() + timerCounter + "", true, interval, r, stopCondition);
            timerCounter++;
            mainThread(() -> {
                InternalServerEvents.timers.add(timer);
            });
            return timer;
        } else {
            return null;
        }
    }

    public GameTimer delay(long delay, Consumer<GameTimer> r) {
        if (threadIsValid()) {

            GameTimer timer = new GameTimer(System.nanoTime() + timerCounter + "", false, delay, r);
            timerCounter++;
            mainThread(() -> {
                InternalServerEvents.timers.add(timer);
            });

            return timer;
        } else {
            return null;
        }
    }

    public GameTimer every(long interval, Consumer<GameTimer> r) {
        if (threadIsValid()) {
            GameTimer timer = new GameTimer(System.nanoTime() + timerCounter + "", true, interval, r);
            timerCounter++;
            mainThread(() -> {
                InternalServerEvents.timers.add(timer);
            });
            return timer;
        } else {
            return null;
        }
    }

    private native Vector nativeGetRandomPoint(double x, double y, double z, double radius);

    public Vector getRandomPoint(Vector origin, double radius) {
        if (threadIsValid()) {
            return nativeGetRandomPoint(origin.getX(), origin.getY(), origin.getZ(), radius);
        }
        return null;
    }

    private native Rotation nativeFindRotationLookingAt(double fromX, double fromY, double fromZ, double toX, double toY, double toZ);

    public Rotation findRotationLookingAt(Vector from, Vector to) {
        if (threadIsValid()) {
            return nativeFindRotationLookingAt(from.x, from.y, from.z, to.x, to.y, to.z);
        }
        return null;
    }

    private native Vector nativeFindDirectionLookingAt(double fromX, double fromY, double fromZ, double toX, double toY, double toZ);

    public Vector findDirectionLookingAt(Vector from, Vector to) {
        if (threadIsValid()) {
            return nativeFindDirectionLookingAt(from.x, from.y, from.z, to.x, to.y, to.z);
        }
        return null;
    }

    private native void nativePutNpcInVehicle(int id, int VehicleId);

    public Server putNpcInVehicle(int npcId, int VehicleId) {
        if (isOnMainThread()) {
            nativePutNpcInVehicle(npcId, VehicleId);
        } else {
            mainThread(() -> {
                nativePutNpcInVehicle(npcId, VehicleId);
            });
        }
        return this;
    }

    public Server putNpcInVehicle(NPC npc, Vehicle veh) {
        putNpcInVehicle(npc.getId(), veh.getId());
        return this;
    }
}
