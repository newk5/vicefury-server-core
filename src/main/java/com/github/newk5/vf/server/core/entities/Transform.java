package com.github.newk5.vf.server.core.entities;

import java.util.Objects;

public class Transform {

    public Vector position = new Vector();
    public Rotation rotation = new Rotation();

    public Transform() {
    }

    public Transform(double x, double y, double z, double yaw, double pitch, double roll) {
        this.position = new Vector(x, y, z);
        this.rotation = new Rotation(yaw, pitch, roll);
    }

    public Transform(Vector position, Rotation rotation) {
        this.position = position;
        this.rotation = rotation;
    }

    public Vector getPosition() {
        return position;
    }

    public void setPosition(Vector position) {
        this.position = position;
    }

    public Rotation getRotation() {
        return rotation;
    }

    public void setRotation(Rotation rotation) {
        this.rotation = rotation;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(this.position);
        hash = 17 * hash + Objects.hashCode(this.rotation);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Transform other = (Transform) obj;
        if (!Objects.equals(this.position, other.position)) {
            return false;
        }
        return Objects.equals(this.rotation, other.rotation);
    }

    @Override
    public String toString() {
        return "Transform{" + "location=" + position + ", rotation=" + rotation + '}';
    }
}