package com.github.newk5.vf.server.core.entities;

import com.github.newk5.vf.server.core.entities.gameobject.GameObject;
import com.github.newk5.vf.server.core.entities.npc.NPC;
import com.github.newk5.vf.server.core.entities.player.Player;
import com.github.newk5.vf.server.core.entities.vehicle.Vehicle;

public abstract class DamageableEntity extends GameEntity {

    public DamageableEntity() {
    }

    public boolean isDead() {
        if (!threadIsValid()) {
            return false;
        }
        if (this instanceof Player) {
            Player p = (Player) this;
            return p.getHealth() <= 0 || !p.isSpawned();
        } else if (this instanceof NPC) {
            NPC p = (NPC) this;
            return p.isDead();
        } else if (this instanceof Vehicle) {
            Vehicle p = (Vehicle) this;
            return p.getHealth() <= 0;
        } else if (this instanceof GameObject) {
            GameObject p = (GameObject) this;
            return p.getHealth() <= 0;
        }
        return false;
    }

    public int getTeam() {
        if (!threadIsValid()) {
            return -1;
        }
        if (this instanceof Player) {
            Player p = (Player) this;
            return p.getTeam();
        } else if (this instanceof NPC) {
            NPC p = (NPC) this;
            return p.getTeam();
        } else if (this instanceof Vehicle) {
            Vehicle p = (Vehicle) this;
            return -1; //TODO: Not implemented yet
        } else if (this instanceof GameObject) {
            GameObject p = (GameObject) this;
            return -1; //TODO: Not implemented yet
        }
        return -1;
    }

    public float getHealth() {
        if (!threadIsValid()) {
            return -1;
        }
        if (this instanceof Player) {
            Player p = (Player) this;
            return p.getHealth();
        } else if (this instanceof NPC) {
            NPC p = (NPC) this;
            return p.getHealth();
        } else if (this instanceof Vehicle) {
            Vehicle p = (Vehicle) this;
            return p.getHealth();
        } else if (this instanceof GameObject) {
            GameObject p = (GameObject) this;
            return (float) p.getHealth();
        }
        return -1;
    }

}
