package com.Game.Entity.Enemy.Underground;

import com.Game.ConnectionHandling.Init.Server;
import com.Game.Entity.Enemy.Enemy;
import com.Game.Inventory.ItemList;
import com.Game.ItemData.DropTable;
import com.Game.Projectile.WebSkillShot;
import com.Game.WorldManagement.GroundItem;
import com.Game.WorldManagement.World;

import java.util.ArrayList;

public class BigSpider extends Enemy {
    public ArrayList<Enemy> minions = new ArrayList<Enemy>();

    public BigSpider(World world, int x, int y) {
        super(world, x, y);
        this.name = "Big Spider";
        this.passive = false;
        this.speed = 1.45f;
        this.maxTarget = 7.5f;
        this.respawnTimer = 6.0f;

        timer2 = 2;

        setImage("bigSpider.png");
        setScale(64, 64);
        setBounds(663, 1808, 1594, 2805);
        setMaxHealth(35);
    }

    public void update() {
        if (!withinBounds()) {
            targetTimer = 0;
            setMoveTo();
        }
    }

    public void loseTarget() {
        world.enemies.removeAll(minions);
        minions.clear();
    }

    public void AI() {
        timer += Server.dTime();
        timer2 += Server.dTime();

        if (range() > 256f)
            moveToPlayer(playerTarget);

        if (timer > 1f) {
            new WebSkillShot(this, playerTarget.getPosition()).multiShotEnemy(playerTarget, 20, 256, 3);
            timer = 0;
        }

        if (timer2 > 4f) {
            Enemy enemy = new BabySpider(world, (int) position.x, (int) position.y);

            enemy.health = enemy.maxHealth;
            enemy.enabled = true;
            enemy.target();

            minions.add(enemy);
            createTemporary(enemy);
            timer2 = 0;
        }
    }

    public void passiveAI() {
        moveToAI();
    }

    public void handleDrops() {
        DropTable table = new DropTable();
        table.add(ItemList.stringItem, 5, 1);
        table.add(ItemList.gold, 750, 0.5);
        table.add(ItemList.copperOre, 1, 0.5);
        GroundItem.createGroundItem(world, position, table.determineOutput());

        minions.forEach(Enemy::handleDrops);
        minions.clear();
    }
}
