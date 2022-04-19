package com.Game.Entity.Enemy;

import com.Game.ConnectionHandling.Init.Server;
import com.Game.Entity.Enemy.Generic.AIType;
import com.Game.Entity.Enemy.Generic.Enemy;
import com.Game.Entity.Player.Player;
import com.Game.Inventory.ItemList;
import com.Game.Inventory.ItemStack;
import com.Game.Util.Math.DeltaMath;
import com.Game.Util.Math.Vector2;
import com.Game.WorldManagement.World;

import java.util.ArrayList;

public class Chicken extends Enemy {
    public Chicken(World world) {
        super(world, 0, 0);
        this.id = 3;
        this.respawnTime = 5000;
        this.targetLostTime = 2000;
        this.name = "Chicken";
        this.speed = 80f;
        this.idleAI = AIType::passiveBoundaryWalk;

        setImage("chicken.png", 64, 64);
        setMaxHealth(10);
        setBounds(3513, 1279,
                4143, 1912);

        this.spawnPosition = new Vector2(DeltaMath.range(b1.x, b2.x), DeltaMath.range(b1.y, b2.y));
        setPosition(spawnPosition.clone());

        //addPassiveTimer(1000, this::chickenTest);
    }

    public void update() {

    }

    public void chickenTest() {
        if (moveTo == null || movement == null)
            return;

        for (Player player : world.players) {
            Server.send(player, "cc", position, moveTo, movement);
        }
    }

    public void handleDrops() {
        ArrayList<ItemStack> stack = new ArrayList<>();
        stack.add(new ItemStack(ItemList.feather, (int) DeltaMath.range(4, 9)));
        world.createGroundItem(position, stack);
    }
}
