package com.Game.WorldManagement;

import com.Game.Inventory.ItemStack;
import com.Game.Util.Math.Vector2;
import com.Game.Util.Other.Settings;

import java.awt.*;
import java.util.ArrayList;

public class GroundItem {
    public Vector2 position;
    public World world;
    public ArrayList<ItemStack> stack;
    private Image topImage;
    private static final float maxDistance = 256f;
    private long time = 0;

    public GroundItem(World world, int x, int y, ArrayList<ItemStack> items) {
        this.position = new Vector2(x, y);
        this.stack = new ArrayList<ItemStack>();
        this.time = System.currentTimeMillis();
        this.world = world;

        handleStack(items);

        world.groundItems.add(this);
    }

    public GroundItem(Vector2 position, ArrayList<ItemStack> items) {
        this.position = position.clone();
        this.stack = new ArrayList<ItemStack>();
        this.time = System.currentTimeMillis();
        handleStack(items);

        world.groundItems.add(this);
    }

    public GroundItem(Vector2 position, ItemStack drop) {
        this.position = position.clone();
        this.stack = new ArrayList<ItemStack>();
        this.time = System.currentTimeMillis();
        handleItem(drop);

        world.groundItems.add(this);
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
        for (ItemStack s : stack) {
            if (s.getID() == item.getID() && s.getData() == s.getData()) {
                s.amount += item.getAmount();
                return;
            }
        }

        stack.add(item);

        time = System.currentTimeMillis();
    }

    public void addItems(ArrayList<ItemStack> stack) {
        stack.forEach(this::addItem);
    }

    public void update() {
        for (int i = 0; i < stack.size(); i++) {
            if (stack.get(i).getAmount() <= 0)
                stack.remove(i);
        }

        if (stack.size() == 0 || System.currentTimeMillis() - time >= Settings.groundItemDuration)
            world.groundItems.remove(this);
    }

    public static void createGroundItem(World world, Vector2 position, ArrayList<ItemStack> drops) {
        for (GroundItem i : world.groundItems) {
            if (Vector2.distance(i.position, position) < maxDistance) {
                i.addItems(drops);
                return;
            }
        }

        new GroundItem(position, drops);

    }

    public static void createGroundItem(World world, Vector2 position, ItemStack drop) {
        for (GroundItem i : world.groundItems) {
            if (Vector2.distance(i.position, position) < maxDistance) {
                i.addItem(drop);
                return;
            }
        }

        new GroundItem(position, drop);
    }

    public String toString() {
        StringBuilder ret = new StringBuilder();
        stack.forEach(ret::append);

        return ret.toString();
    }
}
