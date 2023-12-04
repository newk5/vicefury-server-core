package com.github.newk5.vf.server.core.entities.zone;

import com.github.newk5.vf.server.core.entities.Transform;
import com.github.newk5.vf.server.core.entities.Vector2D;

public class ZoneSpawnProps {

    private ZoneType type = ZoneType.BLOCKING;
    private Transform transform = new Transform();
    private double sizeX;
    private double sizeY;
    private double height;
    private int color;
    
     public ZoneSpawnProps(ZoneType type, double sphereSize, int color) {
        this.sizeX = sphereSize;
        this.color = color;
        this.type = type;
    }

    public ZoneSpawnProps(ZoneType type, Transform transform, double sizeX, double sizeY, double height, int color) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.height = height;
        this.color = color;
        this.transform = transform;
        this.type = type;
    }
    
      public ZoneSpawnProps(ZoneType type, Transform transform, Vector2D size, double height, int color) {
        this.sizeX = size.x;
        this.sizeY = size.y;
        this.height = height;
        this.color = color;
        this.transform = transform;
        this.type = type;
    }

    public double getSizeX() {
        return sizeX;
    }

    public void setSizeX(double sizeX) {
        this.sizeX = sizeX;
    }

    public double getSizeY() {
        return sizeY;
    }

    public void setSizeY(double sizeY) {
        this.sizeY = sizeY;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public ZoneSpawnProps() {
    }

    public ZoneType getType() {
        return type;
    }

    public void setType(ZoneType type) {
        this.type = type;
    }

    public Transform getTransform() {
        return transform;
    }

    public void setTransform(Transform transform) {
        this.transform = transform;
    }
}
