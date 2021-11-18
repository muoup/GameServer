package com.Game.Projectile;


import com.Game.Entity.Entity;
import com.Game.Util.Math.Vector2;

public class RockPellet extends Projectile {
    public RockPellet(Entity owner, Vector2 aim, float damage, float speed) {
        super(owner, aim, damage, speed, 2500);
        setScale(16);
        setImage("rockPellet.png");
    }
}
