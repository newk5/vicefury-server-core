package com.github.newk5.vf.server.core.utils;

import java.util.Random;

public class CoreUtils {

    public static String format(Object message, Object... args) {
        String msg = "";
        try {
            msg = String.format(String.valueOf(message), args);
        } catch (Exception e) {
            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

            Log.error("An unexpected error has occurred: %s", e.toString());
            for (int i = 3; i < stackTrace.length; i++) {
                Log.error("\t\tat %s", stackTrace[i]);
            }
        }
        return msg;
    }

    public static int randomColor() {
        Random random = new Random();
        int alpha = 255;
        int red = random.nextInt(256);
        int green = random.nextInt(256);
        int blue = random.nextInt(256);

        int color = (alpha << 24) | (red << 16) | (green << 8) | blue;
        return color;
    }

    public static int randomColor(int alpha) {
        Random random = new Random();
        int red = random.nextInt(256);
        int green = random.nextInt(256);
        int blue = random.nextInt(256);

        int color = (alpha << 24) | (red << 16) | (green << 8) | blue;
        return color;
    }

    public static int createColor(int red, int green, int blue, int alpha) {

        int color = (alpha << 24) | (red << 16) | (green << 8) | blue;
        return color;
    }
}
