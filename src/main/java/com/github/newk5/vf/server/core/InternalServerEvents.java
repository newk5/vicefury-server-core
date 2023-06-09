package com.github.newk5.vf.server.core;

import com.github.newk5.vf.server.core.controllers.ai.NPCController;
import com.github.newk5.vf.server.core.controllers.client.ClientChannelController;
import com.github.newk5.vf.server.core.entities.GameEntity;
import com.github.newk5.vf.server.core.entities.GameEntityType;
import com.github.newk5.vf.server.core.entities.NPCAction;
import com.github.newk5.vf.server.core.entities.Vector;
import com.github.newk5.vf.server.core.entities.npc.NPC;
import com.github.newk5.vf.server.core.entities.player.Player;
import com.github.newk5.vf.server.core.entities.vehicle.Vehicle;
import com.github.newk5.vf.server.core.events.BaseServerEvents;
import com.github.newk5.vf.server.core.events.EventName;
import com.github.newk5.vf.server.core.events.Events;
import com.github.newk5.vf.server.core.events.damage.DamageEvent;
import com.github.newk5.vf.server.core.utils.Log;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;

public class InternalServerEvents {

    private Map<String, BaseServerEvents> eventHandlers = new HashMap<>();
    public static Map<String, ClientChannelController> channelControllers = new HashMap<>();
    private static DamageEvent damageEvent = new DamageEvent();
    public static Server server;
    protected static List<Player> allPlayers = new ArrayList<>();
    protected static List<Vehicle> allVehicles = new ArrayList<>();
    protected static List<NPC> allNpcs = new ArrayList<>();
    protected static final Queue<Runnable> queue = new ConcurrentLinkedQueue<>();
    protected static List<GameTimer> timers = new ArrayList<>();
    public static Consumer<Exception> onException;
    private Vector cachedNPCNoiseVector = new Vector();

    public InternalServerEvents() { }

    protected void clearData() {
        eventHandlers.clear();
        channelControllers.clear();
        allPlayers.clear();
        allVehicles.clear();
        allNpcs.clear();
        queue.clear();
        timers.clear();
        onException = null;
        server = null;
    }

    public void addEventHandler(String name, BaseServerEvents baseEvents) {
        eventHandlers.put(name, baseEvents);
    }

    public void removeEventHandler(String name) {
        if(eventHandlers.containsKey(name)){
            eventHandlers.remove(name);
        }
    }

    private void catchException(Exception e) {
        Log.exception(e);
        if (onException != null) {
            onException.accept(e);
        }
    }

    private void clearGameEntityData(GameEntity e) {
        e.clearData();
        e.setGameData(null);

        if (e instanceof NPC) {
            NPC npc = (NPC) e;
            npc.setController(null);
        } else if (e instanceof Player) {
            Player p = (Player) e;
            p.setAuthenticated(false);
        }
    }

    private void processTimers() {
        if (timers.isEmpty()) return;

        Iterator<GameTimer> it = timers.iterator();
        while (it.hasNext()) {
            GameTimer timer = it.next();
            if (timer.shouldRun()) {
                timer.process();
            }
            if (timer.isPendingCancellation() || timer.stopConditionEvalsToTrue() || timer.hasExpired()) {
                it.remove();
            }
        }
    }

    public void onServerStart() {
        eventHandlers.forEach((name, handler) -> {
            try { handler.onServerStart(); }
            catch (Exception e) { catchException(e); }
        });
        Events.emit(EventName.onServerStart);
    }

    public void onServerShutdown() {
        eventHandlers.forEach((name, handler) -> {
            try { handler.onServerShutdown(); }
            catch (Exception e) { catchException(e); }
        });
        Events.emit(EventName.onServerShutdown);
    }

    public void onTick() {
        try {
            processTimers();
            if (!queue.isEmpty()) {
                Runnable r = queue.poll();
                if (r != null) {
                    r.run();
                }
            }
        }
        catch (Exception e) { catchException(e); }

        eventHandlers.forEach((name, handler) -> {
            try { handler.onTick(); }
            catch (Exception e) { catchException(e); }
        });
        Events.emit(EventName.onTick);
    }

    public void onPlayerJoin(Player player) {
        allPlayers.add(player);

        eventHandlers.forEach((name, handler) -> {
            try { handler.onPlayerJoin(player); }
            catch (Exception e) { catchException(e); }
        });
        Events.emit(EventName.onPlayerJoin, player);
    }

