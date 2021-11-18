package com.Game.Entity.Enemy.Underground;

import com.Game.Entity.Enemy.Enemy;
import com.Game.WorldManagement.World;

public class SpiderRoach extends Enemy {
    public SpiderRoach(World world,  int x, int y) {
        super(world, x, y);
        this.maxTarget = 25.5f;
        this.name = "Spider Roach";

        setImage("SpiderRoach.png");
        setScale(128, 128);
        setMaxHealth(150);
    }

    public void AI() {

    }

    public void passiveAI() {

    }
}
