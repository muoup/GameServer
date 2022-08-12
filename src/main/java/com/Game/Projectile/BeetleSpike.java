package com.Game.Projectile;

import com.Game.Entity.Entity;
import com.Game.Entity.Player.Player;

public class BeetleSpike extends HomingProjectile {
    public BeetleSpike(Entity owner, Player target, float damage) {
        super(owner, target, damage, 225f);
        setImage("beetle_spike.png");
        this.rotate = true;
    }
}
