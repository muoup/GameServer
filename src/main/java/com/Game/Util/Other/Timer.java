package com.Game.Util.Other;

public class Timer {
    public long nextTime;
    public long timer;
    public Runnable onRun;

    public Timer(long timer, boolean initStart, Runnable onRun) {
        this.timer = timer;
        this.onRun = onRun;

        if (initStart)
            nextTime = 0;
        else
            setNext();
    }

    public Timer(long timer, long offset, boolean initStart, Runnable onRun) {
        this(timer, initStart, onRun);
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
