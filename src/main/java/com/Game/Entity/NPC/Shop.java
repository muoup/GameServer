package com.Game.Entity.NPC;

import com.Game.ConnectionHandling.Init.Server;
import com.Game.Entity.Player.Player;
import com.Game.Entity.Player.ShopHandler;
import com.Game.Inventory.ItemList;
import com.Game.Inventory.ItemStack;

public class Shop {
    protected String shopVerb = "Sell";
    protected String inventoryVerb = "Buy";

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

    public void inventoryInteraction(ShopHandler handler, int index, int amount) {
        handler.sellInventory(index, amount);
    }

    public void shopInteraction(ShopHandler handler, int index, int amount) {
        handler.buyOption(index, amount);
    }

    public void miscInteraction(ShopHandler handler, String message, int index, int amount) {}

    public void examineShop(ShopHandler handler, int index) {
        ItemStack item = offeredItems[index];

        handler.getPlayer().sendMessage(item.getExamineTextAbstract());
    }

    public void examineInventory(ShopHandler handler, int index) {
        ItemStack item = handler.getPlayer().getInventory().getStack(index);

        handler.getPlayer().sendMessage(item.getExamineTextAbstract());
    }

    public String getShopVerb() {
        return shopVerb;
    }

    public String getInventoryVerb() {
        return inventoryVerb;
    }

    public int getWorth(ItemStack stack) {
        return stack.getWorth();
    }

    public void sendItems(Player player) {
        for (ItemStack stack : offeredItems)
            Server.send(player, "sa", stack.name, stack.getImage(), getWorth(stack), stack.getExamineTextAbstract());
    }
}
