package com.Game.Projectile;

import com.Game.Entity.Entity;
import com.Game.Util.Math.Vector2;

public class Pellet extends Projectile {
    public Pellet(Entity owner, Vector2 direction, float damage, float speed, long duration) {
        super(owner, direction, damage, speed, duration);
        this.direction = direction;

        setScale(8);
        setImage("pellet.png");
    }
}
