package com.github.newk5.vf.server.core.entities;

import com.github.newk5.vf.server.core.utils.MathUtils;

public class Vector {

    public double x;
    public double y;
    public double z;

    public Vector(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector() {
    }

    public Vector createCopy() {
        return new Vector(x, y, z);
    }

    public VectorWithAngle createVectorWithAngle(double angle) {
        return new VectorWithAngle(x, y, z, angle);
    }

    public Double distanceTo(Vector v2) {
        return Math.sqrt((v2.x - x) * (v2.x - x) + (v2.y - y) * (v2.y - y) + (v2.z - z) * (v2.z - z));
    }

    public Vector multiply(double v) {
        return new Vector(x * v, y * v, z * v);
    }

    public Vector multiply(Vector v) {
        return new Vector(x * v.x, y * v.y, z * v.z);
    }

    public Vector add(double v) {
        return new Vector(x + v, y + v, z + v);
    }

    public Vector add(Vector v) {
        return new Vector(x + v.x, y + v.y, z + v.z);
    }

    public double dotProduct(Vector other) {
        return (this.x * other.x) + (this.y * other.y) + (this.z * other.z);
    }

    public Vector moveInDirection(Vector direction, double distance) {
        return MathUtils.movePointInDirection(this, direction, distance);
    }

    public Vector crossProduct(Vector other) {
        double resultX = (this.y * other.z) - (this.z * other.y);
        double resultY = (this.z * other.x) - (this.x * other.z);
        double resultZ = (this.x * other.y) - (this.y * other.x);

        return new Vector(resultX, resultY, resultZ);
    }

    public Vector subtract(double v) {
        return new Vector(x - v, y - v, z - v);
    }

    public Vector subtract(Vector v) {
        return new Vector(x + v.x, y - v.y, z - v.z);
    }

    public void set(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
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

    @Override
    public String toString() {
        return "Vector{" + "x=" + x + ", y=" + y + ", z=" + z + '}';
    }
}
