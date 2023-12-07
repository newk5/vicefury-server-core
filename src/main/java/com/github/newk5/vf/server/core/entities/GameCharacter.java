package com.github.newk5.vf.server.core.entities;

import com.github.newk5.vf.server.core.entities.npc.NPC;
import com.github.newk5.vf.server.core.entities.player.Player;

public abstract class GameCharacter extends DamageableEntity {

    public GameCharacter() {
    }

    public boolean isRunning() {
        if (this instanceof Player) {
            Player p = (Player) this;
            return p.isRunning();
        } else if (this instanceof NPC) {
            NPC p = (NPC) this;
            return p.isRunning();
        }
        return false;
    }

    public boolean isKnocked() {
        if (this instanceof Player) {
            Player p = (Player) this;
            return p.isKnocked();
        } else if (this instanceof NPC) {
            NPC p = (NPC) this;
            return p.isOnTheGround();
        }
        return false;
    }

    public boolean isInAir() {
        if (this instanceof Player) {
            Player p = (Player) this;
            return p.isFalling();
        } else if (this instanceof NPC) {
            NPC p = (NPC) this;
            return p.isFalling();
        }
        return false;
    }

    public boolean isInVehicle() {
        if (this instanceof Player) {
            Player p = (Player) this;
            return p.isDriving() || p.isPassenger();
        } else if (this instanceof NPC) {
            NPC p = (NPC) this;
            return p.isInVehicle();
        }
        return false;
    }

    public boolean isMoving() {
        if (this instanceof Player) {
            Player p = (Player) this;
            return p.getMovementSpeed() > 0;
        } else if (this instanceof NPC) {
            NPC p = (NPC) this;
            return p.getMovementSpeed() > 0;
        }
        return false;
    }

    public float getMovementSpeed() {
        if (this instanceof Player) {
            Player p = (Player) this;
            return p.getMovementSpeed();
        } else if (this instanceof NPC) {
            NPC p = (NPC) this;
            return p.getMovementSpeed();
        }
        return 0f;
    }

    @Override
    public int getTeam() {
        if (this instanceof Player) {
            Player p = (Player) this;
            return p.getTeam();
        } else if (this instanceof NPC) {
            NPC p = (NPC) this;
            return p.getTeam();
        }
        return -1;
    }

    public int getColor() {
        if (this instanceof Player) {
            Player p = (Player) this;
            return p.getColor();
        } else if (this instanceof NPC) {
            NPC p = (NPC) this;
            return p.getColor();
        }
        return -1;
    }

    public String getBoneNames() {
        if (this instanceof Player) {
            Player p = (Player) this;
            return p.getBoneNames();
        } else if (this instanceof NPC) {
            NPC p = (NPC) this;
            return p.getBoneNames();
        }
        return "";
    }
    
    
    public String getName() {
        if (this instanceof Player) {
            Player p = (Player) this;
            return p.getName();
        } else if (this instanceof NPC) {
            NPC p = (NPC) this;
            return p.getNameTag();
        }
        return "";
    }
}
