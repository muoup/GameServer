package com.Game.Inventory;

import com.Game.ConnectionHandling.Client;
import com.Game.ConnectionHandling.Save.SaveSettings;
import com.Game.Entity.Player.Player;

public class InventoryManager {
    public ItemStack[] inventory = new ItemStack[20];
    private Player player;

    public InventoryManager(ItemStack[] itemsSaved) {
        if (itemsSaved.length != SaveSettings.inventoryAmount)
            System.err.println("an inventory error has occured, the length is not " + SaveSettings.inventoryAmount);

        inventory = itemsSaved;
    }

    public InventoryManager(Player player) {
        this.player = player;

        for (int i = 0; i < inventory.length; i++) {
            inventory[i] = ItemStack.empty();
        }
    }


    public boolean isFull() {
        for (ItemStack i : inventory) {
            if (i.getItem().id == 0)
                return false;
        }

        return true;
    }

    public void handleInventory() {
        for (int i = 0; i < inventory.length; i++) {
            if (inventory[i].getAmount() <= 0 && inventory[i].getID() != -1) {
                inventory[i] = Item.emptyStack();
            }
        }
    }

    public void update() {

    }

    public void removeItem(int index, int amount) {
        setAmount(index, inventory[index].getAmount() - amount);
    }

    public int itemCount(ItemList item) {
        int amount = 0;
        for (ItemStack stack : inventory) {
            if (stack.getID() == item.getID())
                amount += stack.getAmount();
        }
        return amount;
    }

    public int itemCount(ItemStack stack) {
        return itemCount(stack.getItemList(), stack.getData());
    }

    public int itemCount(ItemList item, int data) {
        int amount = 0;
        for (ItemStack stack : inventory) {
            if (stack.getID() == item.getID() && stack.getData() == data)
                amount += stack.getAmount();
        }
        return amount;
    }

    public int itemCount(ItemSets set) {
        int amount = 0;

        for (int id : set.items) {
            amount += itemCount(ItemList.values()[id]);
        }

        return amount;
    }

    public int indexOf(ItemList item) {
        for (int i = 0; i < inventory.length; i++) {
            ItemStack stack = inventory[i];
            if (stack.getID() == item.getID())
                return i;
        }

        return -1;
    }

    public int indexOf(ItemStack stack) {
        for (int i = 0; i < inventory.length; i++) {
            if (inventory[i].equals(stack))
                return i;
        }

        return -1;
    }

    public ItemStack findStack(ItemList item) {
        for (ItemStack stack : inventory) {
            if (stack.getID() == item.getID())
                return stack;
        }

        return null;
    }

    public ItemStack findStack(ItemList item, int data) {
        for (ItemStack stack : inventory) {
            if (stack.getID() == item.getID() && stack.getData() == data)
                return stack;
        }

        return null;
    }

    public int getIndex(ItemStack stack) {
        for (int i = 0; i < inventory.length; i++) {
            if (stack.compare(inventory[i])) {
                return i;
            }
        }

        return -1;
    }

    public int getIndexIgnoreStack(ItemStack stack) {
        for (int i = 0; i < inventory.length; i++) {
            if (stack.compareIgnoreStack(inventory[i])) {
                return i;
            }
        }

        return -1;
    }

    public int getAmount(ItemList list) {
        int amount = 0;
        for (ItemStack s : inventory) {
            if (s.getID() == list.getID())
                amount += s.getAmount();
        }
        return amount;
    }

    public int getAmount(ItemList list, int data) {
        int amount = 0;
        for (ItemStack s : inventory) {
            if (s.getID() == list.getID() && s.getData() == data)
                amount += s.getAmount();
        }

        return amount;
    }

    public int removeItem(ItemList item, int amount, int data) {
        int remove = amount;
        for (int i = 0; i < inventory.length; i++) {
            ItemStack stack = inventory[i];
            if (stack.getID() == item.getID() && stack.getData() == data) {
                int removeAmount = (remove > stack.getAmount()) ? stack.getAmount() : remove;
                removeItem(i, removeAmount);
                remove -= removeAmount;
                if (remove == 0)
                    return 0;
            }
        }

        return amount;
    }

