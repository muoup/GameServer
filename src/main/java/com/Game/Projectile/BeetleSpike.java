package com.Game.Projectile;

import com.Game.Entity.Entity;
import com.Game.Entity.Player.Player;

public class BeetleSpike extends HomingProjectile {
    public BeetleSpike(Entity owner, Player target) {
        super(owner, target, 45f, 225f);
        setImage("beetle_spike.png");
        this.rotate = true;
    }
}
