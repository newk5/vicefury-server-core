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
        AnsiConsole.out.println(ansi().fg(type.color).a(type.tag +" ").reset().a(message));
    }

    private static String format(Object message, Object...args) {
        String msg = "";
        try { msg = String.format(String.valueOf(message), args); }
        catch (Exception e) {
            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

            print(Type.ERR, String.format("An unexpected error has occurred: %s", e));
            for (int i = 3; i < stackTrace.length; i++) {
                print(Type.ERR, String.format("\t\tat %s", stackTrace[i]));
            }
        }
        return msg;
    }

    public static void exception(Object message, Object... args) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

        error("An unexpected error has occurred: %s", format(message, args));
        for (int i = 2; i < stackTrace.length; i++) {
            error("\t\tat %s", stackTrace[i]);
        }
    }

    public static void exception(Exception e, Object message, Object... args) {
        StackTraceElement[] stackTrace = e.getStackTrace();

        error("An unexpected error has occurred: %s", format(message, args));
        for (StackTraceElement element : stackTrace) {
            error("\t\tat %s", element);
        }
    }

    public static void exception(Exception e) {
        exception(e, e.getMessage());
    }

    public static void info(Object message, Object... args) {
        String msg = format(message, args);

        print(Type.INF, msg);
        Logger.info(Thread.currentThread().getStackTrace()[2] + " " + msg);
    }

    public static void warn(Object message, Object... args) {
        String msg = format(message, args);

        print(Type.WRN, msg);
        Logger.warn(Thread.currentThread().getStackTrace()[2] + " " + msg);
    }

    public static void debug(Object message, Object... args) {
        String msg = format(message, args);

        print(Type.DBG, msg);
        Logger.debug(Thread.currentThread().getStackTrace()[2] + " " + msg);
    }

    public static void success(Object message, Object... args) {
        String msg = format(message, args);

        print(Type.SCS, msg);
        Logger.info(msg);
    }

    public static void error(Object message, Object... args) {
        String msg = format(message, args);

        print(Type.ERR, msg);
        Logger.error(msg);
    }
}