    public void removeItem(ItemSets item, int amount) {
        int removeRemaining = amount;

        for (int id : item.items) {
            removeRemaining = removeItem(ItemList.values()[id], removeRemaining);
        }
    }

    public int removeItem(ItemList item, int amount) {
        return removeItem(item, amount, 0);
    }

    public int removeItem(ItemStack stack) {
        return removeItem(stack.getItemList(), stack.amount, stack.data);
    }

    public void setAmount(int slot, int amount) {
        ItemStack stack = getStack(slot);
        stack.amount = amount;
        setItem(slot, stack);
    }

    public void addAmount(int slot, int amount) {
        setAmount(slot, amount + getStack(slot).getAmount());
    }

    public int useIndex = -1;

    public void setItem(int slot, ItemStack item) {
        inventory[slot] = item;

        if (item.getAmount() <= 0) {
            inventory[slot] = new ItemStack(ItemList.empty, 0);
        }

        Client.sendInventorySlot(player, slot, inventory[slot]);
    }

    public void swapSlots(int slot1, int slot2) {
        ItemStack temp = getStack(slot1).clone();
        setItem(slot1, getStack(slot2).clone());
        setItem(slot2, temp);
    }

    public void clientSetItem(int slot, int id, int amount, int data) {
        inventory[slot] = new ItemStack(ItemList.values()[id], amount, data);
    }

    public int addItem(ItemList item, int amount, int data) {
        return addItem(new ItemStack(item, amount, data));
    }

    public int addItem(Item item, int amount, int data) {
        return addItem(ItemList.values()[item.id], amount, data);
    }

    public int addItem(ItemList item, int amount) {
        return addItem(item, amount, 0);
    }

    public int addItem(Item item, int amount) {
        return addItem(ItemList.values()[item.id], amount);
    }

    public int addItem(ItemStack itemStack) {
        // Constants
        int amount = itemStack.amount;
        int data = itemStack.data;

        // Loop Dynamic Variables
        int amt = itemStack.amount;
        int add;

        for (int i = 0; i < inventory.length; i++) {
            add = amount;
            ItemStack stack = inventory[i];
            if (stack.compare(itemStack)) {
                if (add > stack.maxStack() - stack.getAmount()) {
                    add = stack.maxStack() - stack.getAmount();
                }

                addAmount(i, add);

                amount -= add;

                if (amount == 0)
                    return amt;
            }
        }

        if (amount == 0)
            return amt;

        for (int i = 0; i < inventory.length; i++) {
            add = amount;

            if (inventory[i].getID() == 0) {
                if (add > itemStack.maxStack()) {
                    add = itemStack.maxStack();
                }

                setItem(i, new ItemStack(itemStack.getItem(), add, data, itemStack.isStacked));

                amount -= add;
            }

            if (amount == 0)
                return amt;
        }

        return amt - amount;
    }

    public ItemStack getStack(int inventoryIndex) {
        return inventory[inventoryIndex];
    }

    public Item getItem(int inventoryIndex) {
        return inventory[inventoryIndex].getItem();
    }

    public void setData(int slot, int data) {
        ItemStack oldStack = inventory[slot];
        setItem(slot, new ItemStack(ItemList.values()[oldStack.getID()], oldStack.getAmount(), data));
    }

    public int getData(int index) {
        return inventory[index].getData();
    }

    public int getIndexFirst(ItemList item1, int data1, ItemList item2, int data2) {
        for (int i = 0; i < inventory.length; i++) {
            ItemStack stack = inventory[i];
            if (stack.getID() == item1.getID() && stack.getData() == data1 ||
                    stack.getID() == item2.getID() && stack.getData() == data2)
                return i;
        }

        return -1;
    }

    public int emptySpace() {
        int amount = 0;

        for (ItemStack item : inventory) {
            if (item.getID() == 0)
                amount++;
        }

        return amount;
    }

    public void setItemAmount(int slot, int amount) {
        ItemStack stack = getStack(slot).clone();

        stack.amount = amount;

        setItem(slot, stack);
    }

    public void changeItemAmount(int slot, int amount) {
        setItemAmount(slot, getStack(slot).amount + amount);
    }
}
