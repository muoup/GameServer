package com.Game.Util.Other;

public class Timer {
    public long nextTime;
    public long timer;
    public Runnable onRun;

    public Timer(long timer, Runnable onRun) {
        this.timer = timer;
        this.onRun = onRun;

        setNext();
    }

    public Timer(long timer, Runnable onRun, long offset) {
        this(timer, onRun);
        nextTime += offset;
    }

    public void update() {
        if (System.currentTimeMillis() > nextTime) {
            setNext();

            onRun.run();
        }
    }

    public void setNext() {
        nextTime = System.currentTimeMillis() + timer;
    }
}
