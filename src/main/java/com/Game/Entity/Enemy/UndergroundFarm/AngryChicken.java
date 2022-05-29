package com.Game.Entity.Enemy.UndergroundFarm;

import com.Game.Entity.Enemy.Generic.AIType;
import com.Game.Entity.Enemy.Generic.Enemy;
import com.Game.Projectile.Pellet;
import com.Game.Util.Math.Vector2;
import com.Game.WorldManagement.World;

public class AngryChicken extends Enemy {
    private Clucky owner;

    public AngryChicken(World world, Clucky owner, int x, int y) {
        super(world, x, y);
        this.name = "Angry Chicken";
        this.idleAI = AIType.none;
        this.targetAI = AIType::basicChase;
        this.speed = 60f;
        this.followDistance = 128f;
        this.loseFocusDistance = 2500;
        this.temporary = true;
        this.owner = owner;

        setImage("angry_chicken.png", 64, 64);
        setMaxHealth(30);
        addProjTimer(1000, this::shoot, (long) (Math.random() * 1000));
    }

    public void shoot() {
        if (playerTarget == null)
            return;

        new Pellet(this, Vector2.movementPredictVector(position, playerTarget.getPosition(), playerTarget.estimatedVelocity, 500f).normalize(),
                200f, 500f, 2000);
    }

    public void onDeath() {
        owner.minionDied(this);
    }
}
