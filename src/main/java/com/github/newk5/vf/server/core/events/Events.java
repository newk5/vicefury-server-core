package com.github.newk5.vf.server.core.events;

import com.github.newk5.vf.server.core.events.callbacks.*;
import com.github.newk5.vf.server.core.utils.Log;

import java.util.HashMap;

public class Events {

    private static final HashMap<String, EventEntry> eventHandlers = new HashMap<>();

    private static EventCallback addCallback(String eventName, EventCallback eventCallback) {
        String key = eventName.toLowerCase();

        if(!eventHandlers.containsKey(key)) eventHandlers.put(key, new EventEntry(eventName));
        eventHandlers.get(key).addCallback(eventCallback);

        return eventCallback;
    }

    public static void disconnect(String eventName, EventCallback eventCallback) {
        String key = eventName.toLowerCase();

        if(eventHandlers.containsKey(key)) {
            EventEntry event = eventHandlers.get(key);

            event.removeCallback(eventCallback);
            if(event.isEmpty()) eventHandlers.remove(key);
        }
        else Log.exception("Unable to remove callback for event: %s (Invalid event name)", eventName);
    }

    public static void emit(String eventName, Object... args) {
        String key = eventName.toLowerCase();

        if(eventHandlers.containsKey(key)) {
            eventHandlers.get(key).emit(args);
        }
    }

    public static <S> S request(String eventName, Object... args) {
        String key = eventName.toLowerCase();

        if(eventHandlers.containsKey(key)) {
            return eventHandlers.get(key).request(args);
        }
        return null;
    }

    public static void clear(String eventName) {
        String key = eventName.toLowerCase();

        if(eventHandlers.containsKey(key)) {
            eventHandlers.get(key).clear();
            eventHandlers.remove(key);
        }
        else Log.exception("Unable to clear callbacks for event: %s (Invalid event name)", eventName);
    }

    public static EventEntry getHandler(String eventName) {
        EventEntry event = eventHandlers.get(eventName.toLowerCase());

        if(event == null) Log.exception("Unable to retrieve handler for event: %s (Invalid event name)", eventName);
        return event;
    }

    public static EventCallback connect(String eventName, EventCallback0 eventCallback) { return addCallback(eventName, eventCallback); }
    public static <T> EventCallback connect(String eventName, EventCallback1<T> eventCallback) { return addCallback(eventName, eventCallback); }
    public static <T,U> EventCallback connect(String eventName, EventCallback2<T,U> eventCallback) { return addCallback(eventName, eventCallback); }
    public static <T,U,V> EventCallback connect(String eventName, EventCallback3<T,U,V> eventCallback) { return addCallback(eventName, eventCallback); }
    public static <T,U,V,W> EventCallback connect(String eventName, EventCallback4<T,U,V,W> eventCallback) { return addCallback(eventName, eventCallback); }
    public static <T,U,V,W,X> EventCallback connect(String eventName, EventCallback5<T,U,V,W,X> eventCallback) { return addCallback(eventName, eventCallback); }
    public static <T,U,V,W,X,Y> EventCallback connect(String eventName, EventCallback6<T,U,V,W,X,Y> eventCallback) { return addCallback(eventName, eventCallback); }
    public static <T,U,V,W,X,Y,Z> EventCallback connect(String eventName, EventCallback7<T,U,V,W,X,Y,Z> eventCallback) { return addCallback(eventName, eventCallback); }

    public static <S> EventCallback connect(String eventName, EventSupplier0<S> eventCallback) { return addCallback(eventName, eventCallback); }
    public static <S,T> EventCallback connect(String eventName, EventSupplier1<S,T> eventCallback) { return addCallback(eventName, eventCallback); }
    public static <S,T,U> EventCallback connect(String eventName, EventSupplier2<S,T,U> eventCallback) { return addCallback(eventName, eventCallback); }
    public static <S,T,U,V> EventCallback connect(String eventName, EventSupplier3<S,T,U,V> eventCallback) { return addCallback(eventName, eventCallback); }
    public static <S,T,U,V,W> EventCallback connect(String eventName, EventSupplier4<S,T,U,V,W> eventCallback) { return addCallback(eventName, eventCallback); }
    public static <S,T,U,V,W,X> EventCallback connect(String eventName, EventSupplier5<S,T,U,V,W,X> eventCallback) { return addCallback(eventName, eventCallback); }
    public static <S,T,U,V,W,X,Y> EventCallback connect(String eventName, EventSupplier6<S,T,U,V,W,X,Y> eventCallback) { return addCallback(eventName, eventCallback); }
    public static <S,T,U,V,W,X,Y,Z> EventCallback connect(String eventName, EventSupplier7<S,T,U,V,W,X,Y,Z> eventCallback) { return addCallback(eventName, eventCallback); }
}