package com.github.newk5.vf.server.core.entities;

public class VectorWithAngle {

    public double x;
    public double y;
    public double z;
    public double yawAngle;

    public VectorWithAngle(double x, double y, double z, double yawAngle) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yawAngle = yawAngle;
    }

    public VectorWithAngle() {
    }

    public void set(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void set(double x, double y, double z, double yawAngle) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yawAngle = yawAngle;
    }

    public VectorWithAngle createCopy() {
        return new VectorWithAngle(x, y, z, yawAngle);
    }

    public Double distanceTo(Vector v2) {
        return Math.sqrt((v2.x - x) * (v2.x - x) + (v2.y - y) * (v2.y - y) + (v2.z - z) * (v2.z - z));
    }

    public Double distanceTo(VectorWithAngle v2) {
        return Math.sqrt((v2.x - x) * (v2.x - x) + (v2.y - y) * (v2.y - y) + (v2.z - z) * (v2.z - z));
    }

    public Vector asVector() {
        return new Vector(x, y, z);
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    /**
     * @return the yawAngle
     */
    public double getYawAngle() {
        return yawAngle;
    }

    /**
     * @param yawAngle the yawAngle to set
     */
    public void setYawAngle(double yawAngle) {
        this.yawAngle = yawAngle;
    }

    @Override
    public String toString() {
        return "VectorWithAngle{" + "x=" + x + ", y=" + y + ", z=" + z + ", yawAngle=" + yawAngle + '}';
    }
}
