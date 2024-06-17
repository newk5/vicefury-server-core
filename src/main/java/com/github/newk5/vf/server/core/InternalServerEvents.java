package com.github.newk5.vf.server.core;

import com.github.newk5.vf.server.core.controllers.BaseEventController;
import com.github.newk5.vf.server.core.controllers.ClientChannelController;
import com.github.newk5.vf.server.core.controllers.NPCController;
import com.github.newk5.vf.server.core.entities.GameEntity;
import com.github.newk5.vf.server.core.entities.Vector;
import com.github.newk5.vf.server.core.entities.Weapon;
import com.github.newk5.vf.server.core.entities.gameobject.GameObject;
import com.github.newk5.vf.server.core.entities.npc.NPC;
import com.github.newk5.vf.server.core.entities.npc.NPCAction;
import com.github.newk5.vf.server.core.entities.npc.NPCType;
import com.github.newk5.vf.server.core.entities.player.Player;
import com.github.newk5.vf.server.core.entities.vehicle.Vehicle;
import com.github.newk5.vf.server.core.entities.zone.Zone;
import com.github.newk5.vf.server.core.entities.zone.ZoneType;
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

    public static Server server;
    protected Map<String, BaseEventController> eventHandlers = new LinkedHashMap<>();
    public static Map<String, ClientChannelController> channelControllers = new HashMap<>();
    protected static List<Player> allPlayers = new ArrayList<>();
    protected static List<NPC> allNpcs = new ArrayList<>();
    protected static List<Vehicle> allVehicles = new ArrayList<>();
    protected static List<GameObject> allObjects = new ArrayList<>();
    protected static List<Zone> allZones = new ArrayList<>();
    protected static final Queue<Runnable> queue = new ConcurrentLinkedQueue<>();
    protected static List<GameTimer> timers = new ArrayList<>();
    public static Consumer<Exception> onException;
    private static DamageEvent damageEvent = new DamageEvent();
    private Vector cachedNPCNoiseVector = new Vector();

    public InternalServerEvents() {
    }

    protected void clearData() {
        eventHandlers.clear();
        channelControllers.clear();
        allPlayers.clear();
        allNpcs.clear();
        allVehicles.clear();
        allObjects.clear();
        allZones.clear();
        queue.clear();
        timers.clear();
        onException = null;
        server = null;
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

    public static boolean isValid(GameEntity e) {
        if (e instanceof Player) {
            return allPlayers.stream().anyMatch(p -> p.getId() == e.getId());
        } else if (e instanceof NPC) {
            return allNpcs.stream().anyMatch(n -> n.getId() == e.getId());
        } else if (e instanceof Vehicle) {
            return allVehicles.stream().anyMatch(v -> v.getId() == e.getId());
        } else if (e instanceof GameObject) {
            return allObjects.stream().anyMatch(o -> o.getId() == e.getId());
        } else if (e instanceof Zone) {
            return allZones.stream().anyMatch(o -> o.getId() == e.getId());
        }
        return true;
    }

    private void clearGameEntityData(GameEntity e) {
        e.clearData();
        e.setGameData(null);
        e.setTags(null);
        if (e instanceof NPC) {
            NPC npc = (NPC) e;
            npc.setController(null);
            npc.destroyController();
            npc.getWeapons().clear();
        } else if (e instanceof Player) {
            Player p = (Player) e;
            p.setAuthenticated(false);
            p.getWeapons().clear();
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
                if (!handler.isDisabled()) {
                    handler.onTick();
                }
            } catch (Exception e) {
                catchException(e);
            }
        });
        Events.emit(EventName.onTick);
    }

    public void onLowTPS(int limit, int current) {
        eventHandlers.forEach((name, handler) -> {
            try {
                if (!handler.isDisabled()) {
                    handler.onLowTPS(limit, current);
                }
            } catch (Exception e) {
                catchException(e);
            }
        });
        Events.emit(EventName.onLowTPS, limit, current);
    }

    public void onServerStart() {
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
        clearData();
    }

    public void onPlayerJoin(Player player) {
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
        Float returnValue = null;
        for (Entry<String, BaseEventController> entry : eventHandlers.entrySet()) {
            if (!entry.getValue().isDisabled()) {
                try {
                    Float newValue = entry.getValue().onPlayerReceiveDamage(player, damageEvent);
                    if (newValue != null) {
                        returnValue = newValue;
                    }
                } catch (Exception e) {
                    catchException(e);
                }
            }
        }
        Float newValue = Events.request(EventName.onPlayerReceiveDamage, player, damageEvent);
        if (newValue != null) {
            returnValue = newValue;
        }
        if (returnValue == null) {
            returnValue = damageEvent.getDamageToApply();
        }
        return returnValue;
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

    public void onPlayerWeaponReceived(Player player, int weaponId, boolean isEquipped) {
        Weapon weapon = new Weapon(player, weaponId);

        player.getWeapons().add(weapon);
        eventHandlers.forEach((name, handler) -> {
            try {
                if (!handler.isDisabled()) {
                    handler.onPlayerWeaponReceived(player, weapon, isEquipped);
                }
            } catch (Exception e) {
                catchException(e);
            }
        });
        Events.emit(EventName.onPlayerWeaponReceived, player, weapon, isEquipped);
    }

    public void onPlayerWeaponRemoved(Player player, int weaponId, boolean isEquipped) {
        Weapon weapon = player.getWeapon(weaponId);

        player.getWeapons().removeIf(w -> w.getWeaponId() == weaponId);
        eventHandlers.forEach((name, handler) -> {
            try {
                if (!handler.isDisabled()) {
                    handler.onPlayerWeaponRemoved(player, weapon, isEquipped);
                }
            } catch (Exception e) {
                catchException(e);
            }
        });
        Events.emit(EventName.onPlayerWeaponRemoved, player, weapon, isEquipped);
    }

    public void onPlayerStartedSwimming(Player player) {
        eventHandlers.forEach((name, handler) -> {
            try {
                if (!handler.isDisabled()) {
                    handler.onPlayerStartedSwimming(player);
                }
            } catch (Exception e) {
                catchException(e);
            }
        });
        Events.emit(EventName.onPlayerStartedSwimming, player);
    }

    public void onPlayerStoppedSwimming(Player player) {
        eventHandlers.forEach((name, handler) -> {
            try {
                if (!handler.isDisabled()) {
                    handler.onPlayerStoppedSwimming(player);
                }
            } catch (Exception e) {
                catchException(e);
            }
        });
        Events.emit(EventName.onPlayerStoppedSwimming, player);
    }

    public void onPlayerDivedUnderwater(Player player) {
        eventHandlers.forEach((name, handler) -> {
            try {
                if (!handler.isDisabled()) {
                    handler.onPlayerDivedUnderwater(player);
                }
            } catch (Exception e) {
                catchException(e);
            }
        });
        Events.emit(EventName.onPlayerDivedUnderwater, player);
    }

    public void onPlayerReachedWaterSurface(Player player) {
        eventHandlers.forEach((name, handler) -> {
            try {
                if (!handler.isDisabled()) {
                    handler.onPlayerReachedWaterSurface(player);
                }
            } catch (Exception e) {
                catchException(e);
            }
        });
        Events.emit(EventName.onPlayerReachedWaterSurface, player);
    }

    public void onVehicleCreated(Vehicle vehicle) {
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
        Float returnValue = null;
        for (Entry<String, BaseEventController> entry : eventHandlers.entrySet()) {
            if (!entry.getValue().isDisabled()) {
                try {
                    Float newValue = entry.getValue().onVehicleReceiveDamage(vehicle, damageEvent);
                    if (newValue != null) {
                        returnValue = newValue;
                    }
                } catch (Exception e) {
                    catchException(e);
                }
            }
        }
        Float newValue = Events.request(EventName.onVehicleReceiveDamage, vehicle, damageEvent);
        if (newValue != null) {
            returnValue = newValue;
        }

        if (returnValue == null) {
            returnValue = damageEvent.getDamageToApply();
        }
        return returnValue;
    }

    public void onNPCCreated(NPC npc, int type) {
        npc.NPCType = NPCType.value(type);
        allNpcs.add(npc);

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

        NPCController c = npc.getController();
        if (c != null) {
            c.onCreated();
        }
    }

    public void onNPCDestroyed(NPC npc) {
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

        NPCController c = npc.getController();
        if (c != null) {
            c.onDestroy();
        }
        clearGameEntityData(npc);
        allNpcs.remove(npc);
    }

    public void onNPCSpawned(NPC npc) {
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

        NPCController c = npc.getController();
        if (c != null) {
            c.onSpawned();
        }
    }

    public void onNPCDowned(NPC npc, int entityType, int entityID) {
        GameEntity ent = server.getGameEntity(entityType, entityID);

        eventHandlers.forEach((name, handler) -> {
            try {
                if (!handler.isDisabled()) {
                    handler.onNpcDowned(npc, ent);
                }
            } catch (final Exception e) {
                catchException(e);
            }
        });

        NPCController c = npc.getController();
        if (c != null) {
            c.onDowned(ent);
        }

    }

    public void onPlayerRevived(Player player, int entityType, int entityID) {
        GameEntity revivedBy = server.getGameEntity(entityType, entityID);
        eventHandlers.forEach((name, handler) -> {
            try {
                if (!handler.isDisabled()) {
                    handler.onPlayerRevived(player, revivedBy);
                }
            } catch (final Exception e) {
                catchException(e);
            }
        });
        if (revivedBy instanceof NPC) {
            NPC revivedByNpc = (NPC) revivedBy;
            NPCController revByCtrl = revivedByNpc.getController();
            if (revByCtrl != null) {
                revByCtrl.onFinishedCharacterRevive(player);
            }
        }

    }

    public void onPlayerStartedRevive(Player player, int entityType, int entityID) {
        GameEntity ent = server.getGameEntity(entityType, entityID);
        eventHandlers.forEach((name, handler) -> {
            try {
                if (!handler.isDisabled()) {
                    handler.onPlayerStartedRevive(player, ent == null ? null : ent.asGameCharacter());
                }
            } catch (final Exception e) {
                catchException(e);
            }
        });

    }

    public void onNPCRevived(NPC npc, int entityType, int entityID) {
        GameEntity revivedBy = server.getGameEntity(entityType, entityID);
        eventHandlers.forEach((name, handler) -> {
            try {
                if (!handler.isDisabled()) {
                    handler.onNpcRevived(npc, revivedBy);
                }
            } catch (final Exception e) {
                catchException(e);
            }
        });

        NPCController c = npc.getController();
        if (c != null) {
            c.onRevive(revivedBy);
        }
        if (revivedBy instanceof NPC) {
            NPC revivedByNpc = (NPC) revivedBy;
            NPCController revByCtrl = revivedByNpc.getController();
            if (revByCtrl != null) {
                revByCtrl.onFinishedCharacterRevive(npc);
            }
        }

    }

    public void onNPCStartedRevive(NPC npc, int entityType, int entityID) {
        GameEntity ent = server.getGameEntity(entityType, entityID);

        eventHandlers.forEach((name, handler) -> {
            try {
                if (!handler.isDisabled()) {
                    handler.onNpcStartedRevive(npc, ent == null ? null : ent.asGameCharacter());
                }
            } catch (final Exception e) {
                catchException(e);
            }
        });

        NPCController c = npc.getController();
        if (c != null) {
            c.onStartedCharacterRevive(ent == null ? null : ent.asGameCharacter());
        }

    }

    public void onPlayerDowned(Player player, int entityType, int entityID) {
        GameEntity ent = server.getGameEntity(entityType, entityID);
        eventHandlers.forEach((name, handler) -> {
            try {
                if (!handler.isDisabled()) {
                    handler.onPlayerDowned(player, ent);
                }
            } catch (final Exception e) {
                catchException(e);
            }
        });

    }

    public void onPlayerFailedRevive(Player player, int entityType, int entityID) {
        GameEntity ent = server.getGameEntity(entityType, entityID);

        eventHandlers.forEach((name, handler) -> {
            try {
                if (!handler.isDisabled()) {
                    handler.onPlayerFailedRevive(player, ent == null ? null : ent.asGameCharacter());
                }
            } catch (final Exception e) {
                catchException(e);
            }
        });

    }

    public void onNPCFailedRevive(NPC npc, int entityType, int entityID) {
        GameEntity ent = server.getGameEntity(entityType, entityID);
        eventHandlers.forEach((name, handler) -> {
            try {
                if (!handler.isDisabled()) {
                    handler.onNpcFailedRevive(npc, ent == null ? null : ent.asGameCharacter());
                }
            } catch (final Exception e) {
                catchException(e);
            }
        });
        NPCController c = npc.getController();
        if (c != null) {
            c.onFailedCharacterRevive(ent == null ? null : ent.asGameCharacter());
        }

    }

    public void onNPCEnemyDowned(NPC npc, int entityType, int entityID) {
        GameEntity ent = server.getGameEntity(entityType, entityID);
        eventHandlers.forEach((name, handler) -> {
            try {
                if (!handler.isDisabled()) {
                    handler.onNPCEnemyDowned(npc, ent);
                }
            } catch (final Exception e) {
                catchException(e);
            }
        });
        NPCController c = npc.getController();
        if (c != null) {
            c.onEnemyDowned(ent);
        }

    }

    public void onNPCFollowTargetDowned(NPC npc, int entityType, int entityID) {
        GameEntity ent = server.getGameEntity(entityType, entityID);

        eventHandlers.forEach((name, handler) -> {
            try {
                if (!handler.isDisabled()) {
                    handler.onNPCFollowTargetDowned(npc, ent);
                }
            } catch (final Exception e) {
                catchException(e);
            }
        });
        NPCController c = npc.getController();
        if (c != null) {
            c.onFollowTargetDowned(ent);
        }

    }

    public void onNPCDied(NPC npc, int source, int sourceId, int damagedByEntity, int damagedById, float damageToApply) {
        damageEvent.applyValues(source, sourceId, damagedByEntity, damagedById, damageToApply);

        onNpcDied(npc, damageEvent);
    }

    public void onNpcDied(NPC npc, DamageEvent damageEvent) {
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

        NPCController c = npc.getController();
        if (c != null) {
            c.onDeath(damageEvent);
        }
    }

    public float onNPCReceiveDamage(NPC npc, int source, int sourceId, int damagedByEntity, int damagedById, float damageToApply) {
        damageEvent.applyValues(source, sourceId, damagedByEntity, damagedById, damageToApply);

        return onNpcReceiveDamage(npc, damageEvent);
    }

    public Float onNpcReceiveDamage(NPC npc, DamageEvent damageEvent) {
        Float returnValue = null;
        //order:
        //1 - Handlers
        //2 - Events. signal
        //3 - NPC controller
        // the last non null returned value is the one that's used
        //1 - Handlers
        for (Entry<String, BaseEventController> entry : eventHandlers.entrySet()) {
            if (!entry.getValue().isDisabled()) {
                try {
                    Float newValue = entry.getValue().onNpcReceiveDamage(npc, damageEvent);
                    if (newValue != null) {
                        returnValue = newValue;
                    }
                } catch (Exception e) {
                    catchException(e);
                }
            }
        }
        //2 - Events signal
        Float value = Events.request(EventName.onNpcReceiveDamage, npc, damageEvent);
        if (value != null) {
            returnValue = value;
        }

        //3 - NPC Controller
        NPCController c = npc.getController();
        if (c != null) {
            Float newValue = c.onReceiveDamage(damageEvent);
            if (newValue != null) {
                returnValue = newValue;
            }
        }
        if (returnValue != null) {
            return returnValue;
        }
        return damageEvent.getDamageToApply();
    }

    public void onNPCEnterVehicle(NPC npc, Vehicle vehicle) {
        eventHandlers.forEach((name, handler) -> {
            try {
                if (!handler.isDisabled()) {
                    handler.onNpcEnterVehicle(npc, vehicle);
                }
            } catch (Exception e) {
                catchException(e);
            }
        });
        Events.emit(EventName.onNpcEnterVehicle, npc, vehicle);

        NPCController c = npc.getController();
        if (c != null) {
            c.onEnterVehicle(vehicle);
        }
    }

    public void onNPCLeaveVehicle(NPC npc, Vehicle vehicle) {
        eventHandlers.forEach((name, handler) -> {
            try {
                if (!handler.isDisabled()) {
                    handler.onNpcLeaveVehicle(npc, vehicle);
                }
            } catch (Exception e) {
                catchException(e);
            }
        });
        Events.emit(EventName.onNpcLeaveVehicle, npc, vehicle);

        NPCController c = npc.getController();
        if (c != null) {
            c.onLeaveVehicle(vehicle);
        }
    }

    public void onNPCWeaponReceived(NPC npc, int weaponId, boolean isEquipped) {
        Weapon weapon = new Weapon(npc, weaponId);
        npc.getWeapons().add(weapon);

        eventHandlers.forEach((name, handler) -> {
            try {
                if (!handler.isDisabled()) {
                    handler.onNpcWeaponReceived(npc, weapon, isEquipped);
                }
            } catch (Exception e) {
                catchException(e);
            }
        });
        Events.emit(EventName.onNpcWeaponReceived, npc, weapon, isEquipped);

        NPCController c = npc.getController();
        if (c != null) {
            c.onWeaponReceived(weapon, isEquipped);
        }
    }

    public void onNPCWeaponRemoved(NPC npc, int weaponId, boolean isEquipped) {
        Weapon weapon = npc.getWeapon(weaponId);
        npc.getWeapons().removeIf(w -> w.getWeaponId() == weaponId);

        eventHandlers.forEach((name, handler) -> {
            try {
                if (!handler.isDisabled()) {
                    handler.onNpcWeaponRemoved(npc, weapon, isEquipped);
                }
            } catch (Exception e) {
                catchException(e);
            }
        });
        Events.emit(EventName.onNpcWeaponRemoved, npc, weapon, isEquipped);

        NPCController c = npc.getController();
        if (c != null) {
            c.onWeaponRemoved(weapon, isEquipped);
        }
    }

    public void onNPCRanOutOfAmmo(NPC npc, int weapon) {
        eventHandlers.forEach((name, handler) -> {
            try {
                if (!handler.isDisabled()) {
                    handler.onNpcRanOutOfAmmo(npc, weapon);
                }
            } catch (Exception e) {
                catchException(e);
            }
        });
        Events.emit(EventName.onNpcRanOutOfAmmo, npc, weapon);

        NPCController c = npc.getController();
        if (c != null) {
            c.onRanOutOfAmmo(weapon);
        }
    }

    public void onNPCIsAboutToAttack(NPC npc, int entityType, int entityID) {
        GameEntity entity = server.getGameEntity(entityType, entityID);

        eventHandlers.forEach((name, handler) -> {
            try {
                if (!handler.isDisabled()) {
                    handler.onNpcIsAboutToAttack(npc, entity);
                }
            } catch (Exception e) {
                catchException(e);
            }
        });
        Events.emit(EventName.onNpcIsAboutToAttack, npc, entity);

        NPCController c = npc.getController();
        if (c != null) {
            c.onBeforeAttack(entity);
        }
    }

    public void onNPCActionChanged(NPC npc, int oldAction, int newAction) {
        onNpcActionChanged(npc, NPCAction.value(oldAction), NPCAction.value(newAction));
    }

    public void onNpcActionChanged(NPC npc, NPCAction oldAction, NPCAction newAction) {
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

        NPCController c = npc.getController();
        if (c != null) {
            c.onActionChanged(oldAction, newAction);
        }
    }

    public void onNPCGainedSightOf(NPC npc, int entityType, int entityId) {
        GameEntity entity = server.getGameEntity(entityType, entityId);

        if (entity != null) {
            onNpcGainedSightOf(npc, entity);
        }
    }

    public void onNpcGainedSightOf(NPC npc, GameEntity entity) {
        for (Entry<String, BaseEventController> entry : eventHandlers.entrySet()) {
            if (!entry.getValue().isDisabled()) {
                try {
                    entry.getValue().onNpcGainedSightOf(npc, entity);

                } catch (Exception e) {
                    catchException(e);
                }
            }
        }
        Events.emit(EventName.onNpcGainedSightOf, npc, entity);

        NPCController c = npc.getController();
        if (c != null) {
            c.onGainedSightOf(entity);
        }
    }

    public void onNPCLostSightOf(NPC npc, int entityType, int entityId) {
        GameEntity entity = server.getGameEntity(entityType, entityId);

        if (entity != null) {
            onNpcLostSightOf(npc, entity);
        }
    }

    public void onNpcLostSightOf(NPC npc, GameEntity entity) {
        for (Entry<String, BaseEventController> entry : eventHandlers.entrySet()) {
            if (!entry.getValue().isDisabled()) {
                try {
                    entry.getValue().onNpcLostSightOf(npc, entity);

                } catch (Exception e) {
                    catchException(e);
                }
            }
        }
        Events.emit(EventName.onNpcLostSightOf, npc, entity);

        NPCController c = npc.getController();
        if (c != null) {
            c.onLostSightOf(entity);
        }
    }

    public void onNPCHeardNoise(NPC npc, double x, double y, double z) {
        cachedNPCNoiseVector.set(x, y, z);

        for (Entry<String, BaseEventController> entry : eventHandlers.entrySet()) {
            if (!entry.getValue().isDisabled()) {
                try {
                    entry.getValue().onNpcHeardNoise(npc, cachedNPCNoiseVector);

                } catch (Exception e) {
                    catchException(e);
                }
            }
        }
        Events.emit(EventName.onNpcHeardNoise, npc, cachedNPCNoiseVector);

        NPCController c = npc.getController();
        if (c != null) {
            c.onHeardNoise(cachedNPCNoiseVector);
        }
    }

    public void onNPCReachedLocation(NPC npc, String location) {
        eventHandlers.forEach((name, handler) -> {
            try {
                if (!handler.isDisabled()) {
                    handler.onNpcReachedLocation(npc, location);
                }
            } catch (Exception e) {
                catchException(e);
            }
        });
        Events.emit(EventName.onNpcReachedLocation, npc, location);

        NPCController c = npc.getController();
        if (c != null) {
            c.onLocationReached(location);
        }
    }

    public void onNPCReachedFollowTarget(NPC npc, int entityType, int entityID) {
        GameEntity entity = server.getGameEntity(entityType, entityID);

        eventHandlers.forEach((name, handler) -> {
            try {
                if (!handler.isDisabled()) {
                    handler.onNpcReachedFollowTarget(npc, entity);
                }
            } catch (Exception e) {
                catchException(e);
            }
        });
        Events.emit(EventName.onNpcReachedFollowTarget, npc, entity);

        NPCController c = npc.getController();
        if (c != null) {
            c.onFollowTargetReached(entity);
        }
    }

    public void onNPCStartedSwimming(NPC npc) {
        eventHandlers.forEach((name, handler) -> {
            try {
                if (!handler.isDisabled()) {
                    handler.onNpcStartedSwimming(npc);
                }
            } catch (Exception e) {
                catchException(e);
            }
        });
        Events.emit(EventName.onNpcStartedSwimming, npc);

        NPCController c = npc.getController();
        if (c != null) {
            c.onStartedSwimming();
        }
    }

    public void onNPCStoppedSwimming(NPC npc) {
        eventHandlers.forEach((name, handler) -> {
            try {
                if (!handler.isDisabled()) {
                    handler.onNpcStoppedSwimming(npc);
                }
            } catch (Exception e) {
                catchException(e);
            }
        });
        Events.emit(EventName.onNpcStoppedSwimming, npc);

        NPCController c = npc.getController();
        if (c != null) {
            c.onStoppedSwimming();
        }
    }

    public void onNPCDivedUnderwater(NPC npc) {
        eventHandlers.forEach((name, handler) -> {
            try {
                if (!handler.isDisabled()) {
                    handler.onNpcDivedUnderwater(npc);
                }
            } catch (Exception e) {
                catchException(e);
            }
        });
        Events.emit(EventName.onNpcDivedUnderwater, npc);

        NPCController c = npc.getController();
        if (c != null) {
            c.onDivedUnderwater();
        }
    }

    public void onNPCReachedWaterSurface(NPC npc) {
        eventHandlers.forEach((name, handler) -> {
            try {
                if (!handler.isDisabled()) {
                    handler.onNpcReachedWaterSurface(npc);
                }
            } catch (Exception e) {
                catchException(e);
            }
        });
        Events.emit(EventName.onNpcReachedWaterSurface, npc);

        NPCController c = npc.getController();
        if (c != null) {
            c.onReachedWaterSurface();
        }
    }

    public void onNPCEnemyDied(NPC npc, int entityType, int entityID) {
        GameEntity entity = server.getGameEntity(entityType, entityID);

        eventHandlers.forEach((name, handler) -> {
            try {
                if (!handler.isDisabled()) {
                    handler.onNpcEnemyDied(npc, entity);
                }
            } catch (Exception e) {
                catchException(e);
            }
        });
        Events.emit(EventName.onNpcEnemyDied, npc, entity);

        NPCController c = npc.getController();
        if (c != null) {
            c.onEnemyDied(entity);
        }
    }

    public void onNPCEnemyEnterVehicle(NPC npc, int entityType, int entityID, Vehicle vehicle, boolean asDriver) {
        GameEntity entity = server.getGameEntity(entityType, entityID);

        eventHandlers.forEach((name, handler) -> {
            try {
                if (!handler.isDisabled()) {
                    handler.onNpcEnemyEnterVehicle(npc, entity, vehicle, asDriver);
                }
            } catch (Exception e) {
                catchException(e);
            }
        });
        Events.emit(EventName.onNpcEnemyEnterVehicle, npc, entity, vehicle, asDriver);

        NPCController c = npc.getController();
        if (c != null) {
            c.onEnemyEnterVehicle(entity, vehicle, asDriver);
        }
    }

    public void onNPCEnemyLeaveVehicle(NPC npc, int entityType, int entityID, Vehicle vehicle, boolean asDriver) {
        GameEntity entity = server.getGameEntity(entityType, entityID);

        eventHandlers.forEach((name, handler) -> {
            try {
                if (!handler.isDisabled()) {
                    handler.onNpcEnemyLeaveVehicle(npc, entity, vehicle, asDriver);
                }
            } catch (Exception e) {
                catchException(e);
            }
        });
        Events.emit(EventName.onNpcEnemyLeaveVehicle, npc, entity, vehicle, asDriver);

        NPCController c = npc.getController();
        if (c != null) {
            c.onEnemyLeaveVehicle(entity, vehicle, asDriver);
        }
    }

    public void onNPCEnemyLeftServer(NPC npc, Player player) {
        eventHandlers.forEach((name, handler) -> {
            try {
                if (!handler.isDisabled()) {
                    handler.onNpcEnemyLeftServer(npc, player);
                }
            } catch (Exception e) {
                catchException(e);
            }
        });
        Events.emit(EventName.onNpcEnemyLeftServer, npc, player);

        NPCController c = npc.getController();
        if (c != null) {
            c.onEnemyLeftTheServer(player);
        }
    }

    public void onNPCFollowTargetDied(NPC npc, int entityType, int entityID) {
        GameEntity entity = server.getGameEntity(entityType, entityID);

        eventHandlers.forEach((name, handler) -> {
            try {
                if (!handler.isDisabled()) {
                    handler.onNpcFollowTargetDied(npc, entity);
                }
            } catch (Exception e) {
                catchException(e);
            }
        });
        Events.emit(EventName.onNpcFollowTargetDied, npc, entity);

        NPCController c = npc.getController();
        if (c != null) {
            c.onFollowTargetDied(entity);
        }
    }

    public void onNPCFollowTargetEnterVehicle(NPC npc, int entityType, int entityID, Vehicle vehicle, boolean asDriver) {
        GameEntity entity = server.getGameEntity(entityType, entityID);

        eventHandlers.forEach((name, handler) -> {
            try {
                if (!handler.isDisabled()) {
                    handler.onNpcFollowTargetEnterVehicle(npc, entity, vehicle, asDriver);
                }
            } catch (Exception e) {
                catchException(e);
            }
        });
        Events.emit(EventName.onNpcFollowTargetEnterVehicle, npc, entity, vehicle, asDriver);

        NPCController c = npc.getController();
        if (c != null) {
            c.onFollowTargetEnterVehicle(entity, vehicle, asDriver);
        }
    }

    public void onNPCFollowTargetLeaveVehicle(NPC npc, int entityType, int entityID, Vehicle vehicle, boolean asDriver) {
        GameEntity entity = server.getGameEntity(entityType, entityID);

        eventHandlers.forEach((name, handler) -> {
            try {
                if (!handler.isDisabled()) {
                    handler.onNpcFollowTargetLeaveVehicle(npc, entity, vehicle, asDriver);
                }
            } catch (Exception e) {
                catchException(e);
            }
        });
        Events.emit(EventName.onNpcFollowTargetLeaveVehicle, npc, entity, vehicle, asDriver);

        NPCController c = npc.getController();
        if (c != null) {
            c.onFollowTargetLeaveVehicle(entity, vehicle, asDriver);
        }
    }

    public void onNPCFollowTargetLeftServer(NPC npc, Player player) {
        eventHandlers.forEach((name, handler) -> {
            try {
                if (!handler.isDisabled()) {
                    handler.onNpcFollowTargetLeftServer(npc, player);
                }
            } catch (Exception e) {
                catchException(e);
            }
        });
        Events.emit(EventName.onNpcFollowTargetLeftServer, npc, player);

        NPCController c = npc.getController();
        if (c != null) {
            c.onFollowTargetLeftTheServer(player);
        }
    }

    public void onObjectCreated(GameObject obj) {
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
    }

    public float onObjectReceiveDamage(GameObject obj, int source, int sourceId, int damagedByEntity, int damagedById, float damageToApply) {
        damageEvent.applyValues(source, sourceId, damagedByEntity, damagedById, damageToApply);

        return onObjectReceiveDamage(obj, damageEvent);
    }

    public Float onObjectReceiveDamage(GameObject obj, DamageEvent damageEvent) {
        Float returnValue = null;
        for (Entry<String, BaseEventController> entry : eventHandlers.entrySet()) {
            if (!entry.getValue().isDisabled()) {
                try {
                    Float newValue = entry.getValue().onObjectReceiveDamage(obj, damageEvent);
                    if (newValue != null) {
                        returnValue = newValue;

                    }
                } catch (Exception e) {
                    catchException(e);
                }
            }
        }
        Float newValue = Events.request(EventName.onObjectReceiveDamage, obj, damageEvent);
        if (newValue != null) {
            returnValue = newValue;
        }
        if (returnValue == null) {
            returnValue = damageEvent.getDamageToApply();
        }
        return returnValue;
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

    public void onObjectOverlapped(GameObject obj, int entityType, int entityId) {
        GameEntity entity = server.getGameEntity(entityType, entityId);

        eventHandlers.forEach((name, handler) -> {
            try {
                if (!handler.isDisabled()) {
                    handler.onObjectOverlapped(obj, entity);
                }
            } catch (Exception e) {
                catchException(e);
            }
        });
        Events.emit(EventName.onObjectOverlapped, obj, entity);
    }

    public void onObjectStoppedOverlapping(GameObject obj, int entityType, int entityId) {
        GameEntity entity = server.getGameEntity(entityType, entityId);

        eventHandlers.forEach((name, handler) -> {
            try {
                if (!handler.isDisabled()) {
                    handler.onObjectStoppedOverlapping(obj, entity);
                }
            } catch (Exception e) {
                catchException(e);
            }
        });
        Events.emit(EventName.onObjectStoppedOverlapping, obj, entity);
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

    public void onZoneCreated(Zone zone, int type) {
        zone.zoneType = ZoneType.value(type);
        allZones.add(zone);

        eventHandlers.forEach((name, handler) -> {
            try {
                if (!handler.isDisabled()) {
                    handler.onZoneCreated(zone);
                }
            } catch (Exception e) {
                catchException(e);
            }
        });
        Events.emit(EventName.onZoneCreated, zone);
    }

    public void onZoneDestroyed(Zone zone) {
        eventHandlers.forEach((name, handler) -> {
            try {
                if (!handler.isDisabled()) {
                    handler.onZoneDestroyed(zone);
                }
            } catch (Exception e) {
                catchException(e);
            }
        });
        Events.emit(EventName.onZoneDestroyed, zone);

        clearGameEntityData(zone);
        allZones.remove(zone);
    }

    public void onZoneEnter(Zone zone, int entityType, int entityId) {
        GameEntity entity = server.getGameEntity(entityType, entityId);

        eventHandlers.forEach((name, handler) -> {
            try {
                if (!handler.isDisabled()) {
                    handler.onZoneEnter(zone, entity);
                }
            } catch (Exception e) {
                catchException(e);
            }
        });
        Events.emit(EventName.onZoneEnter, zone, entity);
    }

    public void onZoneLeave(Zone zone, int entityType, int entityId) {
        GameEntity entity = server.getGameEntity(entityType, entityId);

        eventHandlers.forEach((name, handler) -> {
            try {
                if (!handler.isDisabled()) {
                    handler.onZoneLeave(zone, entity);
                }
            } catch (Exception e) {
                catchException(e);
            }
        });
        Events.emit(EventName.onZoneLeave, zone, entity);
    }
}
