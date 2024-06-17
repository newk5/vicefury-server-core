package com.github.newk5.vf.server.core.entities.vehicle;

import com.github.newk5.vf.server.core.entities.Rotation;
import com.github.newk5.vf.server.core.entities.Transform;
import com.github.newk5.vf.server.core.entities.Vector;

public class VehicleSpawnProps {

    private int modelId;
    public Vector position = new Vector();
    public Rotation rotation = new Rotation();

    public VehicleSpawnProps() {
    }

    public VehicleSpawnProps(int modelId, Vector position, Rotation rotation) {
        this.modelId = modelId;
        this.position = position;
        this.rotation = rotation;
    }
    
     public VehicleSpawnProps(int modelId, Transform transform) {
        this.modelId = modelId;
        this.position = transform.position;
        this.rotation = transform.rotation;
    }

    public int getModelId() {
        return modelId;
    }

    public VehicleSpawnProps modelId(int modelId) {
        this.modelId = modelId;
        return this;
    }

    public Vector getPosition() {
        return position;
    }

    public VehicleSpawnProps position(Vector position) {
        this.position = position;
        return this;

    }

    public Rotation getRotation() {
        return rotation;
    }

    public VehicleSpawnProps rotation(Rotation rotation) {
        this.rotation = rotation;
        return this;

    }

}
