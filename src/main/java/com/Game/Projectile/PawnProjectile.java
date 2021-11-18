package com.Game.Projectile;

import com.Game.Entity.Entity;
import com.Game.Util.Math.Vector2;

public class PawnProjectile extends Projectile {
    public PawnProjectile(Entity owner, Vector2 aim, float damage) {
        super(owner, aim, damage, 3.5f, 1500);
        this.speed = 1.25f;
        setScale(48);
        setImage("chess_pellet.png");
    }
}
