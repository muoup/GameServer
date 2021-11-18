package com.Game.Projectile;

import com.Game.Entity.Entity;
import com.Game.Util.Math.Vector2;

public class RockArrow extends Projectile {
    public RockArrow(Entity owner, Vector2 aim, float damage, float speed) {
        super(owner, aim, damage, speed, 1250);
        this.rotate = true;
        this.attackStyle = 1;
        setScale(16);
        setImage("rock_arrow.png");
        setCooldown(0.4f);
    }

    public RockArrow(Entity owner, Vector2 aim, Projectile projectile) {
        super(owner, aim, projectile.damage, projectile.speed, 1250);
        this.rotate = true;
        this.attackStyle = 1;
        setScale(16);
        setImage("rock_arrow.png");
        setCooldown(0.4f);
    }
}
