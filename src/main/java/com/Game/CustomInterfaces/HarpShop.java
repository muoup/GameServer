package com.Game.CustomInterfaces;

import com.Game.Entity.NPC.Shop;
import com.Game.Entity.Player.Player;
import com.Game.Entity.Player.ShopHandler;
import com.Game.Inventory.ItemStack;

import java.util.ArrayList;

public class HarpShop extends Shop {

    public HarpShop(ItemStack[] stacks, float priceModifier) {
        super(stacks);
        this.sellMultiplier = priceModifier;
        this.shopVerb = "Sell";
    }

    public String extraInfoPacket(Player player) {
        return "DrawString(center, bottom, Sell items for " + (sellMultiplier * 100) + "% of their original value!)";
    }

    public void shopInteraction(ShopHandler handler, int index, int amount) {
        ItemStack selected = getShopItem(handler.getPlayer(), index);

        int i = handler.getPlayer().inventory.getIndex(selected);

        if (i == -1) {
            handler.getPlayer().sendMessage("You don't have that any more of that item!");
            return;
        }

        inventoryInteraction(handler, i, amount);
    }

    public void examineShop(ShopHandler handler, int index) {
        ItemStack selected = getShopItem(handler.getPlayer(), index);

        int i = handler.getPlayer().inventory.getIndex(selected);

        if (i == -1) {
            handler.getPlayer().sendMessage("You cannot sell what you no longer have!");
            return;
        }

        examineInventory(handler, i);
    }

    public void sendItems(Player player) {
        ArrayList<ItemStack> items = new ArrayList<>();

        add:
        for (int i = 0; i < 20; i++) {
            ItemStack stack = player.inventory.getStack(i).clone();

            if (stack.getSingleValue() <= 0)
                continue;

            // If the itemstack is not in items, add it
            for (ItemStack item : items) {
                if (item.compareIgnoreStack(stack))
                    continue add;
            }

            items.add(stack);
        }

        items.forEach((stack) -> sendItem(player, stack.getName(),
                        stack.getImage().getToken(), getSellPrice(stack), stack.getExamineTextAbstract()));

        player.pushReference("harpShop", items);
    }

    public ItemStack getShopItem(Player player, int index) {
        ArrayList<ItemStack> harpShopRef = player.getReference("harpShop");
        return harpShopRef.get(index);
    }
}