    public void onPlayerLeave(Player player) {
        clearGameEntityData(player);
        allPlayers.remove(player);

        eventHandlers.forEach((name, handler) -> {
            try { handler.onPlayerLeave(player); }
            catch (Exception e) { catchException(e); }
        });
        Events.emit(EventName.onPlayerLeave, player);
    }

    public boolean onPlayerRequestSpawn(Player player) {
        for (Entry<String, BaseServerEvents> entry : eventHandlers.entrySet()) {
            try {
                Boolean value = entry.getValue().onPlayerRequestSpawn(player);
                if (value != null) {
                    Events.emit(EventName.onPlayerRequestSpawn, player);
                    return value;
                }
            }
            catch (Exception e) { catchException(e); }
        }
        Boolean value = Events.request(EventName.onPlayerRequestSpawn, player);
        if(value != null) return value;

        return true;
    }

    public void onPlayerSpawnScreenSkinChange(Player player, int skinId) {
        eventHandlers.forEach((name, handler) -> {
            try { handler.onPlayerSpawnScreenSkinChange(player, skinId); }
            catch (Exception e) { catchException(e); }
        });
        Events.emit(EventName.onPlayerSpawnScreenSkinChange, player, skinId);
    }

    public void onPlayerSpawn(Player player) {
        eventHandlers.forEach((name, handler) -> {
            try { handler.onPlayerSpawn(player); }
            catch (Exception e) { catchException(e); }
        });
        Events.emit(EventName.onPlayerSpawn, player);
    }

    public void onPlayerDied(Player player, int source, int sourceId, int damagedByEntity, int damagedById, float damageToApply) {
        damageEvent.applyValues(source, sourceId, damagedByEntity, damagedById, damageToApply);

        eventHandlers.forEach((name, handler) -> {
            try { handler.onPlayerDied(player, damageEvent); }
            catch (Exception e) { catchException(e); }
        });
        Events.emit(EventName.onPlayerDied, player, damageEvent);
    }

    public boolean onPlayerMessage(Player player, String message) {
        for (Entry<String, BaseServerEvents> entry : eventHandlers.entrySet()) {
            try {
                Boolean value = entry.getValue().onPlayerMessage(player, message);
                if (value != null) {
                    Events.emit(EventName.onPlayerMessage, player, message);
                    return value;
                }
            }
            catch (Exception e) { catchException(e); }
        }
        Boolean value = Events.request(EventName.onPlayerMessage, player, message);
        if(value != null) return value;

        return true;
    }

    public void onPlayerCommand(Player player, String message) {
        try { server.commandRegistry.process(player, message); }
        catch (Exception ignored) { }

        eventHandlers.forEach((name, handler) -> {
            try { handler.onPlayerCommand(player, message); }
            catch (Exception e) { catchException(e); }
        });
        Events.emit(EventName.onPlayerCommand, player, message);
    }

    public void onDataReceived(Player player, String channel, String data) {
        ClientChannelController c = channelControllers.get(channel);

        if (c == null) {
            eventHandlers.forEach((name, handler) -> {
                try { handler.onDataReceived(player, channel, data); }
                catch (Exception e) { catchException(e); }
            });
            Events.emit(EventName.onDataReceived, player, channel, data);
        }
        else c.receiveData(player, data);
    }

    public float onPlayerReceiveDamage(Player player, int source, int sourceId, int damagedByEntity, int damagedById, float damageToApply) {
        damageEvent.applyValues(source, sourceId, damagedByEntity, damagedById, damageToApply);

        return onPlayerReceiveDamage(player, damageEvent);
    }

    public Float onPlayerReceiveDamage(Player player, DamageEvent damageEvent) {
        for (Entry<String, BaseServerEvents> entry : eventHandlers.entrySet()) {
            try {
                Float newValue = entry.getValue().onPlayerReceiveDamage(player, damageEvent);
                if (newValue != null) {
                    Events.emit(EventName.onPlayerReceiveDamage, player, damageEvent);
                    return newValue;
                }
            }
            catch (Exception e) { catchException(e); }
        }
        Float newValue = Events.request(EventName.onPlayerReceiveDamage, player, damageEvent);
        if(newValue != null) return newValue;

        return damageEvent.getDamageToApply();
    }

    public void onPlayerEnterVehicle(Player player, Vehicle vehicle, boolean asDriver) {
        eventHandlers.forEach((name, handler) -> {
            try { handler.onPlayerEnterVehicle(player, vehicle, asDriver); }
            catch (Exception e) { catchException(e); }
        });
        Events.emit(EventName.onPlayerEnterVehicle, player, vehicle, asDriver);
    }

