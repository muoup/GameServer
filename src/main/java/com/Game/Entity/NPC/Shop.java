package com.Game.Entity.NPC;

import com.Game.ConnectionHandling.Init.Server;
import com.Game.Entity.Player.Player;
import com.Game.Entity.Player.ShopHandler;
import com.Game.Inventory.ItemList;
import com.Game.Inventory.ItemStack;
import com.Game.Util.Other.Settings;

import java.awt.*;

public class Shop {
    protected String shopVerb = "Buy";
    protected String inventoryVerb = "Sell";

    protected float buyMultiplier = 1;
    protected float sellMultiplier = 1;

    protected boolean showBuyPrice = true;

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
        handler.sellInventory(index, amount, sellMultiplier * Settings.shopSellMultiplier);
    }

    public void shopInteraction(ShopHandler handler, int index, int amount) {
        handler.buyOption(index, amount, buyMultiplier);
    }

    public void miscInteraction(ShopHandler handler, String message, int index, int amount) {}

    public void examineShop(ShopHandler handler, int index) {
        ItemStack item = offeredItems[index];

        handler.getPlayer().sendMessage(item.getExamineTextAbstract());

        if (shopVerb.equals("Buy"))
            handler.getPlayer().sendMessage("You can buy this item for " + getShopPrice(index) + " coins.");
    }

    public void examineInventory(ShopHandler handler, int index) {
        ItemStack item = handler.getPlayer().getInventory().getStack(index);

        handler.getPlayer().sendMessage(item.getExamineTextAbstract());

        if (inventoryVerb.equals("Sell"))
            handler.getPlayer().sendMessage("You can sell this item for " + getInventoryPrice(handler, index) + " coins.");
    }

    public int getShopPrice(int index) {
        ItemStack selected = offeredItems[index];

        return getBuyPrice(selected);
    }

    public int getInventoryPrice(ShopHandler handler, int index) {
        ItemStack selected = handler.getPlayer().getInventory().getStack(index);

        return getSellPrice(selected);
    }

    public String getShopVerb() {
        return shopVerb;
    }

    public String getInventoryVerb() {
        return inventoryVerb;
    }

    public String extraInfoPacket(Player player) {
        return null;
    }

    public void sendItems(Player player) {
        for (int i = 0; i < offeredItems.length; i++) {
            sendItem(player, i);
        }
    }

    public void sendItem(Player player, int index) {
        ItemStack stack = offeredItems[index];
        Server.send(player, "sa", stack.name, stack.getImage(), getShopPrice(index), stack.getExamineTextAbstract());
    }

    public void sendItem(Player player, String name, String image, int price, String examine) {
        Server.send(player, "sa", name, image, price, examine);
    }

    public int getSellPrice(ItemStack stack) {
        return (int) (stack.getSingleValue() * getSellMultiplier());
    }

    public int getBuyPrice(ItemStack stack) {
        return (int) (stack.getSingleValue() * getBuyMultiplier());
    }

    public float getSellMultiplier() {
        return sellMultiplier * Settings.shopSellMultiplier;
    }

    public float getBuyMultiplier() {
        return buyMultiplier;
    }

    public int getStackIndex(ItemStack stack) {
        for (int i = 0; i < offeredItems.length; i++) {
            if (offeredItems[i].equals(stack))
                return i;
        }
        return -1;
    }

    public boolean showPrices() {
        return showBuyPrice;
    }
}
