package com.github.newk5.vf.server.core.utils;

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
}