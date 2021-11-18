package com.Game.Projectile;

import com.Game.Entity.Entity;
import com.Game.Entity.Player.Player;

public class BeetleSpike extends HomingProjectile {
    public BeetleSpike(Entity owner, Player target) {
        super(owner, target, 5.5f, 2.5f);
        setImage("beetle_spike.png");
        this.rotate = true;
    }
}
