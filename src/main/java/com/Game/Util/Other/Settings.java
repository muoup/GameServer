package com.Game.Util.Other;

import java.util.TimerTask;

public class Settings {

    // In case xp rates are too fast, I'll cap leveling
    // This'll increase over time when more content is released
    public static final int lvlMax = 50;

    // Change these to make xp faster.
    public static final float rangedXPMultiplier = 1.5f;
    public static final float meleeXPMultiplier = 1.65f;

    public static final float shopSellMultiplier = 0.85f;

    public static float xpMultiplier = 1;

    public static final long groundItemDuration = 600000;
    public static final int fpsCap = 60;
    public static final float enemyLatency = 2;
    public static final float mergeDistance = 128;
    public static final long velocityCheckTime = 10;

    public static TimerTask wrap(Runnable r) {
        return new TimerTask() {

            @Override
            public void run() {
                r.run();
            }
        };
    }
}
