package com.Game.Util.Other;

import com.Game.Util.Math.Vector2;

import java.awt.*;
import java.util.TimerTask;

public class Settings {

    // In case xp rates are too fast, I'll cap leveling
    // This'll increase over time when more content is released
    public static final int lvlMax = 50;
    public static float dashTimer = .75f;

    public static final Vector2[] resolutions = {
            new Vector2(800, 600),
            new Vector2(1200, 900),
            new Vector2(1960, 1280)
    };

    // Change these to make xp faster.
    public static final float rangedXPMultiplier = 1.5f;
    public static final float meleeXPMultiplier = 1.65f;

    public static final float projLengthMultiplier = 1.775f;

    public static final long groundItemDuration = 600000;

    public static TimerTask wrap(Runnable r) {
        return new TimerTask() {

            @Override
            public void run() {
                r.run();
            }
        };
    }
}
