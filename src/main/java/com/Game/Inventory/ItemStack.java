package com.Game.Inventory;

import com.Game.Player.Player;

import java.util.ArrayList;

/**
 * Physical stacks held in the inventory or other storage devices.
 */
public class ItemStack {
    protected Item item;
    protected int amount, data;
    protected ArrayList<String> options;

    public ItemStack(Item item, int amount, int data) {
        this.item = item;
        this.amount = amount;
        this.data = data;
        this.options = new ArrayList();
    }

    public ItemStack(ItemList item, int amount, int data) {
        this(item.getItem(), amount, data);
    }

    public boolean compare(ItemStack stack) {
        return this.item.getID() == stack.item.getID() &&
                data == stack.data;
    }

    public ArrayList<String> getOptions() {
        return options;
    }

    public String getImage() {
        return item.getImageToken();
    }

    public boolean hasAtleast(ItemStack minimum) {
        return amount > minimum.amount && compare(minimum);
    }

    public boolean hasAtleast(int amount) {
        return this.amount > amount;
    }

    public String getName(Player player, int data) {
        return item.getName();
    }

    public Item getItem() {
        return this.item;
    }

    public int getAmount() {
        return amount;
    }

    public int getData() {
        return data;
    }
}