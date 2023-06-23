package com.github.newk5.vf.server.core;

import com.github.newk5.vf.server.core.commands.CommandRegistry;
import com.github.newk5.vf.server.core.controllers.BaseEventController;
import com.github.newk5.vf.server.core.entities.*;
import com.github.newk5.vf.server.core.entities.gameobject.EntitySocket;
import com.github.newk5.vf.server.core.entities.gameobject.GameObject;
import com.github.newk5.vf.server.core.entities.gameobject.ObjectSpawnProps;
import com.github.newk5.vf.server.core.entities.npc.NPC;
import com.github.newk5.vf.server.core.entities.npc.NPCSpawnProps;
import com.github.newk5.vf.server.core.entities.player.Player;
import com.github.newk5.vf.server.core.entities.vehicle.Vehicle;
import com.github.newk5.vf.server.core.exceptions.InvalidThreadException;
import com.github.newk5.vf.server.core.utils.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class Server {

    private long threadId = Thread.currentThread().getId();
    private long timerCounter;
    public CommandRegistry commandRegistry;

    protected Server() {
        commandRegistry = new CommandRegistry();
    }

    public BaseEventController getEventHandler(String name) {
        return PluginLoader.baseEvents.getEventHandler(name);
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

    public List<Player> getAllPlayers() {
        return new ArrayList<>(InternalServerEvents.allPlayers);
    }

    public List< NPC> getAllNPCs() {
        return new ArrayList<>(InternalServerEvents.allNpcs);

    }

    public void mainThread(Runnable r) {
        InternalServerEvents.queue.offer(r);
    }

    public List<Vehicle> getAllVehicles() {
        return new ArrayList<>(InternalServerEvents.allVehicles);
    }

    public List<GameObject> getAllObjects() {
        return new ArrayList<>(InternalServerEvents.allObjects);
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

    public void forEachObject(Consumer<GameObject> obj) {
        for (GameObject o : InternalServerEvents.allObjects) {
            obj.accept(o);
        }
    }

    private native float nativeGetTime();

    public float getTime() {
        if (threadIsValid()) {
            return nativeGetTime();
        }
        return 0;
    }

    private native float nativeSetTime(float time);

    public void setTime(float time) {
        if (isOnMainThread()) {
            nativeSetTime(time);
        } else {
            mainThread(() -> {
                nativeSetTime(time);
            });
        }
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
            InternalServerEvents.server.mainThread(() -> {
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

    public Server sendChatMessage(int color, String message) {
        if (isOnMainThread()) {
            this.nativeSendChatMessageToAll(color, message);
        } else {
            mainThread(() -> {
                this.nativeSendChatMessageToAll(color, message);
            });
        }
        return this;
    }

    public Server sendChatMessage(String message) {
        if (isOnMainThread()) {
            int color = -1;
            this.nativeSendChatMessageToAll(color, message);
        } else {
            mainThread(() -> {
                int color = -1;
                this.nativeSendChatMessageToAll(color, message);
            });
        }
        return this;
    }

    private native void nativeSendChatMessageToPlayer(int var1, int var2, String var3);

    public Server sendChatMessage(Player p, String message, int color) {
        if (isOnMainThread()) {
            int id = p.getId();
            this.nativeSendChatMessageToPlayer(id, color, message);
        } else {
            mainThread(() -> {
                int id = p.getId();
                this.nativeSendChatMessageToPlayer(id, color, message);
            });
        }
        return this;
    }

    public Server sendChatMessage(Player p, String message) {
        if (isOnMainThread()) {
            int color = -1;
            this.nativeSendChatMessageToPlayer(p.getId(), color, message);
        } else {
            mainThread(() -> {
                int color = -1;
                this.nativeSendChatMessageToPlayer(p.getId(), color, message);
            });
        }
        return this;
    }

    private native Player nativeGetPlayer(int var1);

    public Player getPlayer(int id) {
        return this.threadIsValid() ? this.nativeGetPlayer(id) : null;
    }

    public GameEntity getGameEntity(int type, int id) {
        GameEntityType t = GameEntityType.value(type);

        if (t != null) {
            switch (t) {
                case PLAYER:
                    return InternalServerEvents.server.getPlayer(id);
                case VEHICLE:
                    return InternalServerEvents.server.getVehicle(id);
                case NPC:
                    return InternalServerEvents.server.getNPC(id);
                case OBJECT:
                    return InternalServerEvents.server.getObject(id);
            }
        }
        return null;
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

    private native Vehicle nativeGetVehicle(int id);

    public Vehicle getVehicle(int id) {
        return this.threadIsValid() ? this.nativeGetVehicle(id) : null;
    }

    private native NPC nativeGetNPC(int ID);

    public NPC getNPC(int id) {
        return this.threadIsValid() ? this.nativeGetNPC(id) : null;
    }

    private native GameObject nativeGetObject(int ID);

    public GameObject getObject(int id) {
        return this.threadIsValid() ? this.nativeGetObject(id) : null;
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

    private native void nativeSendData(int playerID, String channel, String data);

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

    public GameTimer createTimer(String name, long interval, boolean recurring, Consumer<GameTimer> r) {
        if (threadIsValid()) {
            if (InternalServerEvents.timers.stream().anyMatch(t -> t.getName().equalsIgnoreCase(name))) {
                Log.exception("Unable to create timer with name: %s (Timer already exists)", name);
                return null;
            }
            GameTimer timer = new GameTimer(name, recurring, interval, r);
            timerCounter++;
            InternalServerEvents.timers.add(timer);
            return timer;
        } else {
            return null;
        }
    }

    public List<GameTimer> getAllTimers() {
        return Collections.unmodifiableList(InternalServerEvents.timers);

    }

    public GameTimer getTimer(String name) {
        return InternalServerEvents.timers.stream().filter(t -> t.getName().equals(name)).findFirst().orElse(null);
    }

    public GameTimer createTimer(long interval, boolean recurring, Consumer<GameTimer> r) {
        if (threadIsValid()) {
            GameTimer timer = new GameTimer(System.nanoTime() + timerCounter + "", recurring, interval, r);
            timerCounter++;
            InternalServerEvents.timers.add(timer);
            return timer;
        } else {
            return null;
        }
    }

    public GameTimer repeatUntil(long interval, Consumer<GameTimer> r, Predicate<GameTimer> stopCondition) {
        if (threadIsValid()) {
            GameTimer timer = new GameTimer(System.nanoTime() + timerCounter + "", true, interval, r, stopCondition);
            timerCounter++;
            InternalServerEvents.timers.add(timer);
            return timer;
        } else {
            return null;
        }
    }

    public GameTimer delay(long delay, Consumer<GameTimer> r) {
        if (threadIsValid()) {
            GameTimer timer = new GameTimer(System.nanoTime() + timerCounter + "", false, delay, r);
            timerCounter++;
            InternalServerEvents.timers.add(timer);
            return timer;
        } else {
            return null;
        }
    }

    public GameTimer every(long interval, Consumer<GameTimer> r) {
        if (threadIsValid()) {
            GameTimer timer = new GameTimer(System.nanoTime() + timerCounter + "", true, interval, r);
            timerCounter++;
            InternalServerEvents.timers.add(timer);
            return timer;
        } else {
            return null;
        }
    }

    public Server sendData(Player p, String channel, String data) {
        sendData(p.getId(), channel, data);
        return this;
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
}
