package com.Game.Entity.Enemy.Underground;

import com.Game.Entity.Enemy.Generic.AIType;
import com.Game.Entity.Enemy.Generic.Enemy;
import com.Game.Inventory.ItemList;
import com.Game.ItemData.DropTable;
import com.Game.Projectile.BeetleSpike;
import com.Game.Projectile.BeetleSpikeAvoidable;
import com.Game.WorldManagement.World;

public class Cockroach extends Enemy {
    public Cockroach(World world, int x, int y) {
        super(world, x, y);

        this.speed = 125f;
        this.name = "Cockroach";
        this.respawnTime = 8000;
        this.loseTargetTime = 16000;
        this.loseFocusDistance = 1000;
        this.followDistance = 256;
        this.idleAI = AIType::passiveBoundaryWalk;
        this.targetAI = AIType::basicChase;

        setImage("CockRoach.png", 128, 128);
        setMaxHealth(250);
        setBounds(3577, 2793, 4159, 3355);

        addProjTimer(2000, () -> new BeetleSpikeAvoidable(this, playerTarget.getPosition()).multiShot(45, 5));
        addProjTimer(5000, () -> new BeetleSpike(this, playerTarget));
    }

    public void AI() {
    }

    public void handleDrops() {
        DropTable table = new DropTable();

        table.addItem(ItemList.arrow, 5, 15, 1);

        world.createGroundItem(position, table.determineOutput());
    }
}
