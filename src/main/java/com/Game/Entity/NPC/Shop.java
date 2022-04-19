package com.Game.Entity.NPC;

import com.Game.Inventory.ItemList;
import com.Game.Inventory.ItemStack;

public class Shop {
    public static Shop empty = new Shop(new ItemStack[0]);
    public static Shop fishing = new Shop(new ItemStack[] {
            new ItemStack(ItemList.clownfish, 1, 0),
            new ItemStack(ItemList.fishBait, 1, 0)
    });
    public static Shop kanuna = new Shop(new ItemStack[] {
            new ItemStack(ItemList.kanunaNecklace, 1, 0)
    });

    public ItemStack[] offeredItems;

    public Shop(ItemStack[] stacks) {
        offeredItems = stacks;
    }
}
