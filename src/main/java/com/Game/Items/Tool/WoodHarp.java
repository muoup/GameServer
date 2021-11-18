package com.Game.Items.Tool;

import com.Game.ConnectionHandling.Client;
import com.Game.Entity.Player.Player;
import com.Game.Inventory.InventoryManager;
import com.Game.Inventory.Item;
import com.Game.Inventory.ItemList;
import com.Game.Inventory.ItemStack;

public class WoodHarp extends Usable {

    public WoodHarp(int id, String name, String examineText, int worth, boolean stackable) {
        super(id, name, examineText, worth, stackable);

        setImage("wood_harp.png");
    }

    public void use(Player player, int index) {
        ItemStack stack = player.inventory.getStack(index);
        int worth = stack.getWorth();
        if (worth <= 0) {
            Client.sendMessage(player, "You cannot cash that item.");
            return;
        } else if (worth * 0.35 < 1) {
            Client.sendMessage(player,"That item would grant you less than a coin. It was not harmonised.");
            return;
        }

        player.inventory.setItem(index, Item.emptyStack());
        player.addItem(ItemList.gold, (int) (worth * 0.35));
    }
}
