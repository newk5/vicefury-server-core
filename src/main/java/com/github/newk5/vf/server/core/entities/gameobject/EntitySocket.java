package com.github.newk5.vf.server.core.entities.gameobject;

import com.github.newk5.vf.server.core.entities.Transform;

public class EntitySocket {

    private String name;
    private String bone;
    private Transform transform = new Transform();

    public EntitySocket() {
    }

    public EntitySocket(String name, String bone, Transform transform) {
        this.name = name;
        this.bone = bone;
        this.transform = transform;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBone() {
        return bone;
    }

    public void setBone(String bone) {
        this.bone = bone;
    }

    public Transform getTransform() {
        return transform;
    }

    public void setTransform(Transform transform) {
        this.transform = transform;
    }
}
