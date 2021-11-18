package com.Game.Projectile;

import com.Game.Entity.Entity;
import com.Game.Util.Math.Vector2;
import com.Game.WorldManagement.World;

public class Bullet extends Projectile {

    public Bullet(Entity owner, Vector2 aim, float damage, float speed, long duration) {
        super(owner, aim, damage, speed, duration);
        this.attackStyle = 1;
    }
}
