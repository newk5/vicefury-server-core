package com.github.newk5.vf.server.core.events;

import com.github.newk5.vf.server.core.events.callbacks.*;
import com.github.newk5.vf.server.core.utils.Log;

import java.util.ArrayList;
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

    public static void clear() {
        eventHandlers.clear();
    }

    public static void clear(String eventName) {
        String key = eventName.toLowerCase();

        if(eventHandlers.containsKey(key)) {
            eventHandlers.get(key).clear();
            eventHandlers.remove(key);
        }
        else Log.exception("Unable to clear callbacks for event: %s (Invalid event name)", eventName);
    }

    public static EventEntry getEntry(String eventName) {
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

    public static class EventEntry {

        private final String eventName;
        private boolean enabled = true;
        private final ArrayList<EventCallback> callbacks = new ArrayList<>();

        private EventEntry(String eventName) {
            this.eventName = eventName;
            Log.debug("%s created", eventName);
        }

        public boolean isEmpty() {
            Log.debug("%s is empty", eventName);
            return this.callbacks.isEmpty();
        }

        public boolean isEnabled() {
            return this.enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        private void addCallback(EventCallback callback) {
            Log.debug("Event: %s, Callback : %s", eventName, callback);
            if(!this.callbacks.contains(callback)) {
                if(!this.isEmpty() && this.callbacks.get(0).getClass().getInterfaces()[0] != callback.getClass().getInterfaces()[0]) {
                    Log.exception("Unable to connect callback for event: %s (Cannot have arguments/return-type different than the first callback connected)", this.eventName);
                    return;
                }
                this.callbacks.add(callback);
            }
            else Log.exception("Unable to connect callback for event: %s (Callback already connected)", this.eventName);
        }

        private void removeCallback(EventCallback callback) {
            if(this.callbacks.contains(callback)) this.callbacks.remove(callback);
            else Log.exception("Unable to disconnect callback for event: %s (Callback not connected)", this.eventName);
        }

        private void clear() {
            this.callbacks.clear();
        }

        private void catchException(Exception e, EventCallback callback) {
            Log.exception(e, "Unable to invoke callback: %s for event: %s (Invalid argument type)", callback.getClass().getSimpleName(), this.eventName);
        }

        public <T,U,V,W,X,Y,Z> void emit(Object... args) {
            if(!this.enabled) return;

            if (this.callbacks.get(0).getClass().getInterfaces()[0].getSimpleName().contains("EventSupplier")) {
                Log.exception("Cannot invoke callbacks with return value for event: %s (Use #Events.request to invoke this event)", this.eventName);
                return;
            }

            switch (args.length) {
                case 0: for (EventCallback callback : this.callbacks) {
                    try { ((EventCallback0)callback).accept(); }
                    catch (Exception e) { this.catchException(e, callback); }
                }   break;
                case 1: for (EventCallback callback : this.callbacks) {
                    try { ((EventCallback1<T>)callback).accept((T)args[0]); }
                    catch (Exception e) { this.catchException(e, callback); }
                }   break;
                case 2: for (EventCallback callback : this.callbacks) {
                    try { ((EventCallback2<T,U>)callback).accept((T)args[0], (U)args[1]); }
                    catch (Exception e) { this.catchException(e, callback); }
                }   break;
                case 3: for (EventCallback callback : this.callbacks) {
                    try { ((EventCallback3<T,U,V>)callback).accept((T)args[0], (U)args[1], (V)args[2]); }
                    catch (Exception e) { this.catchException(e, callback); }
                }   break;
                case 4: for (EventCallback callback : this.callbacks) {
                    try { ((EventCallback4<T,U,V,W>)callback).accept((T)args[0], (U)args[1], (V)args[2], (W)args[3]); }
                    catch (Exception e) { this.catchException(e, callback); }
                }   break;
                case 5: for (EventCallback callback : this.callbacks) {
                    try { ((EventCallback5<T,U,V,W,X>)callback).accept((T)args[0], (U)args[1], (V)args[2], (W)args[3], (X)args[4]); }
                    catch (Exception e) { this.catchException(e, callback); }
                }   break;
                case 6: for (EventCallback callback : this.callbacks) {
                    try { ((EventCallback6<T,U,V,W,X,Y>) callback).accept((T)args[0], (U)args[1], (V)args[2], (W)args[3], (X)args[4], (Y)args[5]); }
                    catch (Exception e) { this.catchException(e, callback); }
                }   break;
                case 7: for (EventCallback callback : this.callbacks) {
                    try { ((EventCallback7<T,U,V,W,X,Y,Z>) callback).accept((T)args[0], (U)args[1], (V)args[2], (W)args[3], (X)args[4], (Y)args[5], (Z)args[6]); }
                    catch (Exception e) { this.catchException(e, callback); }
                }   break;
                default: Log.exception("Unable to invoke callbacks for event: %s (Cannot have more than 7 arguments)", this.eventName);
            }
        }

        public <S,T,U,V,W,X,Y,Z> S request(Object... args) {
            if(!this.enabled) return null;

            if (this.callbacks.get(0).getClass().getInterfaces()[0].getSimpleName().contains("EventCallback")) {
                Log.exception("Cannot invoke callbacks with no return value for event: %s (Use #Events.emit to invoke this event)", this.eventName);
                return null;
            }

            S result = null;
            switch (args.length) {
                case 0: for (EventCallback callback : this.callbacks) {
                    try { S value = ((EventSupplier0<S>)callback).get(); if(value != null) result = value; }
                    catch (Exception e) { this.catchException(e, callback); }
                }   break;
                case 1: for (EventCallback callback : this.callbacks) {
                    try { S value = ((EventSupplier1<S,T>)callback).get((T)args[0]); if(value != null) result = value; }
                    catch (Exception e) { this.catchException(e, callback); }
                }   break;
                case 2: for (EventCallback callback : this.callbacks) {
                    try { S value = ((EventSupplier2<S,T,U>)callback).get((T)args[0], (U)args[1]); if(value != null) result = value; }
                    catch (Exception e) { this.catchException(e, callback); }
                }   break;
                case 3: for (EventCallback callback : this.callbacks) {
                    try { S value = ((EventSupplier3<S,T,U,V>)callback).get((T)args[0], (U)args[1], (V)args[2]); if(value != null) result = value; }
                    catch (Exception e) { this.catchException(e, callback); }
                }   break;
                case 4: for (EventCallback callback : this.callbacks) {
                    try { S value = ((EventSupplier4<S,T,U,V,W>)callback).get((T)args[0], (U)args[1], (V)args[2], (W)args[3]); if(value != null) result = value; }
                    catch (Exception e) { this.catchException(e, callback); }
                }   break;
                case 5: for (EventCallback callback : this.callbacks) {
                    try { S value = ((EventSupplier5<S,T,U,V,W,X>)callback).get((T)args[0], (U)args[1], (V)args[2], (W)args[3], (X)args[4]); if(value != null) result = value; }
                    catch (Exception e) { this.catchException(e, callback); }
                }   break;
                case 6: for (EventCallback callback : this.callbacks) {
                    try { S value = ((EventSupplier6<S,T,U,V,W,X,Y>)callback).get((T)args[0], (U)args[1], (V)args[2], (W)args[3], (X)args[4], (Y)args[5]); if(value != null) result = value; }
                    catch (Exception e) { this.catchException(e, callback); }
                }   break;
                case 7: for (EventCallback callback : this.callbacks) {
                    try { S value = ((EventSupplier7<S,T,U,V,W,X,Y,Z>)callback).get((T)args[0], (U)args[1], (V)args[2], (W)args[3], (X)args[4], (Y)args[5], (Z)args[6]); if(value != null) result = value; }
                    catch (Exception e) { this.catchException(e, callback); }
                }   break;
                default: Log.exception("Unable to invoke callbacks for event: %s (Cannot have more than 7 arguments)", this.eventName);
            }
            return result;
        }
    }
}