    public void onPlayerLeaveVehicle(Player player, Vehicle vehicle, boolean asDriver) {
        eventHandlers.forEach((name, handler) -> {
            try { handler.onPlayerLeaveVehicle(player, vehicle, asDriver); }
            catch (Exception e) { catchException(e); }
        });
        Events.emit(EventName.onPlayerLeaveVehicle, player, vehicle, asDriver);
    }

    public void onVehicleCreated(Vehicle vehicle) {
        allVehicles.add(vehicle);

        eventHandlers.forEach((name, handler) -> {
            try { handler.onVehicleCreated(vehicle); }
            catch (Exception e) { catchException(e); }
        });
        Events.emit(EventName.onVehicleCreated, vehicle);
    }

    public void onVehicleDestroyed(Vehicle vehicle) {
        clearGameEntityData(vehicle);
        allVehicles.remove(vehicle);

        eventHandlers.forEach((name, handler) -> {
            try { handler.onVehicleDestroyed(vehicle); }
            catch (Exception e) { catchException(e); }
        });
        Events.emit(EventName.onVehicleDestroyed, vehicle);
    }

    public void onVehicleExploded(Vehicle vehicle) {
        eventHandlers.forEach((name, handler) -> {
            try { handler.onVehicleExploded(vehicle); }
            catch (Exception e) { catchException(e); }
        });
        Events.emit(EventName.onVehicleExploded, vehicle);
    }

    public float onVehicleReceiveDamage(Vehicle vehicle, int source, int sourceId, int damagedByEntity, int damagedById, float damageToApply) {
        damageEvent.applyValues(source, sourceId, damagedByEntity, damagedById, damageToApply);

        return onVehicleReceiveDamage(vehicle, damageEvent);
    }

    public Float onVehicleReceiveDamage(Vehicle vehicle, DamageEvent damageEvent) {
        for (Entry<String, BaseServerEvents> entry : eventHandlers.entrySet()) {
            try {
                Float newValue = entry.getValue().onVehicleReceiveDamage(vehicle, damageEvent);
                if (newValue != null) {
                    Events.emit(EventName.onVehicleReceiveDamage, vehicle, damageEvent);
                    return newValue;
                }
            }
            catch (Exception e) { catchException(e); }
        }
        Float newValue = Events.request(EventName.onVehicleReceiveDamage, vehicle, damageEvent);
        if (newValue != null) return newValue;

        return damageEvent.getDamageToApply();
    }

    public void onNPCCreated(NPC npc) {
        allNpcs.add(npc);

        NPCController c = npc.getController();
        if(c == null) {
            eventHandlers.forEach((name, handler) -> {
                try { handler.onNpcCreated(npc); }
                catch (Exception e) { catchException(e); }
            });
            Events.emit(EventName.onNpcCreated, npc);
        }
        else c.onCreated();
    }

    public void onNPCDestroyed(NPC npc) {
        clearGameEntityData(npc);
        allNpcs.remove(npc);

        NPCController c = npc.getController();
        if(c == null) {
            eventHandlers.forEach((name, handler) -> {
                try { handler.onNpcDestroy(npc); }
                catch (Exception e) { catchException(e); }
            });
            Events.emit(EventName.onNpcDestroy, npc);
        }
        else c.onDestroy();
    }

    public void onNPCSpawned(NPC npc) {
        NPCController c = npc.getController();

        if(c == null) {
            eventHandlers.forEach((name, handler) -> {
                try { handler.onNpcSpawned(npc); }
                catch (Exception e) { catchException(e); }
            });
            Events.emit(EventName.onNpcSpawned, npc);
        }
        else c.onSpawned();
    }

    public void onNPCDied(NPC npc, int source, int sourceId, int damagedByEntity, int damagedById, float damageToApply) {
        damageEvent.applyValues(source, sourceId, damagedByEntity, damagedById, damageToApply);

        onNpcDied(npc, damageEvent);
    }

    public void onNpcDied(NPC npc, DamageEvent damageEvent) {
        NPCController c = npc.getController();

        if(c == null) {
            eventHandlers.forEach((name, handler) -> {
                try { handler.onNpcDied(npc, damageEvent); }
                catch (Exception e) { catchException(e); }
            });
            Events.emit(EventName.onNpcDied, npc, damageEvent);
        }
        else c.onDeath(damageEvent);
    }

    public float onNPCReceiveDamage(NPC npc, int source, int sourceId, int damagedByEntity, int damagedById, float damageToApply) {
        damageEvent.applyValues(source, sourceId, damagedByEntity, damagedById, damageToApply);

        return onNpcReceiveDamage(npc, damageEvent);
    }

