package com.github.newk5.vf.server.core.controllers;

import com.github.newk5.vf.server.core.InternalServerEvents;
import com.github.newk5.vf.server.core.Server;
import com.github.newk5.vf.server.core.entities.GameCharacter;
import com.github.newk5.vf.server.core.entities.GameEntity;
import com.github.newk5.vf.server.core.entities.npc.NPCAction;
import com.github.newk5.vf.server.core.entities.Vector;
import com.github.newk5.vf.server.core.entities.Weapon;
import com.github.newk5.vf.server.core.entities.npc.NPC;
import com.github.newk5.vf.server.core.entities.player.Player;
import com.github.newk5.vf.server.core.entities.vehicle.Vehicle;
import com.github.newk5.vf.server.core.events.damage.DamageEvent;

public abstract class NPCController {

    public NPC npc;
    public Server server;

    public NPCController(NPC npc) {
        this.npc = npc;
        this.server = InternalServerEvents.server;
    }

    public void onCreated() {

    }

    public void onDowned(GameEntity by) {

    }

    public void onRevive(GameEntity by) {

    }

    public void onStartedCharacterRevive(GameCharacter character) {

    }

    public void onFailedCharacterRevive(GameCharacter character) {

    }

    public void onFinishedCharacterRevive(GameCharacter character) {

    }

    public void onDestroy() {

    }

    public void onSpawned() {

    }

    public void onStartedSwimming() {

    }

    public void onStoppedSwimming() {

    }

    public void onDivedUnderwater() {

    }

    public void onReachedWaterSurface() {

    }

    public void onDeath(DamageEvent damageEvent) {

    }

    public Float onReceiveDamage(DamageEvent damageEvent) {
        return null;
    }

    public void onActionChanged(NPCAction oldAction, NPCAction newAction) {

    }

    public void onGainedSightOf(GameEntity entity) {

    }

    public void onLostSightOf(GameEntity entity) {

    }

    public void onHeardNoise(Vector noiseLocation) {

    }

    public void onWeaponReceived(Weapon wep, boolean equipped) {

    }

    public void onWeaponRemoved(Weapon wep, boolean equipped) {

    }

    public void onEnterVehicle(Vehicle v) {

    }

    public void onLeaveVehicle(Vehicle v) {

    }

    public void onLocationReached(String location) {

    }

    public void onEnemyDied(GameEntity enemy) {

    }

    public void onEnemyDowned( GameEntity downedBy) {

    }

    public void onFollowTargetDowned( GameEntity downedBy) {

    }

    public void onEnemyLeftTheServer(Player p) {

    }

    public void onEnemyEnterVehicle(GameEntity ent, Vehicle vehicle, boolean asDriver) {

    }

    public void onEnemyLeaveVehicle(GameEntity ent, Vehicle vehicle, boolean asDriver) {

    }

    public void onFollowTargetDied(GameEntity enemy) {

    }

    public void onFollowTargetLeftTheServer(Player p) {

    }

    public void onFollowTargetEnterVehicle(GameEntity ent, Vehicle vehicle, boolean asDriver) {

    }

    public void onFollowTargetLeaveVehicle(GameEntity ent, Vehicle vehicle, boolean asDriver) {

    }

    public void onFollowTargetReached(GameEntity target) {

    }

    public void onBeforeAttack(GameEntity target) {

    }

    public void onRanOutOfAmmo(int weapon) {

    }
}
