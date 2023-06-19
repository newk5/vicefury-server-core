package com.github.newk5.vf.server.core.entities;

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

    public VectorWithAngle createVectorWithAngle(double angle) {
        return new VectorWithAngle(x, y, z, angle);
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