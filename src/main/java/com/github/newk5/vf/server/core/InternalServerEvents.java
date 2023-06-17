package com.github.newk5.vf.server.core;

import com.github.newk5.vf.server.core.controllers.client.ClientChannelController;
import com.github.newk5.vf.server.core.entities.GameEntity;
import com.github.newk5.vf.server.core.entities.GameEntityType;
import com.github.newk5.vf.server.core.entities.NPCAction;
import com.github.newk5.vf.server.core.entities.Vector;
import com.github.newk5.vf.server.core.entities.gameobject.GameObject;
import com.github.newk5.vf.server.core.entities.npc.NPC;
import com.github.newk5.vf.server.core.entities.player.Player;
import com.github.newk5.vf.server.core.entities.vehicle.Vehicle;
import com.github.newk5.vf.server.core.events.BaseServerEvents;
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
    protected static List<GameObject> allObjects = new ArrayList<>();
    protected static List<NPC> allNpcs = new ArrayList<>();
    protected static final Queue<Runnable> queue = new ConcurrentLinkedQueue<>();
    protected static List<GameTimer> timers = new ArrayList<>();
    public static Consumer<Exception> onException;
    private Vector cachedNPCNoiseVector = new Vector();

    public InternalServerEvents() {
    }

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

    public void addEventHandler(String name, BaseServerEvents e) {
        eventHandlers.put(name, e);
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
        if (!timers.isEmpty()) {
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
    }

    public void onServerStart() {
        eventHandlers.forEach((name, handler) -> {
            try {
                handler.onServerStart();
            } catch (final Exception e) {
                catchException(e);
            }
        });
    }

    public void onServerShutdown() {
        eventHandlers.forEach((name, handler) -> {
            try {
                handler.onServerShutdown();
            } catch (final Exception e) {
                catchException(e);
            }
        });

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
            for (final Map.Entry<String, BaseServerEvents> e : eventHandlers.entrySet()) {
                final BaseServerEvents handler = e.getValue();
                handler.onTick();
            }
        } catch (final Exception e) {
            catchException(e);
        }
    }

    public void onPlayerJoin(final Player player) {

        allPlayers.add(player);
        eventHandlers.forEach((name, handler) -> {
            try {
                handler.onPlayerJoin(player);
            } catch (final Exception e) {
                catchException(e);
            }
        });
    }

    public void onPlayerLeave(final Player player) {
        clearGameEntityData(player);
        allPlayers.remove(player);

        eventHandlers.forEach((name, handler) -> {
            try {
                handler.onPlayerLeave(player);
            } catch (final Exception e) {
                catchException(e);
            }
        });
    }

    public boolean onPlayerRequestSpawn(Player player) {

        for (final Map.Entry<String, BaseServerEvents> e : eventHandlers.entrySet()) {
            try {
                Boolean v = e.getValue().onPlayerRequestSpawn(player);
                if (v != null) {
                    return v;
                }
            } catch (final Exception ex) {
                catchException(ex);
            }
        }
        return true;
    }

    public void onPlayerSpawnScreenSkinChange(Player player, int SkinId) {
        eventHandlers.forEach((name, handler) -> {
            try {
                handler.onPlayerSpawnScreenSkinChange(player, SkinId);
            } catch (final Exception e) {
                catchException(e);
            }
        });
    }

    public void onPlayerSpawn(Player player) {
        eventHandlers.forEach((name, handler) -> {
            try {
                handler.onPlayerSpawn(player);
            } catch (final Exception e) {
                catchException(e);
            }
        });
    }

    public void onPlayerDied(Player player, int source, int sourceId, int DamagedByEntity, int damagedById, float damageToApply) {
        damageEvent.applyValues(source, sourceId, DamagedByEntity, damagedById, damageToApply);

        eventHandlers.forEach((name, handler) -> {
            try {
                handler.onPlayerDied(player, damageEvent);
            } catch (final Exception e) {
                catchException(e);
            }
        });
    }

    public boolean onPlayerMessage(final Player player, final String message) {
        for (final Map.Entry<String, BaseServerEvents> entry : eventHandlers.entrySet()) {
            final String name = entry.getKey();
            final BaseServerEvents handler = entry.getValue();
            try {
                Boolean canSendMessage = handler.onPlayerMessage(player, message);
                if (canSendMessage != null) {
                    return canSendMessage;
                }
            } catch (final Exception e) {
                catchException(e);
            }
        }
        return true;
    }

    public void onPlayerCommand(final Player player, final String message) {
        try {
            server.commandRegistry.process(player, message);
        } catch (Exception ignored) {
        }

        eventHandlers.forEach((name, handler) -> {
            try {
                handler.onPlayerCommand(player, message);
            } catch (final Exception e) {
                catchException(e);
            }
        });
    }

    public void onDataReceived(final Player player, String channel, String data) {
        ClientChannelController c = channelControllers.get(channel);
        if (c != null) {
            c.receiveData(player, data);
        } else {
            for (final Map.Entry<String, BaseServerEvents> entry : eventHandlers.entrySet()) {
                try {
                    final BaseServerEvents handler = entry.getValue();
                    handler.onDataReceived(player, channel, data);
                } catch (final Exception e) {
                    catchException(e);
                }
            }
        }
    }

    public float onPlayerReceiveDamage(final Player player, int source, int sourceId, int DamagedByEntity, int damagedById, float damageToApply) {
        damageEvent.applyValues(source, sourceId, DamagedByEntity, damagedById, damageToApply);

        return onPlayerReceiveDamage(player, damageEvent);
    }

    public Float onPlayerReceiveDamage(final Player player, DamageEvent ev) {
        for (Entry<String, BaseServerEvents> handler : eventHandlers.entrySet()) {
            try {
                Float newValue = handler.getValue().onPlayerReceiveDamage(player, ev);
                if (newValue != null) {
                    return newValue;
                }
            } catch (final Exception e) {
                catchException(e);
            }
        }
        return ev.getDamageToApply();
    }

    public void onPlayerEnterVehicle(Player player, Vehicle v, boolean asDriver) {
        for (Entry<String, BaseServerEvents> handler : eventHandlers.entrySet()) {
            try {
                handler.getValue().onPlayerEnterVehicle(player, v, asDriver);
            } catch (final Exception e) {
                catchException(e);
            }
        }
    }

    public void onPlayerLeaveVehicle(Player player, Vehicle v, boolean asDriver) {
        for (Entry<String, BaseServerEvents> handler : eventHandlers.entrySet()) {
            try {
                handler.getValue().onPlayerLeaveVehicle(player, v, asDriver);
            } catch (final Exception e) {
                catchException(e);
            }
        }
    }

    public void onVehicleCreated(Vehicle v) {
        allVehicles.add(v);
        eventHandlers.forEach((name, handler) -> {
            try {
                handler.onVehicleCreated(v);
            } catch (final Exception e) {
                catchException(e);
            }
        });
    }

    public void onVehicleDestroyed(Vehicle v) {
        clearGameEntityData(v);
        allVehicles.remove(v);
        eventHandlers.forEach((name, handler) -> {
            try {
                handler.onVehicleDestroyed(v);
            } catch (final Exception e) {
                catchException(e);
            }
        });
    }

    public void onVehicleExploded(Vehicle vehicle) {
        eventHandlers.forEach((name, handler) -> {
            try {
                handler.onVehicleExploded(vehicle);
            } catch (final Exception e) {
                catchException(e);
            }
        });
    }

    public float onVehicleReceiveDamage(final Vehicle v, int source, int sourceId, int DamagedByEntity, int damagedById, float damageToApply) {
        damageEvent.applyValues(source, sourceId, DamagedByEntity, damagedById, damageToApply);

        return onVehicleReceiveDamage(v, damageEvent);
    }

    public Float onVehicleReceiveDamage(final Vehicle v, DamageEvent ev) {
        for (Entry<String, BaseServerEvents> handler : eventHandlers.entrySet()) {
            try {
                Float newValue = handler.getValue().onVehicleReceiveDamage(v, ev);
                if (newValue != null) {
                    return newValue;
                }
            } catch (final Exception e) {
                catchException(e);
            }
        }
        return ev.getDamageToApply();
    }

    public void onNPCCreated(final NPC npc) {
        allNpcs.add(npc);
        try {
            for (final Map.Entry<String, BaseServerEvents> entry : eventHandlers.entrySet()) {
                final String name = entry.getKey();
                final BaseServerEvents handler = entry.getValue();
                if (npc.getController() != null) {
                    npc.getController().created();
                } else {
                    handler.onNpcCreated(npc);
                }
            }
        } catch (final Exception e) {
            catchException(e);
        }
    }

    public void onNPCDestroyed(final NPC npc) {
        clearGameEntityData(npc);
        allNpcs.remove(npc);
        try {
            for (final Map.Entry<String, BaseServerEvents> entry : eventHandlers.entrySet()) {
                final String name = entry.getKey();
                final BaseServerEvents handler = entry.getValue();
                if (npc.getController() != null) {
                    npc.getController().destroyed();
                } else {
                    handler.onNpcDestroy(npc);
                }
            }
        } catch (final Exception e) {
            catchException(e);
        }
    }

    public void onNPCSpawned(final NPC npc) {
        try {
            for (final Map.Entry<String, BaseServerEvents> entry : eventHandlers.entrySet()) {
                final String name = entry.getKey();
                final BaseServerEvents handler = entry.getValue();
                if (npc.getController() != null) {
                    npc.getController().spawned();
                } else {
                    handler.onNpcSpawned(npc);
                }
            }
        } catch (final Exception e) {
            catchException(e);
        }
    }

    public void onNPCDied(final NPC npc, int source, int sourceId, int DamagedByEntity, int damagedById, float damageToApply) {
        damageEvent.applyValues(source, sourceId, DamagedByEntity, damagedById, damageToApply);

        onNpcDied(npc, damageEvent);
    }

    public void onNpcDied(final NPC npc, DamageEvent ev) {
        for (Entry<String, BaseServerEvents> handler : eventHandlers.entrySet()) {
            try {
                if (npc.getController() != null) {
                    npc.getController().onDeath(ev);
                }
                handler.getValue().onNpcDied(npc, ev);
            } catch (final Exception e) {
                catchException(e);
            }
        }
    }

    public float onNPCReceiveDamage(final NPC npc, int source, int sourceId, int DamagedByEntity, int damagedById, float damageToApply) {
        damageEvent.applyValues(source, sourceId, DamagedByEntity, damagedById, damageToApply);

        return onNpcReceiveDamage(npc, damageEvent);
    }

    public Float onNpcReceiveDamage(final NPC npc, DamageEvent ev) {
        for (Entry<String, BaseServerEvents> handler : eventHandlers.entrySet()) {
            try {
                Float newValue = npc.getController() != null ? npc.getController().receivedDamage(ev) : handler.getValue().onNpcReceiveDamage(npc, ev);
                if (newValue != null) {
                    return newValue;
                }
            } catch (final Exception e) {
                catchException(e);
            }
        }
        return ev.getDamageToApply();
    }

    public void onNPCActionChanged(final NPC npc, int oldAct, int newAct) {
        for (final Map.Entry<String, BaseServerEvents> entry : eventHandlers.entrySet()) {
            final String name = entry.getKey();
            final BaseServerEvents handler = entry.getValue();
            try {
                if (npc.getController() != null) {
                    npc.getController().actionChanged(NPCAction.value(oldAct), NPCAction.value(newAct));

                } else {
                    handler.onNpcActionChanged(npc, NPCAction.value(oldAct), NPCAction.value(newAct));
                }
            } catch (final Exception e) {
                catchException(e);
            }
        }
    }

    public boolean onNPCGainedSightOf(final NPC npc, int entityType, int entityId) {
        GameEntity ent = server.getGameEntity(GameEntityType.value(entityType), entityId);

        if (ent != null) {
            return onNPCGainedSightOf(npc, ent);
        }
        return true;
    }

    public Boolean onNPCGainedSightOf(final NPC npc, GameEntity ent) {
        for (final Map.Entry<String, BaseServerEvents> entry : eventHandlers.entrySet()) {
            final String name = entry.getKey();
            final BaseServerEvents handler = entry.getValue();
            try {
                if (npc.getController() != null) {
                    npc.getController().gainedSightOf(ent);
                    return false;
                }
                Boolean ack = handler.onNpcGainedSightOf(npc, ent);
                if (ack != null) {
                    return ack;
                }
            } catch (final Exception e) {
                catchException(e);
            }
        }
        return true;
    }

    public boolean onNPCLostSightOf(final NPC npc, int entityType, int entityId) {
        GameEntity ent = server.getGameEntity(GameEntityType.value(entityType), entityId);
        if (ent != null) {
            return onNPCLostSightOf(npc, ent);
        }
        return true;
    }

    public Boolean onNPCLostSightOf(final NPC npc, GameEntity ent) {
        for (final Map.Entry<String, BaseServerEvents> entry : eventHandlers.entrySet()) {
            final String name = entry.getKey();
            final BaseServerEvents handler = entry.getValue();
            try {
                if (npc.getController() != null) {
                    npc.getController().lostSightOf(ent);
                    return false;
                }
                Boolean ack = handler.onNpcLostSightOf(npc, ent);
                if (ack != null) {
                    return ack;
                }
            } catch (final Exception e) {
                catchException(e);
            }
        }
        return true;
    }

    public boolean onNPCHeardNoise(final NPC npc, double x, double y, double z) {
        cachedNPCNoiseVector.setX(x);
        cachedNPCNoiseVector.setY(y);
        cachedNPCNoiseVector.setZ(z);

        for (final Map.Entry<String, BaseServerEvents> entry : eventHandlers.entrySet()) {
            final String name = entry.getKey();
            final BaseServerEvents handler = entry.getValue();
            try {
                if (npc.getController() != null) {
                    npc.getController().heardNoise(cachedNPCNoiseVector);
                    return false;
                }
                Boolean ack = handler.onNpcHeardNoise(npc, cachedNPCNoiseVector);
                if (ack != null) {
                    return ack;
                }
            } catch (final Exception e) {
                catchException(e);
            }
        }
        return true;
    }

    public void onObjectCreated(GameObject o) {
        allObjects.add(o);
        eventHandlers.forEach((name, handler) -> {
            try {
                handler.onObjectCreated(o);
            } catch (Exception e) {
                catchException(e);
            }
        });
    }

    public void onObjectDestroyed(GameObject obj) {

        eventHandlers.forEach((name, handler) -> {
            try {
                handler.onObjectDestroyed(obj);
            } catch (Exception e) {
                catchException(e);
            }
        });
        clearGameEntityData(obj);
        allObjects.remove(obj);
    }

    public float onObjectReceiveDamage(GameObject obj, int source, int sourceId, int DamagedByEntity, int damagedById, float damageToApply) {
        damageEvent.applyValues(source, sourceId, DamagedByEntity, damagedById, damageToApply);

        eventHandlers.forEach((name, handler) -> {
            try {
                handler.onObjectReceiveDamage(obj, damageEvent);
            } catch (final Exception e) {
                catchException(e);
            }
        });
        return damageToApply;
    }

    public void onObjectTouched(GameObject obj, int entityType, int gameEntityId) {
        eventHandlers.forEach((name, handler) -> {
            try {
                GameEntityType t = GameEntityType.value(entityType);

                GameEntity ent = null;
                if (null != t) {
                    switch (t) {
                        case NPC:
                            ent = InternalServerEvents.server.getNPC(gameEntityId);
                            break;
                        case OBJECT:
                            ent = InternalServerEvents.server.getObject(gameEntityId);
                            break;
                        case VEHICLE:
                            ent = InternalServerEvents.server.getVehicle(gameEntityId);
                            break;
                        case PLAYER:
                            ent = InternalServerEvents.server.getPlayer(gameEntityId);
                            break;
                        default:
                            break;
                    }
                }
                handler.onObjectTouched(obj, ent);
            } catch (Exception e) {
                catchException(e);
            }
        });

    }

    public void onObjectBroken(GameObject obj) {
        eventHandlers.forEach((name, handler) -> {
            try {
                handler.onObjectBroken(obj);
            } catch (Exception e) {
                catchException(e);
            }
        });
    }
}
