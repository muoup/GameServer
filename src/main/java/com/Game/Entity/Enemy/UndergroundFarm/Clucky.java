package com.Game.Entity.Enemy.UndergroundFarm;

import com.Game.Entity.Enemy.Generic.Enemy;
import com.Game.Inventory.ItemList;
import com.Game.ItemData.DropTable;
import com.Game.Projectile.Pellet;
import com.Game.Util.Math.DeltaMath;
import com.Game.Util.Math.Vector2;
import com.Game.WorldManagement.World;

import java.util.ArrayList;

public class Clucky extends Enemy {
    private ArrayList<AngryChicken> minions;
    private float minionSpawnDistance = 64;
    private int maxMinions = 3;

    public Clucky(World world, int x, int y) {
        super(world, x, y);
        this.name = "Clucky The Undisturbed";
        this.loseTargetTime = 45000;
        this.speed = 50f;
        this.minions = new ArrayList<>();
        this.respawnTime = 20000;
        this.loseFocusDistance = 1500;

        setImage("clucky.png", 128, 128);
        setMaxHealth(450);
        addProjTimer(15000, this::spawnMinion);
        addProjTimer(3500, this::starAttack);
    }

    public void spawnMinion() {
        // repeat method until we spawn minionsperwave minions
        while (minions.size() < maxMinions) {
            float radians = DeltaMath.range(0, (float) Math.PI * 2);
            Vector2 offset = new Vector2((float) Math.cos(radians) * minionSpawnDistance, (float) Math.sin(radians) * minionSpawnDistance);

            AngryChicken newMinion = new AngryChicken(world, this, getX() + (int) offset.x, getY() + (int) offset.y);

            newMinion.setTarget(playerTarget);

            minions.add(newMinion);
        }
    }

    public void starAttack() {
        float initialRadians = (float) Math.atan2(playerTarget.getY() - getY(), playerTarget.getX() - getX());

        for (int i = 0; i < 8; i++) {
            float radians = initialRadians + (float) Math.PI * 2 * i / 8;
            float speed = (float) Math.random() * 50 + 50;
            float x = (float) Math.cos(radians) * speed;
            float y = (float) Math.sin(radians) * speed;

            Vector2 velocity = new Vector2(x, y).normalize();

            new Pellet(this, velocity, 25f, 200f, 10000);
        }
    }

    public void handleDrops() {
        minions.clear();

        // create a drop table
        // add a random chance for each item
        DropTable dropTable = new DropTable();
        dropTable.addItem(ItemList.feather, 100, 1f);
        dropTable.addItem(ItemList.feather, 250, 0.5f);
        dropTable.addItem(ItemList.chickenShield, 1, 0.05f);
        world.createGroundItem(position, dropTable.determineOutput());
    }

    public void minionDied(AngryChicken angryChicken) {
        minions.remove(angryChicken);
    }
}
