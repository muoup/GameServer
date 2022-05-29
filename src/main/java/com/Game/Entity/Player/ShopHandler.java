package com.Game.Entity.Player;

import com.Game.Entity.NPC.Shop;
import com.Game.Inventory.ItemList;
import com.Game.Inventory.ItemStack;
import com.Game.Util.Other.Settings;

public class ShopHandler {

    public Shop selectedShop = Shop.empty;
    private Player player;

    public ShopHandler(Player player) {
        this.player = player;
    }

    public void handlePacket(String[] message) {
        switch (message[1]) {
            case "shop":
                if (message[3].equals("-1")) {
                    selectedShop.examineShop(this, Integer.parseInt(message[2]));
                    return;
                }

                selectedShop.shopInteraction(this, Integer.parseInt(message[2]), Integer.parseInt(message[3]));
                break;
            case "inventory":
                if (message[3].equals("-1")) {
                    selectedShop.examineInventory(this, Integer.parseInt(message[2]));
                    return;
                }

                selectedShop.inventoryInteraction(this, Integer.parseInt(message[2]), Integer.parseInt(message[3]));
                break;
        }
    }

    public void buyOption(int index, int amount) {
        buyOption(index, amount, 1);
    }

    public void sellInventory(int index, int amount) {
        sellInventory(index, amount, 1);
    }

    public void buyOption(int index, int amount, float priceModifier) {
        if (index > selectedShop.offeredItems.length - 1) {
            System.err.println("UH OH, shoppy did an oopsie");
            return;
        }

        ItemStack selected = selectedShop.offeredItems[index].clone();

        int indPrice = selected.getWorth();
        int price = indPrice * amount;

        int gold = player.inventory.itemCount(ItemList.gold);

        if (gold < price) {
            amount = gold / indPrice;
            price = amount * indPrice;
        }

        if (amount == 0) {
            player.sendMessage("You are out of money!");
            return;
        }

        int maxSpace = player.inventory.emptySpace() * selected.maxStack();

        for (ItemStack item : player.inventory.inventory) {
            if (item.compare(selected)) {
                maxSpace += item.maxStack() - item.getAmount();
            }
        }

        selected.amount = Math.min(amount, maxSpace);

        player.removeItem(ItemList.gold, 0, (int) (price * priceModifier));
        player.addItem(selected);
    }

    public void sellInventory(int index, int amount, float priceModifier) {
        ItemStack selected = player.inventory.getStack(index);

        if (selected.getWorth() <= 0) {
            player.sendMessage("This item cannot be sold!");
        }

        int amt = player.inventory.getAmount(selected.getItemList(), selected.getData());

        if (amount == -1 || amt < amount) {
            amount = amt;
        }

        if (amount == 0) {
            player.sendMessage("You do not have any of this item!");
            return;
        }

        player.removeItem(selected.getItemList(), selected.getData(), amount);
        player.addItem(ItemList.gold, (int) (selected.getSingleValue() * priceModifier * amount));
    }

    public Player getPlayer() {
        return player;
    }
}
