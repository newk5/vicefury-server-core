package com.github.newk5.vf.server.core.entities.npc;

import com.github.newk5.vf.server.core.entities.VectorWithAngle;

public class NPCSpawnProps {

    private NPCType type;
    private Integer subtype;
    private Class controllerClass;
    private VectorWithAngle spawnpoint = new VectorWithAngle();

    public NPCSpawnProps(NPCType type, Integer subtype) {
        this.type = type;
        this.subtype = subtype;
    }

    public NPCSpawnProps(NPCType type, Integer subtype, Class controllerClass) {
        this.type = type;
        this.subtype = subtype;
        this.controllerClass = controllerClass;
    }

    public NPCSpawnProps() {
    }

    public NPCType getType() {
        return type;
    }

    public VectorWithAngle getSpawnpoint() {
        return spawnpoint;
    }

    public NPCSpawnProps withSpawnpoint(VectorWithAngle spawnpoint) {
        this.spawnpoint = spawnpoint;
        return this;
    }

    public NPCSpawnProps withType(NPCType type) {
        this.type = type;
        return this;
    }

    public Integer getSubtype() {
        return subtype;
    }

    public NPCSpawnProps withSubtype(Integer subtype) {
        this.subtype = subtype;
        return this;
    }

    public Class getControllerClass() {
        return controllerClass;
    }

    public NPCSpawnProps withController(Class controllerClass) {
        this.controllerClass = controllerClass;
        return this;
    }
}