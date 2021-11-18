package com.Game.Entity.Enemy;

import com.Game.ConnectionHandling.Init.Server;
import com.Game.Inventory.ItemList;
import com.Game.Inventory.ItemStack;
import com.Game.Projectile.RockPellet;
import com.Game.Util.Math.DeltaMath;
import com.Game.Util.Math.Vector2;
import com.Game.WorldManagement.GroundItem;
import com.Game.WorldManagement.World;

import java.util.ArrayList;

public class RockEnemy extends Enemy {

    private float speed = 1.5f;
    private float maxDistance = 128f;
    private float piecePercent = 5f;
    private float timer = 0f;

    public RockEnemy(World world, int x, int y) {
        super(world, x, y);
        this.maxHealth = 250f;
        this.id = 1;
        this.respawnTimer = 7.5f;
        this.maxTarget = 10f;
        this.name = "Bigger Rock";

        setImage("rock.png");
        setMaxHealth(250);
    }

    public void AI() {
        timer += Server.dTime();

        if (Vector2.distance(playerTarget.getPosition(), position) > maxDistance) {
            position.add(Vector2.magnitudeDirection(position, playerTarget.getPosition()).scale(speed));
        }

        if (timer > 0.65f) {
            new RockPellet(this, playerTarget.getPosition(), 25, speed * 2.5f);
            new RockPellet(this, playerTarget.getPosition().addClone(new Vector2(32, 0)), 25, speed * 2.5f);
            new RockPellet(this, playerTarget.getPosition().addClone(new Vector2(64, 0)), 25, speed * 2.5f);
            new RockPellet(this, playerTarget.getPosition().addClone(new Vector2(-32, 0)), 25, speed * 2.5f);
            new RockPellet(this, playerTarget.getPosition().addClone(new Vector2(-64, 0)), 25, speed * 2.5f);
            timer = 0;
        }
    }

    public void handleDrops() {
        ArrayList stacks = new ArrayList<ItemStack>();
        stacks.add(new ItemStack(ItemList.rockArrow, (int) DeltaMath.range(2, 20)));
        float rand = DeltaMath.range(0, 100);
        while (rand <= piecePercent) {
            stacks.add(new ItemStack(ItemList.values()[Math.round(DeltaMath.range(10, 13))], 1));

            rand = DeltaMath.range(0, 100);
        }

        world.createGroundItem(position, stacks);
    }
}
