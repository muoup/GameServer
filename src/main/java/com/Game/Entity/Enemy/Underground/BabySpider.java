package com.Game.Entity.Enemy.Underground;


import com.Game.Entity.Enemy.Generic.AIType;
import com.Game.Entity.Enemy.Generic.Enemy;
import com.Game.Inventory.ItemList;
import com.Game.Inventory.ItemStack;
import com.Game.Projectile.WebProjectile;
import com.Game.WorldManagement.World;

import java.util.ArrayList;

public class BabySpider extends Enemy {

    public BabySpider(World world, int x, int y) {
        super(world, x, y);
        this.speed = 75f;
        this.respawnTime = 7500;
        this.moveRadius = 100f;
        this.name = "Spider";
        this.targetLostTime = 7500;
        this.idleAI = AIType::passiveRadiusWalk;
        this.targetAI = AIType::basicChase;

        setImage("babySpider.png", 48, 48);
        setMaxHealth(12.5f);

        addProjTimer(3000, this::shootHoming);
    }

    public void shootHoming() {
        new WebProjectile(this, playerTarget, 10f, 60f);
    }

    public void handleDrops() {
        ArrayList<ItemStack> drops = new ArrayList<>();
        drops.add(new ItemStack(ItemList.stringItem, 1));
        world.createGroundItem(position, drops);
    }
}
