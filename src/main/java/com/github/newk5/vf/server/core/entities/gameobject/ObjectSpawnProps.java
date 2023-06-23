package com.github.newk5.vf.server.core.entities.gameobject;

import com.github.newk5.vf.server.core.entities.Transform;

public class ObjectSpawnProps {

    private boolean withCollision;
    private boolean isDamageable;
    private Transform transform = new Transform();
    private int modelId;

    public ObjectSpawnProps() {
    }

    public ObjectSpawnProps(int modelId) {
        this.modelId = modelId;
    }

    public ObjectSpawnProps(Transform transform) {
        this.transform = transform;
    }

    public ObjectSpawnProps(int modelId, Transform transform, boolean withCollision, boolean isDamageable) {
        this.modelId = modelId;
        this.withCollision = withCollision;
        this.isDamageable = isDamageable;
        this.transform = transform;
    }

    public ObjectSpawnProps(Transform transform, boolean withCollision, boolean isDamageable) {
        this.withCollision = withCollision;
        this.isDamageable = isDamageable;
        this.transform = transform;
    }

    public boolean isCollisionEnabled() {
        return withCollision;
    }

    public ObjectSpawnProps collisionEnabled(boolean status) {
        withCollision = status;
        return this;
    }

    public ObjectSpawnProps canBeDamaged(boolean status) {
        isDamageable = status;
        return this;
    }

    public boolean isDamageable() {
        return isDamageable;
    }

    public void setIsDamageable(boolean isDamageable) {
        this.isDamageable = isDamageable;
    }

    public Transform getTransform() {
        return transform;
    }

    public void setTransform(Transform transform) {
        this.transform = transform;
    }

    /**
     * @return the modelId
     */
    public int getModelId() {
        return modelId;
    }

    /**
     * @param modelId the modelId to set
     */
    public void setModelId(int modelId) {
        this.modelId = modelId;
    }

}
