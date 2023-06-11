package com.github.newk5.vf.server.core.controllers;

import com.github.newk5.vf.server.core.InternalServerEvents;
import com.github.newk5.vf.server.core.Server;
import com.github.newk5.vf.server.core.entities.GameEntity;
import com.github.newk5.vf.server.core.entities.npc.NPCAction;
import com.github.newk5.vf.server.core.entities.Vector;
import com.github.newk5.vf.server.core.entities.npc.NPC;
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

    public void onDestroy() {

    }
    
    public void onSpawned() {
        
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
}