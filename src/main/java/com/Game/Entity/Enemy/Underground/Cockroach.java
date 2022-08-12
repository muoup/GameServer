package com.Game.Entity.Enemy.Underground;

import com.Game.Entity.Enemy.Generic.AIType;
import com.Game.Entity.Enemy.Generic.Enemy;
import com.Game.Entity.Player.Player;
import com.Game.Inventory.ItemList;
import com.Game.ItemData.DropTable;
import com.Game.Projectile.BeetleSpike;
import com.Game.Projectile.BeetleSpikeAvoidable;
import com.Game.Util.Math.Vector2;
import com.Game.WorldManagement.World;

public class Cockroach extends UndergroundEnrageable {

    public Cockroach(World world) {
        super(world, 0, 0);

        this.speed = 150f;
        this.name = "Cockroach";
        this.respawnTime = 8000;
        this.loseTargetTime = 16000;
        this.loseFocusDistance = 1024;
        this.followDistance = 400;
        this.idleAI = AIType::passiveBoundaryWalk;
        this.targetAI = AIType::basicChase;

        setImage("CockRoach.png", 128, 128);
        setMaxHealth(250);
        setBounds(3064, 2463, 4137, 3441);
        setSpawnPosition(bounds.randomPoint());

        addProjTimer(750, () -> new BeetleSpikeAvoidable(this, playerTarget.getPosition(), 350f)
                .multiShot(35, 5));
        addProjTimer(1000, () -> new BeetleSpikeAvoidable(this, predict(400f), 350f)
                .multiShot(35, 5));
        addProjTimer(1000, this::isEnraged, () -> new BeetleSpikeAvoidable(this, predict(400f), 350f));

        addProjTimer(5000, this::notEnraged, () -> new BeetleSpike(this, playerTarget, 250f));
    }

    public void handleDrops() {
        DropTable table = new DropTable();

        table.addItem(ItemList.arrow, 5, 15, 1);

        world.createGroundItem(position, table.determineOutput());
    }
}
