package com.Game.Entity.Enemy.Underground;

import com.Game.Entity.Enemy.Generic.Enemy;
import com.Game.WorldManagement.World;

public class SpiderRoach extends Enemy {
    public SpiderRoach(World world,  int x, int y) {
        super(world, x, y);
        this.targetLostTime = 25500;
        this.speed = 250;
        this.name = "Spider Roach";

        setImage("SpiderRoach.png", 128, 128);
        setMaxHealth(150);
    }

    public void AI() {

    }
}
