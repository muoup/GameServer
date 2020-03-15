package com.Game.Items.Tool;

import com.Game.GUI.Chatbox.ChatBox;
import com.Game.GUI.Inventory.InventoryManager;
import com.Game.Items.Item;
import com.Game.Items.ItemList;
import com.Game.Items.ItemStack;

public class WoodHarp extends Usable {

    public WoodHarp(int id, String imageName, String name, String examineText, int maxStack, int worth) {
        super(id, imageName, name, examineText, maxStack, worth);
    }

    public void use(int index) {
        ItemStack stack = InventoryManager.getStack(index);
        int worth = stack.getWorth();
        if (worth <= 0) {
            ChatBox.sendMessage("You cannot cash that item.");
            return;
        } else if (worth * 0.35 < 1) {
            ChatBox.sendMessage("That item would grant you less than a coin. It was not harmonised.");
            return;
        }

        InventoryManager.setItem(index, Item.emptyStack());
        InventoryManager.addItem(ItemList.gold, (int) (worth * 0.35));
    }
}
