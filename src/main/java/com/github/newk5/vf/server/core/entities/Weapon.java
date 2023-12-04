package com.github.newk5.vf.server.core.entities;

import com.github.newk5.vf.server.core.entities.npc.NPC;
import com.github.newk5.vf.server.core.entities.player.Player;

public class Weapon {

    private GameEntity owner; //player or npc
    private int weaponId;

    public Weapon(GameEntity owner, int weaponId) {
        this.owner = owner;
    }

    public float getRange() {
        if (owner instanceof NPC) {
            return owner.asNpc().getWeaponRange(weaponId);
        } else if (owner instanceof Player) {
            return owner.asPlayer().getWeaponRange(weaponId);
        }
        return 0;
    }

    public float getDamage() {
        if (owner instanceof NPC) {
            return owner.asNpc().getWeaponDamage(weaponId);
        } else if (owner instanceof Player) {
            return owner.asPlayer().getWeaponRange(weaponId);
        }

        return 0;
    }

    public int getStoredAmmo() {
        if (owner instanceof NPC) {
            return owner.asNpc().getStoredWeaponAmmo(weaponId);
        } else if (owner instanceof Player) {
            return owner.asPlayer().getStoredWeaponAmmo(weaponId);
        }

        return 0;
    }
    
      public void setStoredAmmo(int ammo) {
        if (owner instanceof NPC) {
            owner.asNpc().setStoredWeaponAmmo(weaponId, ammo);
        } else if (owner instanceof Player) {
            owner.asPlayer().setStoredWeaponAmmo(weaponId, ammo);
        }
    }

    public void setDamage(float dmg) {
        if (owner instanceof NPC) {
            owner.asNpc().setWeaponDamage(weaponId, dmg);
        } else if (owner instanceof Player) {
            owner.asPlayer().setWeaponDamage(weaponId, dmg);
        }
    }

    public void setRange(float dmg) {
        if (owner instanceof NPC) {
            owner.asNpc().setWeaponRange(weaponId, dmg);
        } else if (owner instanceof Player) {
            owner.asPlayer().setWeaponRange(weaponId, dmg);
        }
    }

    public GameEntity getOwner() {
        return owner;
    }

    public int getWeaponId() {
        return weaponId;
    }

}
