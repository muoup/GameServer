package com.Game.Entity.Enemy.Underground;


import com.Game.Entity.Enemy.Generic.AIType;
import com.Game.Entity.Enemy.Generic.Enemy;
import com.Game.Entity.Player.Player;
import com.Game.Inventory.ItemList;
import com.Game.Inventory.ItemStack;
import com.Game.ItemData.DropTable;
import com.Game.Projectile.WebProjectile;
import com.Game.Projectile.WebSkillShot;
import com.Game.Util.Math.DeltaMath;
import com.Game.Util.Math.Vector2;
import com.Game.WorldManagement.World;

import java.util.ArrayList;

public class BabySpider extends UndergroundEnrageable {

    public BabySpider(World world) {
        super(world, 0, 0);
        this.speed          = 125f;
        this.respawnTime    = 7500;
        this.maxMoveRadius  = 250;
        this.loseFocusDistance = 1024;
        this.moveRadius     = 75;
        this.name           = "Spider";
        this.loseTargetTime = 7500;
        this.idleAI         = AIType::passiveBoundaryCreep;
        this.targetAI       = AIType::passiveBoundaryCreep;

        setBounds(2247, 906, 3147, 1656);
        setImage("babySpider.png", 48, 48);
        setMaxHealth(25f);
        setSpawnPosition(bounds.randomPoint());

        addProjTimer(2000, this::notEnraged, this::shootHoming);
        addProjTimer(1200, this::isEnraged, this::shootRandom);
    }

    public void shootRandom() {
        if (DeltaMath.randBool()) {
            new WebSkillShot(this, playerTarget.getPosition(), 350, 200);
        } else {
            new WebSkillShot(this, predict(350f), 350, 200);
        }
    }

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
