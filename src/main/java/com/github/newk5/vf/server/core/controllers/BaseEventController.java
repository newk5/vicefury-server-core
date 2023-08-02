package com.github.newk5.vf.server.core.controllers;

import com.github.newk5.vf.server.core.Server;
import com.github.newk5.vf.server.core.entities.GameEntity;
import com.github.newk5.vf.server.core.entities.Vector;
import com.github.newk5.vf.server.core.entities.gameobject.GameObject;
import com.github.newk5.vf.server.core.entities.npc.NPC;
import com.github.newk5.vf.server.core.entities.npc.NPCAction;
import com.github.newk5.vf.server.core.entities.player.Player;
import com.github.newk5.vf.server.core.entities.vehicle.Vehicle;
import com.github.newk5.vf.server.core.entities.zone.Zone;
import com.github.newk5.vf.server.core.events.damage.DamageEvent;

import java.net.URLClassLoader;

public abstract class BaseEventController {

    public URLClassLoader classLoader;
    public Server server;
    private int position;
    private String controllerName;
    protected boolean disabled;

    public BaseEventController() {
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getControllerName() {
        return controllerName;
    }

    public void setControllerName(String controllerName) {
        this.controllerName = controllerName;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public void onTick() {

    }

    public void onLowTPS(int limit, int current) {

    }

    public void onServerStart() {

    }

    public void onServerShutdown() {

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

    public void onVehicleCreated(Vehicle vehicle) {

    }

    public void onVehicleDestroyed(Vehicle vehicle) {

    }

    public void onVehicleExploded(Vehicle vehicle) {

    }

    public Float onVehicleReceiveDamage(Vehicle vehicle, DamageEvent damageEvent) {
        return null;
    }

    public void onObjectCreated(GameObject obj) {

    }

    public void onObjectDestroyed(GameObject obj) {

    }

    public Float onObjectReceiveDamage(GameObject obj, DamageEvent ev) {
        return null;
    }

    public void onObjectTouched(GameObject obj, GameEntity gameEntity) {

    }

    public void onObjectBroken(GameObject obj) {

    }

    public void onZoneCreated(Zone zone) {

    }

    public void onZoneDestroyed(Zone zone) {

    }

    public void onZoneEnter(Zone zone, GameEntity entity) {

    }

    public void onZoneLeave(Zone zone, GameEntity entity) {

    }
}
