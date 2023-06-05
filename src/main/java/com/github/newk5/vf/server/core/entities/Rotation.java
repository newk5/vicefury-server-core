package com.github.newk5.vf.server.core.entities;

public class Rotation {

    public double yaw;
    public double pitch;
    public double roll;

    public Rotation() {
    }

    public Rotation(double yaw, double pitch, double roll) {
        this.yaw = yaw;
        this.pitch = pitch;
        this.roll = roll;
    }

    public double getYaw() {
        return yaw;
    }

    public void setYaw(double yaw) {
        this.yaw = yaw;
    }

    public double getPitch() {
        return pitch;
    }

    public void setPitch(double pitch) {
        this.pitch = pitch;
    }

    public double getRoll() {
        return roll;
    }

    public void setRoll(double roll) {
        this.roll = roll;
    }

    @Override
    public String toString() {
        return "Rotation{" + "yaw=" + yaw + ", pitch=" + pitch + ", roll=" + roll + '}';
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + (int) (Double.doubleToLongBits(this.yaw) ^ (Double.doubleToLongBits(this.yaw) >>> 32));
        hash = 97 * hash + (int) (Double.doubleToLongBits(this.pitch) ^ (Double.doubleToLongBits(this.pitch) >>> 32));
        hash = 97 * hash + (int) (Double.doubleToLongBits(this.roll) ^ (Double.doubleToLongBits(this.roll) >>> 32));
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
        final Rotation other = (Rotation) obj;
        if (Double.doubleToLongBits(this.yaw) != Double.doubleToLongBits(other.yaw)) {
            return false;
        }
        if (Double.doubleToLongBits(this.pitch) != Double.doubleToLongBits(other.pitch)) {
            return false;
        }
        return Double.doubleToLongBits(this.roll) == Double.doubleToLongBits(other.roll);
    }
}