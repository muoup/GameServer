package com.Game.Inventory;

import com.Game.ConnectionHandling.Save.SaveSettings;

public class InventoryManager {
    private ItemStack[] items;

    public InventoryManager(ItemStack[] itemsSaved) {
        if (itemsSaved.length != SaveSettings.inventoryAmount)
            System.err.println("an inventory error has occured, the length is not " + SaveSettings.inventoryAmount);

        items = itemsSaved;
    }

    public InventoryManager() {
        items = new ItemStack[20];
    }


}