    public Float onNpcReceiveDamage(NPC npc, DamageEvent damageEvent) {
        NPCController c = npc.getController();

        if(c == null) {
            for (Entry<String, BaseServerEvents> entry : eventHandlers.entrySet()) {
                try {
                    Float newValue = entry.getValue().onNpcReceiveDamage(npc, damageEvent);
                    if (newValue != null) {
                        Events.emit(EventName.onNpcReceiveDamage, npc, damageEvent);
                        return newValue;
                    }
                }
                catch (Exception e) { catchException(e); }
            }
            Float newValue = Events.request(EventName.onNpcReceiveDamage, npc, damageEvent);
            if (newValue != null) return newValue;
        }
        else {
            Float newValue = c.onReceiveDamage(damageEvent);
            if(newValue != null) return newValue;
        }
        return damageEvent.getDamageToApply();
    }

    public void onNPCActionChanged(NPC npc, int oldAction, int newAction) {
        onNpcActionChanged(npc, NPCAction.value(oldAction), NPCAction.value(newAction));
    }

    public void onNpcActionChanged(NPC npc, NPCAction oldAction, NPCAction newAction) {
        NPCController c = npc.getController();

        if (c == null) {
            eventHandlers.forEach((name, handler) -> {
                try { handler.onNpcActionChanged(npc, oldAction, newAction); }
                catch (Exception e) { catchException(e); }
            });
            Events.emit(EventName.onNpcActionChanged, npc, oldAction, newAction);
        }
        else c.onActionChanged(oldAction, newAction);
    }

    public boolean onNPCGainedSightOf(NPC npc, int entityType, int entityId) {
        GameEntity entity = server.getGameEntity(GameEntityType.value(entityType), entityId);

        if (entity != null) return onNpcGainedSightOf(npc, entity);
        return true;
    }

    public Boolean onNpcGainedSightOf(NPC npc, GameEntity entity) {
        NPCController c = npc.getController();

        if (c == null) {
            for (Entry<String, BaseServerEvents> entry : eventHandlers.entrySet()) {
                try {
                    Boolean value = entry.getValue().onNpcGainedSightOf(npc, entity);
                    if(value != null) {
                        Events.emit(EventName.onNpcGainedSightOf, npc, entity);
                        return value;
                    }
                }
                catch (Exception e) { catchException(e); }
            }
            Boolean value = Events.request(EventName.onNpcGainedSightOf, npc, entity);
            if(value != null) return value;
        }
        else {
            c.onGainedSightOf(entity);
            return false;
        }
        return true;
    }

    public boolean onNPCLostSightOf(NPC npc, int entityType, int entityId) {
        GameEntity entity = server.getGameEntity(GameEntityType.value(entityType), entityId);

        if (entity != null) return onNpcLostSightOf(npc, entity);
        return true;
    }

    public Boolean onNpcLostSightOf(NPC npc, GameEntity entity) {
        NPCController c = npc.getController();

        if (c == null) {
            for (Entry<String, BaseServerEvents> entry : eventHandlers.entrySet()) {
                try {
                    Boolean value = entry.getValue().onNpcLostSightOf(npc, entity);
                    if(value != null) {
                        Events.emit(EventName.onNpcLostSightOf, npc, entity);
                        return value;
                    }
                }
                catch (Exception e) { catchException(e); }
            }
            Boolean value = Events.request(EventName.onNpcLostSightOf, npc, entity);
            if(value != null) return value;
        }
        else {
            c.onLostSightOf(entity);
            return false;
        }
        return true;
    }

    public boolean onNPCHeardNoise(NPC npc, double x, double y, double z) {
        cachedNPCNoiseVector.set(x, y, z);

        NPCController c = npc.getController();
        if (c == null) {
            for (Entry<String, BaseServerEvents> entry : eventHandlers.entrySet()) {
                try {
                    Boolean value = entry.getValue().onNpcHeardNoise(npc, cachedNPCNoiseVector);
                    if(value != null) {
                        Events.emit(EventName.onNpcHeardNoise, npc, cachedNPCNoiseVector);
                        return value;
                    }
                }
                catch (Exception e) { catchException(e); }
            }
            Boolean value = Events.request(EventName.onNpcHeardNoise, npc, cachedNPCNoiseVector);
            if(value != null) return value;
        }
        else {
            c.onHeardNoise(cachedNPCNoiseVector);
            return false;
        }
        return true;
    }
}