package com.Game.Inventory;

import com.Game.ConnectionHandling.Client;
import com.Game.ConnectionHandling.Save.SaveSettings;
import com.Game.Entity.Player.Player;

public class AccessoriesManager {
    public static final int WEAPON_SLOT = 0;
    public static final int AMMO_SLOT = 4;

    public static final int HELMET_SLOT = 3;
    public static final int NECKLACE_SLOT = 7;
    public static final int CHESTPLATE_SLOT = 11;
    public static final int LEGGING_SLOT = 15;
    public static final int BOOT_SLOT = 19;

    public ItemStack[] accessories = new ItemStack[SaveSettings.accessoryAmount];
    private Player player;

    public AccessoriesManager(Player player) {
        for (int i = 0; i < accessories.length; i++) {
            accessories[i] = Item.emptyStack();
        }

        this.player = player;
    }

    public ItemStack getSlot(int slot) {
        return accessories[slot];
    }

    public void handleInventory() {
        for (int i = 0; i < accessories.length; i++) {
            if (accessories[i].getAmount() <= 0 && accessories[i].getID() != 0) {
                accessories[i] = Item.emptyStack();
            }
        }
    }

    public void addAmount(int slot, int amount) {
        setSlot(slot, new ItemStack(accessories[slot].getItem(), amount + accessories[slot].getAmount(), accessories[slot].getData()));
    }

    public void setSlot(int slot, ItemStack item) {
        int stackSet = Math.min(item.getAmount(), item.maxStack());

        accessories[slot] = new ItemStack(item.getItemList(), stackSet, item.getData());

        Client.sendAccessorySlot(player, slot, item);

        calculateStats();
    }

    public void calculateStats() {
        player.armor = 0;
        player.damageMult = 0;
        player.speedMult = 0;

        for (ItemStack item : accessories) {
            player.armor += item.armor;
            player.damageMult += item.damage;
            player.speedMult += item.speed;
        }
    }

    public void unequip(Player player, int index) {
        ItemStack equip = accessories[index].clone();

        setSlot(index, ItemStack.empty());
        player.addItem(equip);
    }
}
