package com.github.newk5.vf.server.core;

import com.github.newk5.vf.server.core.controllers.BaseEventController;
import com.github.newk5.vf.server.core.controllers.ClientChannelController;
import com.github.newk5.vf.server.core.controllers.NPCController;
import com.github.newk5.vf.server.core.entities.GameEntity;
import com.github.newk5.vf.server.core.entities.GameEntityType;
import com.github.newk5.vf.server.core.entities.Vector;
import com.github.newk5.vf.server.core.entities.gameobject.GameObject;
import com.github.newk5.vf.server.core.entities.npc.NPC;
import com.github.newk5.vf.server.core.entities.npc.NPCAction;
import com.github.newk5.vf.server.core.entities.player.Player;
import com.github.newk5.vf.server.core.entities.vehicle.Vehicle;
import com.github.newk5.vf.server.core.events.EventName;
import com.github.newk5.vf.server.core.events.Events;
import com.github.newk5.vf.server.core.events.damage.DamageEvent;
import com.github.newk5.vf.server.core.utils.Log;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class InternalServerEvents {

    protected Map<String, BaseEventController> eventHandlers = new LinkedHashMap<>();
    public static Map<String, ClientChannelController> channelControllers = new HashMap<>();
    private static DamageEvent damageEvent = new DamageEvent();
    public static Server server;
    protected static List<Player> allPlayers = new ArrayList<>();
    protected static List<Vehicle> allVehicles = new ArrayList<>();
    protected static List<GameObject> allObjects = new ArrayList<>();
    protected static List<NPC> allNpcs = new ArrayList<>();
    protected static final Queue<Runnable> queue = new ConcurrentLinkedQueue<>();
    protected static List<GameTimer> timers = new ArrayList<>();
    public static Consumer<Exception> onException;
    private Vector cachedNPCNoiseVector = new Vector();
    private static List<Integer> playersWhoLeftTheServer = new ArrayList<>();
    private static List<Integer> vehiclesDestroyed = new ArrayList<>();
    private static List<Integer> objectsDestroyed = new ArrayList<>();
    private static List<Integer> npcsDestroyed = new ArrayList<>();

    public InternalServerEvents() {
    }

    protected void clearData() {
        playersWhoLeftTheServer.clear();
        vehiclesDestroyed.clear();
        objectsDestroyed.clear();
        npcsDestroyed.clear();
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

    public static boolean isValid(GameEntity e) {
        if (e instanceof Player) {
            return !playersWhoLeftTheServer.contains((Integer) e.getId());
        } else if (e instanceof Vehicle) {
            return !vehiclesDestroyed.contains((Integer) e.getId());
        } else if (e instanceof NPC) {
            return !npcsDestroyed.contains((Integer) e.getId());
        } else if (e instanceof GameObject) {
            return !objectsDestroyed.contains((Integer) e.getId());
        }

        return true;
    }

    public void addEventHandler(String name, BaseEventController baseEvents) {
        eventHandlers.put(name, baseEvents);
    }

    public void removeEventHandler(String name) {
        if (eventHandlers.containsKey(name)) {
            eventHandlers.remove(name);
        }
    }

    public BaseEventController getEventHandler(String name) {
        return eventHandlers.get(name);
    }

    public void sortEventHandlers() {

        List<BaseEventController> sorted = eventHandlers.values()
                .stream()
                .sorted(Comparator.comparing(BaseEventController::getPosition))
                .collect(Collectors.toList());

        Map<String, BaseEventController> temp = new LinkedHashMap<>();

        sorted.forEach(ev -> {
            temp.put(ev.getControllerName(), ev);
        });
        eventHandlers = temp;
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
        if (timers.isEmpty()) {
            return;
        }

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

    public void onTick() {
        try {
            processTimers();
            if (!queue.isEmpty()) {
                Runnable r = queue.poll();
                if (r != null) {
                    r.run();
                }
            }
        } catch (Exception e) {
            catchException(e);
        }

        eventHandlers.forEach((name, handler) -> {
            try {
                handler.onTick();
            } catch (Exception e) {
                catchException(e);
            }
        });
        Events.emit(EventName.onTick);
    }

    public void onServerStart() {
        playersWhoLeftTheServer.clear();
        vehiclesDestroyed.clear();
        objectsDestroyed.clear();
        npcsDestroyed.clear();
        
        eventHandlers.forEach((name, handler) -> {
            try {
                if (!handler.isDisabled()) {
                    handler.onServerStart();
                }
            } catch (Exception e) {
                catchException(e);
            }
        });
        Events.emit(EventName.onServerStart);
    }

    public void onServerShutdown() {
        eventHandlers.forEach((name, handler) -> {
            try {
                if (!handler.isDisabled()) {
                    handler.onServerShutdown();
                }
            } catch (Exception e) {
                catchException(e);
            }
        });
        Events.emit(EventName.onServerShutdown);
    }

    public void onPlayerJoin(Player player) {
        playersWhoLeftTheServer.remove((Integer) player.getId());
        allPlayers.add(player);

        eventHandlers.forEach((name, handler) -> {
            try {
                if (!handler.isDisabled()) {
                    handler.onPlayerJoin(player);
                }
            } catch (Exception e) {
                catchException(e);
            }
        });
        Events.emit(EventName.onPlayerJoin, player);
    }

    public void onPlayerLeave(Player player) {

        eventHandlers.forEach((name, handler) -> {
            try {
                if (!handler.isDisabled()) {
                    handler.onPlayerLeave(player);
                }
            } catch (Exception e) {
                catchException(e);
            }
        });
        Events.emit(EventName.onPlayerLeave, player);
        clearGameEntityData(player);
        allPlayers.remove(player);
        playersWhoLeftTheServer.add(player.getId());
    }

    public boolean onPlayerRequestSpawn(Player player) {
        for (Entry<String, BaseEventController> entry : eventHandlers.entrySet()) {
            if (!entry.getValue().isDisabled()) {
                try {
                    Boolean value = entry.getValue().onPlayerRequestSpawn(player);
                    if (value != null) {
                        Events.emit(EventName.onPlayerRequestSpawn, player);
                        return value;
                    }
                } catch (Exception e) {
                    catchException(e);
                }
            }
        }
        Boolean value = Events.request(EventName.onPlayerRequestSpawn, player);
        if (value != null) {
            return value;
        }

        return true;
    }

    public void onPlayerSpawnScreenSkinChange(Player player, int skinId) {
        eventHandlers.forEach((name, handler) -> {
            try {
                if (!handler.isDisabled()) {
                    handler.onPlayerSpawnScreenSkinChange(player, skinId);
                }
            } catch (Exception e) {
                catchException(e);
            }
        });
        Events.emit(EventName.onPlayerSpawnScreenSkinChange, player, skinId);
    }

    public void onPlayerSpawn(Player player) {
        eventHandlers.forEach((name, handler) -> {
            try {
                if (!handler.isDisabled()) {
                    handler.onPlayerSpawn(player);
                }
            } catch (Exception e) {
                catchException(e);
            }
        });
        Events.emit(EventName.onPlayerSpawn, player);
    }

    public void onPlayerDied(Player player, int source, int sourceId, int damagedByEntity, int damagedById, float damageToApply) {
        damageEvent.applyValues(source, sourceId, damagedByEntity, damagedById, damageToApply);

        eventHandlers.forEach((name, handler) -> {
            try {
                if (!handler.isDisabled()) {
                    handler.onPlayerDied(player, damageEvent);
                }
            } catch (Exception e) {
                catchException(e);
            }
        });
        Events.emit(EventName.onPlayerDied, player, damageEvent);
    }

    public boolean onPlayerMessage(Player player, String message) {
        for (Entry<String, BaseEventController> entry : eventHandlers.entrySet()) {
            if (!entry.getValue().isDisabled()) {
                try {
                    Boolean value = entry.getValue().onPlayerMessage(player, message);
                    if (value != null) {
                        Events.emit(EventName.onPlayerMessage, player, message);
                        return value;
                    }
                } catch (Exception e) {
                    catchException(e);
                }
            }
        }
        Boolean value = Events.request(EventName.onPlayerMessage, player, message);
        if (value != null) {
            return value;
        }

        return true;
    }

    public void onPlayerCommand(Player player, String message) {
        try {
            server.commandRegistry.process(player, message);
        } catch (Exception ignored) {
        }

        eventHandlers.forEach((name, handler) -> {
            try {
                if (!handler.isDisabled()) {
                    handler.onPlayerCommand(player, message);
                }
            } catch (Exception e) {
                catchException(e);
            }
        });
        Events.emit(EventName.onPlayerCommand, player, message);
    }

    public void onDataReceived(Player player, String channel, String data) {
        ClientChannelController c = channelControllers.get(channel);

        if (c == null) {
            eventHandlers.forEach((name, handler) -> {
                try {
                    if (!handler.isDisabled()) {
                        handler.onDataReceived(player, channel, data);
                    }
                } catch (Exception e) {
                    catchException(e);
                }
            });
            Events.emit(EventName.onDataReceived, player, channel, data);
        } else {
            c.receiveData(player, data);
        }
    }

    public float onPlayerReceiveDamage(Player player, int source, int sourceId, int damagedByEntity, int damagedById, float damageToApply) {
        damageEvent.applyValues(source, sourceId, damagedByEntity, damagedById, damageToApply);

        return onPlayerReceiveDamage(player, damageEvent);
    }

    public Float onPlayerReceiveDamage(Player player, DamageEvent damageEvent) {
        for (Entry<String, BaseEventController> entry : eventHandlers.entrySet()) {
            if (!entry.getValue().isDisabled()) {
                try {
                    Float newValue = entry.getValue().onPlayerReceiveDamage(player, damageEvent);
                    if (newValue != null) {
                        Events.emit(EventName.onPlayerReceiveDamage, player, damageEvent);
                        return newValue;
                    }
                } catch (Exception e) {
                    catchException(e);
                }
            }
        }
        Float newValue = Events.request(EventName.onPlayerReceiveDamage, player, damageEvent);
        if (newValue != null) {
            return newValue;
        }

        return damageEvent.getDamageToApply();
    }

    public void onPlayerEnterVehicle(Player player, Vehicle vehicle, boolean asDriver) {
        eventHandlers.forEach((name, handler) -> {
            try {
                if (!handler.isDisabled()) {
                    handler.onPlayerEnterVehicle(player, vehicle, asDriver);
                }
            } catch (Exception e) {
                catchException(e);
            }
        });
        Events.emit(EventName.onPlayerEnterVehicle, player, vehicle, asDriver);
    }

    public void onPlayerLeaveVehicle(Player player, Vehicle vehicle, boolean asDriver) {
        eventHandlers.forEach((name, handler) -> {
            try {
                if (!handler.isDisabled()) {
                    handler.onPlayerLeaveVehicle(player, vehicle, asDriver);
                }
            } catch (Exception e) {
                catchException(e);
            }
        });
        Events.emit(EventName.onPlayerLeaveVehicle, player, vehicle, asDriver);
    }

    public void onVehicleCreated(Vehicle vehicle) {
        vehiclesDestroyed.remove((Integer) vehicle.getId());
        allVehicles.add(vehicle);

        eventHandlers.forEach((name, handler) -> {
            try {
                if (!handler.isDisabled()) {
                    handler.onVehicleCreated(vehicle);
                }
            } catch (Exception e) {
                catchException(e);
            }
        });
        Events.emit(EventName.onVehicleCreated, vehicle);
    }

    public void onVehicleDestroyed(Vehicle vehicle) {
        eventHandlers.forEach((name, handler) -> {
            try {
                if (!handler.isDisabled()) {
                    handler.onVehicleDestroyed(vehicle);
                }
            } catch (Exception e) {
                catchException(e);
            }
        });
        Events.emit(EventName.onVehicleDestroyed, vehicle);

        clearGameEntityData(vehicle);
        allVehicles.remove(vehicle);
        vehiclesDestroyed.add(vehicle.getId());
    }

    public void onVehicleExploded(Vehicle vehicle) {
        eventHandlers.forEach((name, handler) -> {
            try {
                if (!handler.isDisabled()) {
                    handler.onVehicleExploded(vehicle);
                }
            } catch (Exception e) {
                catchException(e);
            }
        });
        Events.emit(EventName.onVehicleExploded, vehicle);
    }

    public float onVehicleReceiveDamage(Vehicle vehicle, int source, int sourceId, int damagedByEntity, int damagedById, float damageToApply) {
        damageEvent.applyValues(source, sourceId, damagedByEntity, damagedById, damageToApply);

        return onVehicleReceiveDamage(vehicle, damageEvent);
    }

    public Float onVehicleReceiveDamage(Vehicle vehicle, DamageEvent damageEvent) {
        for (Entry<String, BaseEventController> entry : eventHandlers.entrySet()) {
            if (!entry.getValue().isDisabled()) {
                try {
                    Float newValue = entry.getValue().onVehicleReceiveDamage(vehicle, damageEvent);
                    if (newValue != null) {
                        Events.emit(EventName.onVehicleReceiveDamage, vehicle, damageEvent);
                        return newValue;
                    }
                } catch (Exception e) {
                    catchException(e);
                }
            }
        }
        Float newValue = Events.request(EventName.onVehicleReceiveDamage, vehicle, damageEvent);
        if (newValue != null) {
            return newValue;
        }

        return damageEvent.getDamageToApply();
    }

    public void onNPCCreated(NPC npc) {
        npcsDestroyed.remove((Integer) npc.getId());
        allNpcs.add(npc);

        NPCController c = npc.getController();
        if (c == null) {
            eventHandlers.forEach((name, handler) -> {
                try {
                    if (!handler.isDisabled()) {
                        handler.onNpcCreated(npc);
                    }
                } catch (Exception e) {
                    catchException(e);
                }
            });
            Events.emit(EventName.onNpcCreated, npc);
        } else {
            c.onCreated();
        }
    }

    public void onNPCDestroyed(NPC npc) {
        NPCController c = npc.getController();

        if (c == null) {
            eventHandlers.forEach((name, handler) -> {
                try {
                    if (!handler.isDisabled()) {
                        handler.onNpcDestroy(npc);
                    }
                } catch (Exception e) {
                    catchException(e);
                }
            });
            Events.emit(EventName.onNpcDestroy, npc);
        } else {
            c.onDestroy();
        }
        clearGameEntityData(npc);
        allNpcs.remove(npc);
        npcsDestroyed.add(npc.getId());
    }

    public void onNPCSpawned(NPC npc) {
        NPCController c = npc.getController();

        if (c == null) {
            eventHandlers.forEach((name, handler) -> {
                try {
                    if (!handler.isDisabled()) {
                        handler.onNpcSpawned(npc);
                    }
                } catch (Exception e) {
                    catchException(e);
                }
            });
            Events.emit(EventName.onNpcSpawned, npc);
        } else {
            c.onSpawned();
        }
    }

    public void onNPCDied(NPC npc, int source, int sourceId, int damagedByEntity, int damagedById, float damageToApply) {
        damageEvent.applyValues(source, sourceId, damagedByEntity, damagedById, damageToApply);

        onNpcDied(npc, damageEvent);
    }

    public void onNpcDied(NPC npc, DamageEvent damageEvent) {
        NPCController c = npc.getController();

        if (c == null) {
            eventHandlers.forEach((name, handler) -> {
                try {
                    if (!handler.isDisabled()) {
                        handler.onNpcDied(npc, damageEvent);
                    }
                } catch (Exception e) {
                    catchException(e);
                }
            });
            Events.emit(EventName.onNpcDied, npc, damageEvent);
        } else {
            c.onDeath(damageEvent);
        }
    }

    public float onNPCReceiveDamage(NPC npc, int source, int sourceId, int damagedByEntity, int damagedById, float damageToApply) {
        damageEvent.applyValues(source, sourceId, damagedByEntity, damagedById, damageToApply);

        return onNpcReceiveDamage(npc, damageEvent);
    }

    public Float onNpcReceiveDamage(NPC npc, DamageEvent damageEvent) {
        NPCController c = npc.getController();

        if (c == null) {
            for (Entry<String, BaseEventController> entry : eventHandlers.entrySet()) {
                if (!entry.getValue().isDisabled()) {
                    try {
                        Float newValue = entry.getValue().onNpcReceiveDamage(npc, damageEvent);
                        if (newValue != null) {
                            Events.emit(EventName.onNpcReceiveDamage, npc, damageEvent);
                            return newValue;
                        }
                    } catch (Exception e) {
                        catchException(e);
                    }
                }
            }
            Float newValue = Events.request(EventName.onNpcReceiveDamage, npc, damageEvent);
            if (newValue != null) {
                return newValue;
            }
        } else {
            Float newValue = c.onReceiveDamage(damageEvent);
            if (newValue != null) {
                return newValue;
            }
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
                try {
                    if (!handler.isDisabled()) {
                        handler.onNpcActionChanged(npc, oldAction, newAction);
                    }
                } catch (Exception e) {
                    catchException(e);
                }
            });
            Events.emit(EventName.onNpcActionChanged, npc, oldAction, newAction);
        } else {
            c.onActionChanged(oldAction, newAction);
        }
    }

    public boolean onNPCGainedSightOf(NPC npc, int entityType, int entityId) {
        GameEntity entity = server.getGameEntity(entityType, entityId);

        if (entity != null) {
            return onNpcGainedSightOf(npc, entity);
        }
        return true;
    }

    public Boolean onNpcGainedSightOf(NPC npc, GameEntity entity) {
        NPCController c = npc.getController();

        if (c == null) {
            for (Entry<String, BaseEventController> entry : eventHandlers.entrySet()) {
                if (!entry.getValue().isDisabled()) {
                    try {
                        Boolean value = entry.getValue().onNpcGainedSightOf(npc, entity);
                        if (value != null) {
                            Events.emit(EventName.onNpcGainedSightOf, npc, entity);
                            return value;
                        }
                    } catch (Exception e) {
                        catchException(e);
                    }
                }
            }
            Boolean value = Events.request(EventName.onNpcGainedSightOf, npc, entity);
            if (value != null) {
                return value;
            }
        } else {
            c.onGainedSightOf(entity);
            return false;
        }
        return true;
    }

    public boolean onNPCLostSightOf(NPC npc, int entityType, int entityId) {
        GameEntity entity = server.getGameEntity(entityType, entityId);

        if (entity != null) {
            return onNpcLostSightOf(npc, entity);
        }
        return true;
    }

    public Boolean onNpcLostSightOf(NPC npc, GameEntity entity) {
        NPCController c = npc.getController();

        if (c == null) {
            for (Entry<String, BaseEventController> entry : eventHandlers.entrySet()) {
                if (!entry.getValue().isDisabled()) {
                    try {
                        Boolean value = entry.getValue().onNpcLostSightOf(npc, entity);
                        if (value != null) {
                            Events.emit(EventName.onNpcLostSightOf, npc, entity);
                            return value;
                        }
                    } catch (Exception e) {
                        catchException(e);
                    }
                }
            }
            Boolean value = Events.request(EventName.onNpcLostSightOf, npc, entity);
            if (value != null) {
                return value;
            }
        } else {
            c.onLostSightOf(entity);
            return false;
        }
        return true;
    }

    public boolean onNPCHeardNoise(NPC npc, double x, double y, double z) {
        cachedNPCNoiseVector.set(x, y, z);

        NPCController c = npc.getController();
        if (c == null) {
            for (Entry<String, BaseEventController> entry : eventHandlers.entrySet()) {
                if (!entry.getValue().isDisabled()) {
                    try {
                        Boolean value = entry.getValue().onNpcHeardNoise(npc, cachedNPCNoiseVector);
                        if (value != null) {
                            Events.emit(EventName.onNpcHeardNoise, npc, cachedNPCNoiseVector);
                            return value;
                        }
                    } catch (Exception e) {
                        catchException(e);
                    }
                }
            }
            Boolean value = Events.request(EventName.onNpcHeardNoise, npc, cachedNPCNoiseVector);
            if (value != null) {
                return value;
            }
        } else {
            c.onHeardNoise(cachedNPCNoiseVector);
            return false;
        }
        return true;
    }

    public void onObjectCreated(GameObject obj) {
        objectsDestroyed.remove((Integer) obj.getId());
        allObjects.add(obj);

        eventHandlers.forEach((name, handler) -> {
            try {
                if (!handler.isDisabled()) {
                    handler.onObjectCreated(obj);
                }
            } catch (Exception e) {
                catchException(e);
            }
        });
        Events.emit(EventName.onObjectCreated, obj);
    }

    public void onObjectDestroyed(GameObject obj) {
        eventHandlers.forEach((name, handler) -> {
            try {
                if (!handler.isDisabled()) {
                    handler.onObjectDestroyed(obj);
                }
            } catch (Exception e) {
                catchException(e);
            }
        });
        Events.emit(EventName.onObjectDestroyed, obj);

        clearGameEntityData(obj);
        allObjects.remove(obj);
        objectsDestroyed.add(obj.getId());
    }

    public float onObjectReceiveDamage(GameObject obj, int source, int sourceId, int damagedByEntity, int damagedById, float damageToApply) {
        damageEvent.applyValues(source, sourceId, damagedByEntity, damagedById, damageToApply);

        return onObjectReceiveDamage(obj, damageEvent);
    }

    public Float onObjectReceiveDamage(GameObject obj, DamageEvent damageEvent) {
        for (Entry<String, BaseEventController> entry : eventHandlers.entrySet()) {
            if (!entry.getValue().isDisabled()) {
                try {
                    Float newValue = entry.getValue().onObjectReceiveDamage(obj, damageEvent);
                    if (newValue != null) {
                        Events.emit(EventName.onObjectReceiveDamage, obj, damageEvent);
                        return newValue;
                    }
                } catch (Exception e) {
                    catchException(e);
                }
            }
        }
        Float newValue = Events.request(EventName.onObjectReceiveDamage, obj, damageEvent);
        if (newValue != null) {
            return newValue;
        }

        return damageEvent.getDamageToApply();
    }

    public void onObjectTouched(GameObject obj, int entityType, int entityId) {
        GameEntity entity = server.getGameEntity(entityType, entityId);

        eventHandlers.forEach((name, handler) -> {
            try {
                if (!handler.isDisabled()) {
                    handler.onObjectTouched(obj, entity);
                }
            } catch (Exception e) {
                catchException(e);
            }
        });
        Events.emit(EventName.onObjectTouched, obj, entity);
    }

    public void onObjectBroken(GameObject obj) {
        eventHandlers.forEach((name, handler) -> {
            try {
                if (!handler.isDisabled()) {
                    handler.onObjectBroken(obj);
                }
            } catch (Exception e) {
                catchException(e);
            }
        });
        Events.emit(EventName.onObjectBroken, obj);
    }
}
