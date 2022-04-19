package com.Game.Projectile;

import com.Game.Entity.Entity;
import com.Game.Util.Math.Vector2;

public class WebProjectile extends HomingProjectile {
    public WebProjectile(Entity owner, Entity target, float damage, float speed) {
        super(owner, target, damage, speed);

        setImage("web.png");
        setScale(12);
    }
}
