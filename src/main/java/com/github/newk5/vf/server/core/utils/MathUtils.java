package com.github.newk5.vf.server.core.utils;

import com.github.newk5.vf.server.core.entities.Vector;

public abstract class MathUtils {

    public static Vector inverseDirection(Vector direction) {
        return direction.multiply(-1);
    }

    public static  Vector pushPointInDirection(Vector position, Vector direction, double distance) {

        return position.add(direction.multiply(distance));
    }

    public static double angleBetween(Vector direction1, Vector direction2) {

        double cosTheta = direction1.dotProduct(direction2);
        double theta = Math.acos(cosTheta);

        theta = Math.toDegrees(theta);
        Vector crossProd = direction1.crossProduct(direction2);

        if (crossProd.z < 0) {
            theta *= -1.d;
        }
        return theta;
    }
}
