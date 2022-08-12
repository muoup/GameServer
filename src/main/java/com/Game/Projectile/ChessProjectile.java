package com.Game.Projectile;

import com.Game.Entity.Entity;
import com.Game.Util.Math.Vector2;

public class ChessProjectile extends Projectile {
    public ChessProjectile(Entity owner, Vector2 aim, float speed, float damage) {
        super(owner, aim, damage, speed, 1500);
        setScale(16);
        setImage("chess_pellet.png");
    }
}
