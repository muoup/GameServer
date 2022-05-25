package com.Game.Projectile;

import com.Game.Entity.Entity;
import com.Game.Util.Math.Vector2;

public class BeetleSpikeAvoidable extends Projectile {
    public BeetleSpikeAvoidable(Entity owner, Vector2 aim) {
        super(owner, aim, 100f, 400f, 3000);
        this.rotate = false;
        setScale(16);
        setImage("beetle_spike2.png");
    }

    public BeetleSpikeAvoidable(Vector2 position, Vector2 aim, Projectile arrow) {
        super(position, aim, arrow);
    }
}
