package com.Game.Entity.Enemy.Underground;

import com.Game.Entity.Enemy.Generic.Enemy;
import com.Game.Inventory.ItemList;
import com.Game.ItemData.DropTable;
import com.Game.WorldManagement.World;

import java.util.ArrayList;

public class BigSpider extends Enemy {
    public ArrayList<Enemy> minions = new ArrayList<Enemy>();

    public BigSpider(World world, int x, int y) {
        super(world, x, y);
        this.name = "Big Spider";
        this.passive = false;
        this.speed = 145f;
        this.respawnTime = 6000;

        setImage("bigSpider.png", 96, 96);
        setBounds(663, 1808, 1594, 2805);
        setMaxHealth(35);
    }

    public void update() {
    }

    public void loseTarget() {
        world.enemies.removeAll(minions);
        minions.clear();
    }

    public void AI() {
//        timer += Server.dTime();
//        timer2 += Server.dTime();
//
//        if (range() > 256f)
//            moveToPlayer(playerTarget);
//
//        if (timer > 1f) {
//            new WebSkillShot(this, playerTarget.getPosition()).multiShotEnemy(playerTarget, 20, 256, 3);
//            timer = 0;
//        }
//
//        if (timer2 > 4f) {
//            Enemy enemy = new BabySpider(world, (int) position.x, (int) position.y);
//
//            enemy.health = enemy.maxHealth;
//            enemy.enabled = true;
//            enemy.onHit(playerTarget);
//
//            minions.add(enemy);
//            createTemporary(enemy);
//            timer2 = 0;
//        }
    }

    public void handleDrops() {
        DropTable table = new DropTable();
        table.addItem(ItemList.stringItem, 5, 1);
        table.addItem(ItemList.gold, 750, 0.5);
        table.addItem(ItemList.copperOre, 1, 0.5);
        world.createGroundItem(position, table.determineOutput());

        minions.forEach(Enemy::die);
        minions.clear();
    }
}
