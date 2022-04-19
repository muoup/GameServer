package com.Game.Entity.Enemy.Underground;

import com.Game.Entity.Enemy.Generic.AIType;
import com.Game.Entity.Enemy.Generic.Enemy;
import com.Game.Inventory.ItemList;
import com.Game.ItemData.DropTable;
import com.Game.WorldManagement.World;

public class Cockroach extends Enemy {
    public float timer2 = 0;

    public Cockroach(World world, int x, int y) {
        super(world, x, y);

        this.speed = 125f;
        this.name = "Cockroach";
        this.respawnTime = 8000;
        this.targetLostTime = 16000;
        this.idleAI = AIType::passiveRadiusWalk;
        this.targetAI = AIType::basicChase;

        setImage("CockRoach.png", 64, 64);
        setMaxHealth(25);
        setBounds(3577, 2793, 4159, 3355);
    }

    public void AI() {
//        timer += Server.dTime();
//        timer2 += Server.dTime();
//
//        if (range() > 256f)
//            moveToPlayer(playerTarget);
//
//        if (timer > 0.65f) {
//            new BeetleSpikeAvoidable(this, playerTarget.getPosition()).multiShotEnemy(playerTarget,20, 256, 5);
//            timer = 0;
//        }
//
//        if (timer2 > 1.35f) {
//            new BeetleSpike(this, playerTarget);
//            timer2 = 0;
//        }
    }

    public void handleDrops() {
        DropTable table = new DropTable();

        table.addItem(ItemList.arrow, 5, 1);
        table.addItem(ItemList.arrow, 5, 0.25);

        world.createGroundItem(position, table.determineOutput());
    }
}
