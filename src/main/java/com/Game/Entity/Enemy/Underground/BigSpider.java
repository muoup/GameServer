package com.Game.Entity.Enemy.Underground;

import com.Game.Entity.Enemy.Generic.AIType;
import com.Game.Entity.Enemy.Generic.Enemy;
import com.Game.Inventory.ItemList;
import com.Game.ItemData.DropTable;
import com.Game.Projectile.WebSkillShot;
import com.Game.WorldManagement.World;

import java.util.ArrayList;

public class BigSpider extends Enemy {
    public ArrayList<Enemy> minions = new ArrayList<Enemy>();

    public BigSpider(World world, int x, int y) {
        super(world, x, y);
        this.name = "Big Spider";
        this.speed = 75f;
        this.respawnTime = 6000;
        this.moveRadius = 256;
        this.loseTargetTime = 10000;
        this.loseFocusDistance = 1000;
        this.idleAI = AIType::passiveBoundaryCreep;
        this.targetAI = AIType::basicChase;

        setImage("bigSpider.png", 64, 64);
        setBounds(663, 1808, 1594, 2805);
        setMaxHealth(75);

        addProjTimer(1000, () -> new WebSkillShot(this, playerTarget.getPosition()).multiShot(40, 3));
        addProjTimer(4000, () -> {
            Enemy enemy = new BabySpider(world, (int) position.x, (int) position.y);

            enemy.setTarget(playerTarget);
            enemy.temporary = true;

            minions.add(enemy);
        });
    }

    public void update() {
    }

    public void loseTarget() {
        minions.forEach(Enemy::die);
        minions.clear();
    }

    public void handleDrops() {
        DropTable table = new DropTable();
        table.addItem(ItemList.stringItem, 1, 3, 1, true);
        table.addItem(ItemList.gold, 350, 500, 0.5);
        table.addItem(ItemList.copperOre, 1, 3, 0.5, true);
        table.addItem(ItemList.tinOre, 1, 3, 0.5, true);
        world.createGroundItem(position, table.determineOutput());

        minions.forEach(Enemy::die);
        minions.clear();
    }
}
