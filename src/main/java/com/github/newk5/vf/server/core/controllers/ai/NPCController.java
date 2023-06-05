package com.github.newk5.vf.server.core.controllers.ai;

import com.github.newk5.vf.server.core.InternalServerEvents;
import com.github.newk5.vf.server.core.Server;
import com.github.newk5.vf.server.core.entities.GameEntity;
import com.github.newk5.vf.server.core.entities.NPCAction;
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
    
    public void created() {
        
    }

    public void destroyed() {

    }
    
    public void spawned() {
        
    }

    public void onDeath(DamageEvent ev){

    }

    public Float receivedDamage(DamageEvent ev) {

        return null;
    }

    public void actionChanged(NPCAction oldAction, NPCAction newAction) {

    }

    public void gainedSightOf(GameEntity e) {

    }

    public void lostSightOf(GameEntity e) {

    }

    public void heardNoise(Vector noiseLocation) {

    }
}