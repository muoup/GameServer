package com.Game.Entity.Enemy;

import com.Game.ConnectionHandling.Init.Server;
import com.Game.Inventory.ItemList;
import com.Game.Inventory.ItemStack;
import com.Game.Projectile.RockPellet;
import com.Game.Util.Math.DeltaMath;
import com.Game.Util.Math.Vector2;
import com.Game.WorldManagement.World;

import java.util.ArrayList;

public class BabyRockEnemy extends Enemy {
    float piecePercent = 1f;
    private float speed = 1.25f;
    private float maxDistance = 80f;
    private float timer = 0f;

    public BabyRockEnemy(World world, int x, int y) {
        super(world, x, y);

        setImage("babyRock.png");
        this.respawnTimer = 5f;
        this.maxTarget = 7.5f;
        this.name = "Baby Rock";
        setMaxHealth(100);
    }

    public void AI() {
        timer += Server.dTime();
        if (Vector2.distance(playerTarget.getPosition(), position) > maxDistance) {
            position.add(Vector2.magnitudeDirection(position, playerTarget.getPosition()).scale(speed));
        }

        if (timer > 0.45f) {
            timer = 0;
            new RockPellet(this, playerTarget.getPosition(), 25, speed * 2.35f);
        }
    }

    public void handleDrops() {
        ArrayList stacks = new ArrayList<ItemStack>();
        stacks.add(new ItemStack(ItemList.rockArrow, (int) DeltaMath.range(1, 10)));
        float rand = DeltaMath.range(0, 100);
        while (rand <= piecePercent) {
            stacks.add(new ItemStack(ItemList.values()[Math.round(DeltaMath.range(10, 13))], 1));

            rand = DeltaMath.range(0, 100);
        }

        world.createGroundItem(position, stacks);
    }
}
