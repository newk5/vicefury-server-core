package com.github.newk5.vf.server.core.events.damage;

import com.github.newk5.vf.server.core.entities.GameEntityType;
import com.github.newk5.vf.server.core.entities.npc.NPC;
import com.github.newk5.vf.server.core.entities.player.Player;
import com.github.newk5.vf.server.core.entities.vehicle.Vehicle;
import com.github.newk5.vf.server.core.InternalServerEvents;

public class DamageEvent {

    private DamageSource source = DamageSource.UNDEFINED;
    private int sourceId;
    private GameEntityType DamagedByEntityType;
    private int damagedById;
    private float damageToApply;

    public DamageEvent() {
    }

    public DamageEvent(int source, int sourceId, int DamagedByEntity, int damagedById, float Damage) {
        this.applyValues(source, sourceId, DamagedByEntity, damagedById, Damage);
    }

    public void applyValues(int source, int sourceId, int DamagedByEntity, int damagedById, float Damage) {
        this.source = DamageSource.value(source);
        this.sourceId = sourceId;
        this.DamagedByEntityType = GameEntityType.value(DamagedByEntity);
        this.damagedById = damagedById;
        this.setDamageToApply(Damage);
    }

    public DamageSource getSource() {
        return source;
    }

    public boolean wasDamagedByPlayer() {
        return DamagedByEntityType == GameEntityType.PLAYER;
    }

    public boolean wasDamagedByNPC() {
        return DamagedByEntityType == GameEntityType.NPC;
    }

    public boolean wasDamagedByEmptyVehicle() {
        return DamagedByEntityType == GameEntityType.VEHICLE && source == DamageSource.VEHICLE;
    }

    public boolean wasDamagedByVehicle() {
        return source == DamageSource.VEHICLE;
    }

    public boolean wasDamagedByVehicleWithDriver() {
        return DamagedByEntityType == GameEntityType.PLAYER && source == DamageSource.VEHICLE;
    }

    /**
     * Returns the player that caused the damage. Attention: if a player is hit
     * by someone driving a vehicle, this will return the driver. 
     *
     * @return
     */
    public Player getPlayerAttacker() {
        if (wasDamagedByPlayer()) {
            return InternalServerEvents.server.getPlayer(damagedById);
        }
        return null;
    }

    public NPC getNPCAttacker() {
        if (wasDamagedByNPC()) {
            return InternalServerEvents.server.getNPC(damagedById);
        }
        return null;
    }

    public Vehicle getVehicleSource() {
        if (wasDamagedByVehicle()) {
            return InternalServerEvents.server.getVehicle(sourceId);
        }
        return null;
    }

    public boolean wasDamagedByWeapon() {
        return wasDamagedByPlayer() && source == DamageSource.WEAPON;
    }

    public void setSource(DamageSource source) {
        this.source = source;
    }

    public int getSourceId() {
        return sourceId;
    }

    public void setSourceId(int sourceId) {
        this.sourceId = sourceId;
    }

    public GameEntityType getDamagedByEntity() {
        return DamagedByEntityType;
    }

    public void setDamagedByEntity(GameEntityType DamagedByEntity) {
        this.DamagedByEntityType = DamagedByEntity;
    }

    public int getDamagedById() {
        return damagedById;
    }

    public void setDamagedById(int damagedById) {
        this.damagedById = damagedById;
    }

    /**
     * @return the damageToApply
     */
    public float getDamageToApply() {
        return damageToApply;
    }

    /**
     * @param damageToApply the damageToApply to set
     */
    public void setDamageToApply(float damageToApply) {
        this.damageToApply = damageToApply;
    }

}
