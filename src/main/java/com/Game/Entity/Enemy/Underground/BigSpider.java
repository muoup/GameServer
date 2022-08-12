package com.Game.Entity.Enemy.Underground;

import com.Game.Entity.Enemy.Generic.AIType;
import com.Game.Entity.Enemy.Generic.Enemy;
import com.Game.Inventory.ItemList;
import com.Game.ItemData.DropTable;
import com.Game.Projectile.WebSkillShot;
import com.Game.WorldManagement.World;

import java.util.ArrayList;

public class BigSpider extends UndergroundEnrageable {
    public ArrayList<Enemy> minions = new ArrayList<>();

    public BigSpider(World world) {
        super(world, 0, 0);
        this.name = "Big Spider";
        this.speed = 75f;
        this.respawnTime = 6000;
        this.moveRadius = 256;
        this.loseTargetTime = 10000;
        this.loseFocusDistance = 1000;
        this.idleAI = AIType::passiveBoundaryCreep;
        this.targetAI = AIType::basicChase;

        setImage("bigSpider.png", 64, 64);
        setBounds(616, 2194, 1496, 2777);
        setMaxHealth(75);
        setSpawnPosition(bounds.randomPoint());

        addProjTimer(1000, this::notEnraged, () -> new WebSkillShot(this, playerTarget.getPosition(), 250, 75).multiShot(40, 3));
        addProjTimer(750, this::isEnraged, () -> new WebSkillShot(this, predict(250), 250, 75).multiShot(40, 9));
        addProjTimer(4000, this::notEnraged, () -> {
            Enemy enemy = new BabySpider(world, (int) position.x, (int) position.y);

            enemy.targetPlayer(playerTarget);
            enemy.temporary = true;

            minions.add(enemy);
        });
    }

    public void onPlayerTarget() {
        for (Enemy enemy : minions) {
            enemy.targetPlayer(playerTarget);
        }
    }

    public void onTargetLost() {
        minions.forEach(Enemy::kill);
        minions.clear();
    }

    public void handleDrops() {
        DropTable table = new DropTable();
        table.addItem(ItemList.stringItem, 1, 3, 1, true);
        table.addItem(ItemList.gold, 350, 500, 0.5);
        table.addItem(ItemList.copperOre, 1, 3, 0.5, true);
        table.addItem(ItemList.tinOre, 1, 3, 0.5, true);
        world.createGroundItem(position, table.determineOutput());

        minions.forEach(Enemy::kill);
        minions.clear();
    }
}
