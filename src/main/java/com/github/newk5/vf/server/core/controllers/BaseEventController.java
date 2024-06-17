package com.github.newk5.vf.server.core.controllers;

import com.github.newk5.vf.server.core.PluginLoader;
import com.github.newk5.vf.server.core.Server;
import com.github.newk5.vf.server.core.entities.GameCharacter;
import com.github.newk5.vf.server.core.entities.GameEntity;
import com.github.newk5.vf.server.core.entities.Vector;
import com.github.newk5.vf.server.core.entities.Weapon;
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
    public PluginLoader pluginLoader;

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

    public void onPlayerWeaponReceived(Player player, Weapon weapon, boolean isEquipped) {

    }

    public void onPlayerWeaponRemoved(Player player, Weapon weapon, boolean isEquipped) {

    }

    public void onPlayerStartedSwimming(Player player) {

    }

    public void onPlayerStoppedSwimming(Player player) {

    }

    public void onPlayerDivedUnderwater(Player player) {

    }

    public void onPlayerReachedWaterSurface(Player player) {

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

    public void onNpcEnterVehicle(NPC npc, Vehicle vehicle) {

    }

    public void onNpcLeaveVehicle(NPC npc, Vehicle vehicle) {

    }

    public void onNpcWeaponReceived(NPC npc, Weapon wep, boolean equipped) {

    }

    public void onNpcWeaponRemoved(NPC npc, Weapon wep, boolean equipped) {

    }

    public void onNpcRanOutOfAmmo(NPC npc, int weapon) {

    }

    public void onNpcIsAboutToAttack(NPC npc, GameEntity entity) {

    }

    public void onNpcActionChanged(NPC npc, NPCAction oldAction, NPCAction newAction) {

    }

    public void onNpcGainedSightOf(NPC npc, GameEntity entity) {

    }

    public void onNpcLostSightOf(NPC npc, GameEntity entity) {

    }

    public void onNpcHeardNoise(NPC npc, Vector noiseLocation) {

    }

    public void onNpcReachedLocation(NPC npc, String location) {

    }

    public void onNpcReachedFollowTarget(NPC npc, GameEntity entity) {

    }

    public void onNpcStartedSwimming(NPC npc) {

    }

    public void onNpcStoppedSwimming(NPC npc) {

    }

    public void onNpcDivedUnderwater(NPC npc) {

    }

    public void onNpcReachedWaterSurface(NPC npc) {

    }

    public void onNpcEnemyDied(NPC npc, GameEntity entity) {

    }

    public void onNpcEnemyEnterVehicle(NPC npc, GameEntity entity, Vehicle vehicle, boolean asDriver) {

    }

    public void onNpcEnemyLeaveVehicle(NPC npc, GameEntity entity, Vehicle vehicle, boolean asDriver) {

    }

    public void onNpcEnemyLeftServer(NPC npc, Player player) {

    }

    public void onNpcFollowTargetDied(NPC npc, GameEntity entity) {

    }

    public void onNpcFollowTargetEnterVehicle(NPC npc, GameEntity entity, Vehicle vehicle, boolean asDriver) {

    }

    public void onNpcFollowTargetLeaveVehicle(NPC npc, GameEntity entity, Vehicle vehicle, boolean asDriver) {

    }

    public void onNpcFollowTargetLeftServer(NPC npc, Player player) {

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

    public Float onObjectReceiveDamage(GameObject obj, DamageEvent damageEvent) {
        return null;
    }

    public void onObjectTouched(GameObject obj, GameEntity entity) {

    }

    public void onObjectOverlapped(GameObject obj, GameEntity entity) {

    }

    public void onObjectStoppedOverlapping(GameObject obj, GameEntity entity) {

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

    public void onNpcDowned(NPC npc, GameEntity by) {

    }

    public void onPlayerDowned(Player player, GameEntity by) {

    }

    public void onPlayerRevived(Player player, GameEntity by) {

    }

    public void onNpcRevived(NPC npc, GameEntity by) {

    }

    public void onPlayerStartedRevive(Player player, GameCharacter character) {

    }

    public void onNpcStartedRevive(NPC npc, GameCharacter character) {

    }

    public void onPlayerFailedRevive(Player player, GameCharacter charBeingRevived) {

    }

    public void onNpcFailedRevive(NPC npc, GameCharacter charBeingRevived) {

    }

    public void onNPCEnemyDowned(NPC npc,  GameEntity by) {

    }

    public void onNPCFollowTargetDowned(NPC npc,  GameEntity by) {

    }

}
