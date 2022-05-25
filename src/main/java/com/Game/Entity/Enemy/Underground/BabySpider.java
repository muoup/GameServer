package com.Game.Entity.Enemy.Underground;


import com.Game.Entity.Enemy.Generic.AIType;
import com.Game.Entity.Enemy.Generic.Enemy;
import com.Game.Inventory.ItemList;
import com.Game.Inventory.ItemStack;
import com.Game.ItemData.DropTable;
import com.Game.Projectile.WebProjectile;
import com.Game.WorldManagement.World;

import java.util.ArrayList;

public class BabySpider extends Enemy {

    public BabySpider(World world, int x, int y) {
        super(world, x, y);
        this.speed          = 125f;
        this.respawnTime    = 7500;
        this.maxMoveRadius  = 250;
        this.moveRadius     = 75;
        this.name           = "Spider";
        this.loseTargetTime = 7500;
        this.idleAI         = AIType::passiveRadiusCreep;
        this.targetAI       = AIType::passiveRadiusCreep;

        setImage("babySpider.png", 48, 48);
        setMaxHealth(25f);

        addProjTimer(2000, this::shootHoming);
    }

    public void shootHoming() {
        new WebProjectile(this, playerTarget, 45f, 250f);
    }

    public void handleDrops() {
        DropTable table = new DropTable();

        table.addItem(ItemList.stringItem, 1, 4, 1, true);
        table.addItem(ItemList.gold, 25, 50,  0.75);

        world.createGroundItem(position, table.determineOutput());
    }
}
