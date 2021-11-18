package com.Game.Entity.Enemy.Underground;

import com.Game.ConnectionHandling.Init.Server;
import com.Game.Entity.Enemy.Enemy;
import com.Game.Inventory.ItemList;
import com.Game.Inventory.ItemStack;
import com.Game.Projectile.WebProjectile;
import com.Game.Util.Math.DeltaMath;
import com.Game.Util.Math.Vector2;
import com.Game.WorldManagement.GroundItem;
import com.Game.WorldManagement.World;

import java.util.ArrayList;

public class BabySpider extends Enemy {
    private float passiveTimer, shotTimer, speed, maxDistance;
    private Vector2 walkTo = Vector2.zero();
    private boolean walking;

    public BabySpider(World world, int x, int y) {
        super(world, x, y);
        this.speed = 2.5f;
        this.maxDistance = 125f;
        this.shotTimer = DeltaMath.range(0, 0.0525f);
        this.respawnTimer = 7.5f;
        this.maxTarget = 7.5f;
        this.name = "Spider";
        this.passiveTimer = DeltaMath.range(0.25f, 0.75f);

        setImage("babySpider.png");
        setMaxHealth(12.5f);
    }

    public void AI() {
        if (maxDistance < Vector2.distance(position, playerTarget.getPosition())) {
            position.add(Vector2.magnitudeDirection(position, playerTarget.getPosition()).scale(speed));
        }

        shotTimer -= Server.dTime();

        if (shotTimer < 0) {
            shotTimer = DeltaMath.range(0.75f, 1.75f);
            new WebProjectile(this, playerTarget, 12.5f, (speed / shotTimer) * 2.25f, shotTimer * 2.5f);
        }
    }

    public void passiveAI() {
        if (!walking)
            passiveTimer -= Server.dTime();
        else {
            position.add(Vector2.magnitudeDirection(position, walkTo).scale(speed));
        }

        if (passiveTimer < 0) {
            walking = true;
            passiveTimer = DeltaMath.range(0.25f, 0.75f);
            walkTo = spawnPosition.addClone(DeltaMath.range(0, 60), DeltaMath.range(0, 60));
        }

        if (Vector2.distance(position, walkTo) < 12.5f) {
            walking = false;
        }
    }

    public void handleDrops() {
        ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
        drops.add(new ItemStack(ItemList.stringItem, 1));
        world.createGroundItem(position, drops);
    }
}
