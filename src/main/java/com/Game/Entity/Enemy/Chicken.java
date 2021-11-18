package com.Game.Entity.Enemy;

import com.Game.Inventory.ItemList;
import com.Game.Inventory.ItemStack;
import com.Game.Util.Math.DeltaMath;
import com.Game.WorldManagement.World;

import java.util.ArrayList;

public class Chicken extends Enemy {
    public Chicken(World world, int x, int y) {
        super(world, x, y);
        this.id = 3;
        this.respawnTimer = 5.0f;
        this.maxTarget = 2f;
        this.name = "Chicken";
        this.speed = 1.25f;
        this.maxRadius = 200f;
        this.passive = true;

        setImage("chicken.png");
        setMaxHealth(10);
        setBounds(4133, 2641,
                4650, 3100);
    }

    public void passiveAI() {
        moveToAI();
    }

    public void onRespawn() {
        setMoveTo();
    }

    public void handleDrops() {
        ArrayList<ItemStack> stack = new ArrayList();
        stack.add(new ItemStack(ItemList.feather, (int) DeltaMath.range(4, 9)));
        world.createGroundItem(position, stack);
    }
}
