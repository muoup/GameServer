package com.Game.Objects.Utilities.Interfaces;

import com.Game.Entity.Player.ShopHandler;
import com.Game.Inventory.ItemList;
import com.Game.Inventory.ItemStack;

public class AnvilInterface extends ModifiedShop {
    public AnvilInterface() {
        super(new ItemStack[] {
                new ItemStack(ItemList.copperOre, 1),
                new ItemStack(ItemList.tinOre, 1),
                new ItemStack(ItemList.ironOre, 1),
                new ItemStack(ItemList.goldOre, 1)
                                    },
                "Smelt", "N/A");
    }

    public void shopInteraction(ShopHandler handler, String command, int index, int amount) {

    }

    public void inventoryInteraction(ShopHandler handler, String command, int index, int amount) {

    }
}
