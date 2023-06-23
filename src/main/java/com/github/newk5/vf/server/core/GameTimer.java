package com.github.newk5.vf.server.core;

import com.github.newk5.vf.server.core.utils.Log;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class GameTimer {

    private String name;
    private boolean recurring;
    private long interval;
    private Consumer<GameTimer> r;
    private Predicate<GameTimer> stopCondition;
    private boolean pendingCancellation;
    private boolean hasRunOnce;
    private boolean hasThrownException;
    private long lastTick;

    public GameTimer(String name, boolean recurring, long ms, Consumer<GameTimer> r) {
        this.name = name;
        this.recurring = recurring;
        this.interval = ms;
        this.r = r;
    }

    public GameTimer(String name, boolean recurring, long ms, Consumer<GameTimer> r, Predicate<GameTimer> stopCondition) {
        this.name = name;
        this.recurring = recurring;
        this.interval = ms;
        this.r = r;
        this.stopCondition = stopCondition;
    }

    protected boolean stopConditionEvalsToTrue() {
        if (stopCondition != null) {
            return stopCondition.test(this);
        }
        return false;
    }

    protected void process() {
        try {
            setLastTick(System.currentTimeMillis());
            hasRunOnce = true;
            r.accept(this);

        } catch (Exception e) {
            hasThrownException = true;
            Log.exception(e);
        }
    }

    protected boolean shouldRun() {
        if (getLastTick() == 0) {
            setLastTick(System.currentTimeMillis());
        }
        long current = System.currentTimeMillis();
        long v = (current - getLastTick());
        return v >= interval;
    }

    //a timer is considered expired if it's not recurring and has already ran once 
    public boolean hasExpired() {
        return (!isRecurring() && hasRunOnce);
    }

    /**
     * @return the hasRunOnce
     */
    public boolean hasRunOnce() {
        return hasRunOnce;
    }

    public void cancel() {
        pendingCancellation = true;
    }

    public boolean isPendingCancellation() {
        return pendingCancellation;
    }

    protected Predicate<GameTimer> getStopCondition() {
        return stopCondition;
    }

    public boolean hasThrownException() {
        return hasThrownException;
    }

    public String getName() {
        return name;
    }

    public boolean isRecurring() {
        return recurring;
    }

    public long getInterval() {
        return interval;
    }

    public long getLastTick() {
        return lastTick;
    }


    public void setLastTick(long lastTick) {
        this.lastTick = lastTick;
    }
}
