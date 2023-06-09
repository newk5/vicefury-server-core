package com.github.newk5.vf.server.core.events;

import com.github.newk5.vf.server.core.Server;
import com.github.newk5.vf.server.core.entities.GameEntity;
import com.github.newk5.vf.server.core.entities.NPCAction;
import com.github.newk5.vf.server.core.entities.Vector;
import com.github.newk5.vf.server.core.entities.npc.NPC;
import com.github.newk5.vf.server.core.entities.player.Player;
import com.github.newk5.vf.server.core.entities.vehicle.Vehicle;
import com.github.newk5.vf.server.core.events.damage.DamageEvent;

import java.net.URLClassLoader;

public abstract class BaseServerEvents {
    public URLClassLoader classLoader;
    public Server server;

    public BaseServerEvents() {

    }

    public void onServerStart() {

    }

    public void onServerShutdown() {

    }

    public void onTick() {

    }

    public void onPlayerJoin(Player player) {

    }

    public void onPlayerLeave(Player player) {

    }

    public Boolean onPlayerRequestSpawn(Player player) {
        return null;
    }

    public void onPlayerSpawnScreenSkinChange(Player player, int skinId) {

    }

    public void onPlayerSpawn(Player player) {

    }

    public void onPlayerDied(Player player, DamageEvent damageEvent) {

    }

    public Boolean onPlayerMessage(Player player, String message) {
        return null;
    }

    public void onPlayerCommand(Player player, String command) {

    }

    public void onDataReceived(Player player, String channel, String data) {

    }

    public Float onPlayerReceiveDamage(Player player, DamageEvent damageEvent) {
        return null;
    }

    public void onPlayerEnterVehicle(Player player, Vehicle vehicle, boolean asDriver) {

    }

    public void onPlayerLeaveVehicle(Player player, Vehicle vehicle, boolean asDriver) {

    }

    public void onVehicleCreated(Vehicle vehicle) {

    }

    public void onVehicleDestroyed(Vehicle vehicle) {

    }

    public void onVehicleExploded(Vehicle vehicle) {

    }

    public Float onVehicleReceiveDamage(Vehicle vehicle, DamageEvent damageEvent) {
        return null;
    }

    public void onNpcCreated(NPC npc) {

    }

    public void onNpcDestroy(NPC npc) {

    }

    public void onNpcSpawned(NPC npc) {

    }

    public void onNpcDied(NPC npc, DamageEvent damageEvent) {

    }

    public Float onNpcReceiveDamage(NPC npc, DamageEvent damageEvent) {

        return null;
    }

    public void onNpcActionChanged(NPC npc, NPCAction oldAction, NPCAction newAction) {

    }

    public Boolean onNpcGainedSightOf(NPC npc, GameEntity entity) {
        return null;
    }

    public Boolean onNpcLostSightOf(NPC npc, GameEntity entity) {
        return null;
    }

    public Boolean onNpcHeardNoise(NPC npc, Vector noiseLocation) {
        return null;

    }
}