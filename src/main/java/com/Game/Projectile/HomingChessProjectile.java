package com.Game.Projectile;

import com.Game.Entity.Entity;

public class HomingChessProjectile extends HomingProjectile {
    public HomingChessProjectile(Entity owner, Entity target, float damage) {
        super(owner, target, damage, 2.5f);
        setImage("chess_pellet.png");
    }
}
