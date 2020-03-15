package com.Game.Items.Tool;

import com.Game.GUI.Inventory.InventoryManager;
import com.Game.Items.Item;
import com.Game.Items.ItemStack;

public class Usable extends Item {
    public Usable(int id, String imageName, String name, String examineText, int maxStack, int worth) {
        super(id, imageName, name, examineText, maxStack, worth);
    }

    public void OnClick(int index) {
        InventoryManager.useIndex = index;
    }

    public void setData(ItemStack stack) {
        stack.options.add("Use");
    }
}
