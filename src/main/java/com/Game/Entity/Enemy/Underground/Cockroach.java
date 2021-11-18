package com.Game.Entity.Enemy.Underground;

import com.Game.ConnectionHandling.Init.Server;
import com.Game.Entity.Enemy.Enemy;
import com.Game.Inventory.ItemList;
import com.Game.ItemData.DropTable;
import com.Game.Projectile.BeetleSpike;
import com.Game.Projectile.BeetleSpikeAvoidable;
import com.Game.WorldManagement.GroundItem;
import com.Game.WorldManagement.World;

public class Cockroach extends Enemy {
    public float timer2 = 0;

    public Cockroach(World world, int x, int y) {
        super(world, x, y);
        this.maxTarget = 7.5f;
        this.passive = false;
        this.speed = 1.25f;
        this.name = "Cockroach";
        this.respawnTimer = 8.0f;

        setImage("CockRoach.png");
        setMaxHealth(25);
        setScale(64, 64);
        setBounds(3577, 2793, 4159, 3355);
    }

    public void update() {
        if (!withinBounds())
            targetTimer = 0;
    }

    public void AI() {
        timer += Server.dTime();
        timer2 += Server.dTime();

        if (range() > 256f)
            moveToPlayer(playerTarget);

        if (timer > 0.65f) {
            new BeetleSpikeAvoidable(this, playerTarget.getPosition()).multiShotEnemy(playerTarget,20, 256, 5);
            timer = 0;
        }

        if (timer2 > 1.35f) {
            new BeetleSpike(this, playerTarget);
            timer2 = 0;
        }
    }

    public void passiveAI() {
        moveToAI();
    }

    public void handleDrops() {
        DropTable table = new DropTable();

        table.add(ItemList.arrow, 5, 1);
        table.add(ItemList.arrow, 5, 0.25);

        world.createGroundItem(position, table.determineOutput());
    }
}
