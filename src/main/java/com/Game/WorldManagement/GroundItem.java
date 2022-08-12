package com.Game.WorldManagement;

import com.Game.ConnectionHandling.Client;
import com.Game.Entity.Enemy.Generic.Enemy;
import com.Game.Inventory.ItemStack;
import com.Game.Util.Math.DeltaMath;
import com.Game.Util.Math.Vector2;
import com.Game.Util.Other.Settings;

import java.awt.*;
import java.util.ArrayList;

public class GroundItem {
    public Vector2 position;
    public World world;
    public ArrayList<ItemStack> stack;
    private static final float maxDistance = 256f;
    private long time;
    public int randomToken;

    public GroundItem(World world, Vector2 position, ArrayList<ItemStack> items) {
        this.position = position.clone();
        this.stack = new ArrayList<>();
        this.time = System.currentTimeMillis();
        this.world = world;

        handleStack(items);
        setRandomToken();
        world.sendGroundItem(this);
    }

    public GroundItem(World world, Vector2 position, ItemStack drop) {
        this.position = position.clone();
        this.stack = new ArrayList<>();
        this.time = System.currentTimeMillis();
        this.world = world;

        handleItem(drop);
        setRandomToken();
        world.sendGroundItem(this);
    }

    private void setRandomToken() {
        while (tokenNotUnique() || randomToken == 0) {
            randomToken = (int) DeltaMath.range(0, 10000);
        }
    }

    private boolean tokenNotUnique() {
        for (GroundItem g : world.groundItems)
            if (g.randomToken == randomToken)
                return true;

        return false;
    }

    public void handleStack(ArrayList<ItemStack> stack) {
        stack.forEach(this::handleItem);
    }

    public void handleItem(ItemStack stack) {
        this.stack.add(stack);
    }

    public void updateStack() {
        update();
    }

    public void addItem(ItemStack item) {
        for (int i = 0; i < stack.size(); i++) {
            ItemStack s = stack.get(i);
            if (s.getID() == item.getID() && s.getData() == s.getData()) {
                changeAmount(i, item.getAmount());
                return;
            }
        }

        stack.add(item);
        time = System.currentTimeMillis();
    }

    public void addItems(ArrayList<ItemStack> stack) {
        stack.forEach(this::addItem);
    }

    public void changeAmount(int index, int amount) {
        stack.get(index).amount += amount;

        if (stack.get(index).amount <= 0) {
            removeItem(index);
        }

        if (stack.size() == 0) {
            world.removeGroundItem(this);
            return;
        }

        world.sendGroundItemChange(this);
    }

    private void removeItem(int index) {
        stack.remove(index);
    }

    public void update() {
        for (int i = 0; i < stack.size(); i++) {
            if (stack.get(i).getAmount() <= 0)
                stack.remove(i);
        }

        if (stack.size() == 0 || System.currentTimeMillis() - time >= Settings.groundItemDuration)
            world.groundItems.remove(this);
    }

    public String toString() {
        return packetInfo();
    }

    public String packetInfo() {
        StringBuilder items = new StringBuilder();

        for (ItemStack stack : stack) {
            items.append("0;" + stack.getServerPacket() + "=>");
        }

        return position + ";" + randomToken + "=items>" + items;
    }
}
