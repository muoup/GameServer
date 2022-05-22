package com.Game.Entity.Player;

import com.Game.ConnectionHandling.Client;
import com.Game.ConnectionHandling.Save.SaveSettings;
import com.Game.Inventory.ItemStack;

import java.util.ArrayList;

public class BankingHandler {
    private Player player;
    public ArrayList<ItemStack> items;
    private boolean open;

    public BankingHandler(Player player) {
        this.player = player;
        this.items = new ArrayList();
        this.open = false;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public boolean isOpen() {
        return open;
    }

    public void addBankItem(ItemStack itemStack) {
        itemStack.setStacked(false);

        Client.sendBankChange(player, "add", itemStack.getServerPacket());

        items.add(itemStack);
    }

    public void changeBankItem(int index, int amount) {
        Client.sendBankChange(player, "change", index, amount);

        items.get(index).amount += amount;

        if (items.get(index).amount == 0) {
            removeBankItem(index);
        }
    }

    public void removeBankItem(int index) {
        Client.sendBankChange(player, "remove", index);

        items.remove(index);
    }

    public void setBankItem(int index, ItemStack newStack) {
        Client.sendBankChange(player, "replace", index + "=>" + newStack.getServerPacket());

        items.set(index, newStack);
    }

    public void withdrawItem(int index, int amount, boolean inStack) {
        if (index > items.size() - 1)
            return;

        ItemStack item = items.get(index).clone();

        if (amount < 0 || amount > item.getAmount()) {
            amount = item.getAmount();
        }

        item.amount = amount;
        item.setStacked(inStack);

        int realAmount = player.addItem(item);

        changeBankItem(index, -realAmount);
    }

    public void depositFromInventory(int index, int amount) {
        if (index > SaveSettings.inventoryAmount - 1)
            return;

        ItemStack item = player.inventory.getStack(index).clone();

        if (item.amount <= 0 || item.getID() <= 0)
            return;

        int count = player.inventory.itemCount(item.getItemList(), item.getData());

        if (amount == -1 || amount > count) {
            amount = count;
        }

        item.amount = amount;

        for (int i = 0; i < items.size(); i++) {
            ItemStack stack = items.get(i);
            if (stack.compareIgnoreStack(item)) {
                changeBankItem(i, amount);
                player.removeItem(index, amount);
                return;
            }
        }

        addBankItem(item);
        player.removeItem(index, amount);
    }

    public ArrayList<ItemStack> getItems() {
        return items;
    }

    public ItemStack getStack(int index) {
        return items.get(index);
    }

}
