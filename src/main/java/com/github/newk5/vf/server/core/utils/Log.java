package com.github.newk5.vf.server.core.utils;

import org.fusesource.jansi.Ansi.Color;
import org.fusesource.jansi.AnsiConsole;
import org.tinylog.Logger;

import static org.fusesource.jansi.Ansi.ansi;

public class Log {

    enum Type {
        DBG("[DBG]", Color.MAGENTA),
        SCS("[SCS]", Color.GREEN),
        INF("[INF]", Color.CYAN),
        WRN("[WRN]", Color.YELLOW),
        ERR("[ERR]", Color.RED);

        private final String tag;
        private final Color color;

        Type(String tag, Color color) {
            this.tag = tag;
            this.color = color;
        }
    }

    private static void print(Type type, String message) {
        if(!message.isEmpty()) AnsiConsole.out.println(ansi().fg(type.color).a(type.tag + " ").reset().a(message));
    }

    public static void exception(Object message, Object... args) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

        error("An unexpected error has occurred: %s", CoreUtils.format(message, args));
        for (int i = 2; i < stackTrace.length; i++) {
            error("\t\tat %s", stackTrace[i]);
        }
    }

    public static void exception(Exception e, Object message, Object... args) {
        StackTraceElement[] stackTrace = e.getStackTrace();

        error("Cause: %s", e.toString());
        error("An unexpected error has occurred: %s", CoreUtils.format(message, args));
        for (StackTraceElement element : stackTrace) {
            error("\t\tat %s", element);
        }
    }

    public static void exception(Exception e) {
        Logger.error(e);
        StackTraceElement[] stackTrace = e.getStackTrace();

        error("An unexpected error has occurred: %s", e.toString());
        for (StackTraceElement element : stackTrace) {
            error("\t\tat %s", element);
        }
    }

    public static void info(Object message, Object... args) {
        String msg = CoreUtils.format(message, args);

        print(Type.INF, msg);
        Logger.info(Thread.currentThread().getStackTrace()[2] + " " + msg);
    }

    public static void warn(Object message, Object... args) {
        String msg = CoreUtils.format(message, args);

        print(Type.WRN, msg);
        Logger.warn(Thread.currentThread().getStackTrace()[2] + " " + msg);
    }

    public static void debug(Object message, Object... args) {
        String msg = CoreUtils.format(message, args);

        print(Type.DBG, msg);
        Logger.debug(Thread.currentThread().getStackTrace()[2] + " " + msg);
    }

    public static void success(Object message, Object... args) {
        String msg = CoreUtils.format(message, args);

        print(Type.SCS, msg);
        Logger.info(msg);
    }

    public static void error(Object message, Object... args) {
        String msg = CoreUtils.format(message, args);

        print(Type.ERR, msg);
        Logger.error(msg);
    }
}
