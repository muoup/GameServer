package com.Game.Objects.Utilities.Interfaces;

import com.Game.Entity.NPC.Shop;
import com.Game.Entity.Player.ShopHandler;
import com.Game.Inventory.ItemStack;

public abstract class ModifiedShop extends Shop {
    public ModifiedShop(ItemStack[] stacks, String shopVerb, String inventoryVerb) {
        super(stacks);
        this.shopVerb = shopVerb;
        this.inventoryVerb = inventoryVerb;
    }

    public abstract void shopInteraction(ShopHandler handler, String command, int index, int amount);

    public abstract void inventoryInteraction(ShopHandler handler, String command, int index, int amount);

    public void miscInteraction(ShopHandler handler, String message, int index, int amount) {}
}
