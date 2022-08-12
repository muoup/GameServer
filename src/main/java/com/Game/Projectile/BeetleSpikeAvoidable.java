package com.Game.Projectile;

import com.Game.Entity.Entity;
import com.Game.Util.Math.Vector2;

public class BeetleSpikeAvoidable extends Projectile {
    public BeetleSpikeAvoidable(Entity owner, Vector2 aim, float damage) {
        super(owner, aim, damage, 400f, 3000);
        this.rotate = false;

        setScale(16);
        setImage("beetle_spike2.png");
    }
}
