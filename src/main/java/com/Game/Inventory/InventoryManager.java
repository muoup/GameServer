package com.Game.Inventory;

import com.Game.ConnectionHandling.Save.SaveSettings;
import com.Game.Util.Math.Vector2;
import com.Game.Util.Other.Render;
import com.sun.tools.javac.Main;

import java.awt.*;

public class InventoryManager {
    private ItemStack[] inventory;

    public InventoryManager(ItemStack[] itemsSaved) {
        if (itemsSaved.length != SaveSettings.inventoryAmount)
            System.err.println("an inventory error has occured, the length is not " + SaveSettings.inventoryAmount);

        inventory = itemsSaved;
    }

    public InventoryManager() {
        inventory = new ItemStack[20];
    }


    public static boolean isFull() {
        for (ItemStack i : inventory) {
            if (i.getItem().id == 0)
                return false;
        }

        return true;
    }

    public static void handleInventory() {
        for (int i = 0; i < inventory.length; i++) {
            if (inventory[i].getAmount() <= 0 && inventory[i].getID() != -1) {
                inventory[i] = Item.emptyStack();
            }
        }
    }

    public static void update() {
        if (RightClick.coolDown > 0)
            RightClick.coolDown -= Main.dTime();

        if (RightClick.render)
            useIndex = -1;

        InventoryDrag.update();
    }

    public static void removeItem(int index, int amount) {
        setAmount(index, inventory[index].getAmount() - amount);
    }

    public static int itemCount(ItemList item) {
        int amount = 0;
        for (ItemStack stack : inventory) {
            if (stack.getID() == item.getID())
                amount += stack.getAmount();
        }
        return amount;
    }

    public static int itemCount(ItemList item, int data) {
        int amount = 0;
        for (ItemStack stack : inventory) {
            if (stack.getID() == item.getID() && stack.getData() == data)
                amount += stack.getAmount();
        }
        return amount;
    }

    public static int itemCount(ItemSets set) {
        int amount = 0;

        for (int id : set.items) {
            amount += itemCount(ItemList.values()[id]);
        }

        return amount;
    }

    public static int indexOf(ItemList item) {
        for (int i = 0; i < inventory.length; i++) {
            ItemStack stack = inventory[i];
            if (stack.getID() == item.getID())
                return i;
        }

        return -1;
    }

    public static ItemStack findStack(ItemList item) {
        for (ItemStack stack : inventory) {
            if (stack.getID() == item.getID())
                return stack;
        }

        return null;
    }

    public static ItemStack findStack(ItemList item, int data) {
        for (ItemStack stack : inventory) {
            if (stack.getID() == item.getID() && stack.getData() == data)
                return stack;
        }

        return null;
    }

    public static int getIndex(ItemStack stack) {
        for (int i = 0; i < inventory.length; i++) {
            if (stack.getID() == inventory[i].getID() &&
                    stack.getAmount() == inventory[i].getAmount() &&
                    stack.getData() == inventory[i].getData()) {
                return i;
            }
        }

        return -1;
    }

    public static int getAmount(ItemList list) {
        int amount = 0;
        for (ItemStack s : inventory) {
            if (s.getID() == list.getID())
                amount += s.getAmount();
        }
        return amount;
    }

    public static int getAmount(ItemList list, int data) {
        int amount = 0;
        for (ItemStack s : inventory) {
            if (s.getID() == list.getID() && s.getData() == data)
                amount += s.getAmount();
        }

        return amount;
    }

    public static int removeItem(ItemList item, int amount, int data) {
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

    public static void removeItem(ItemSets item, int amount) {
        int removeRemaining = amount;

        for (int id : item.items) {
            removeRemaining = removeItem(ItemList.values()[id], removeRemaining);
        }
    }

    public static int removeItem(ItemList item, int amount) {
        return removeItem(item, amount, 0);
    }

    public static int removeItem(ItemStack stack) {
        return removeItem(stack.getItemList(), stack.amount, stack.data);
    }

    public static void render() {
        Render.setColor(new Color(255, 138, 4));

        Render.drawBounds(GUI.GuiPos, GUI.GUIEnd());

        Render.setColor(Color.BLACK);

        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 5; y++) {

                Vector2 rectPos = GUI.getGridPosition(x, y);

                if (draggedIndex == x + y * 4) {
                    Render.drawRectOutline(rectPos, GUI.invSize);
                    continue;
                }

                ItemStack stack = getStack(x + y * 4);
                GUI.drawItem(x, y, stack);
            }
        }

        if (useIndex != -1) {
            Render.setColor(Color.GREEN);
            Render.drawRectOutline(GUI.getGridPosition(useIndex % 4, useIndex / 4), GUI.invSize);
        }

        InventoryDrag.render();
    }

    public static void setAmount(int slot, int amount) {
        ItemStack stack = getStack(slot);
        stack.amount = amount;
        setItem(slot, stack);
    }

    public static void addAmount(int slot, int amount) {
        setAmount(slot, amount + getStack(slot).getAmount());
    }

    public static int useIndex = -1;

    public static void setItem(int slot, ItemStack item) {
        inventory[slot] = item;

        if (inventory[slot].amount <= 0) {
            inventory[slot] = new ItemStack(ItemList.empty, 0);
        }

        Main.sendPacket("08" + slot + ":" + item.getID() + ":" + item.getAmount() + ":" + item.getData() + ":" + Main.player.name);
    }

    public static void swapSlots(int slot1, int slot2) {
        ItemStack temp = getStack(slot1).clone();
        setItem(slot1, getStack(slot2).clone());
        setItem(slot2, temp);
    }

    public static void clientSetItem(int slot, int id, int amount, int data) {
        inventory[slot] = new ItemStack(ItemList.values()[id], amount, data);
    }

    public static int addItem(ItemList item, int amount, int data) {
        int amt = amount;
        int add = amount;

        for (int i = 0; i < inventory.length; i++) {
            add = amount;

            if (inventory[i].getID() == item.item.id
                    && inventory[i].getData() == data
                    && inventory[i].getAmount() < item.maxStack()) {
                if (add > item.maxStack() - inventory[i].getAmount()) {
                    add = item.maxStack() - inventory[i].getAmount();
                }

                addAmount(i, add);

                amount -= add;
            }
        }

        if (amount == 0)
            return amt;

        for (int i = 0; i < inventory.length; i++) {
            add = amount;

            if (inventory[i].getID() == 0) {
                if (add > item.maxStack()) {
                    add = item.maxStack();
                }

                setItem(i, new ItemStack(item, add, data));

                amount -= add;
            }

            if (amount == 0)
                return amt;
        }

        return amt - add;
    }

    public static int addItem(Item item, int amount, int data) {
        return addItem(ItemList.values()[item.id], amount, data);
    }

    public static int addItem(ItemList item, int amount) {
        return addItem(item, amount, 0);
    }

    public static int addItem(Item item, int amount) {
        return addItem(ItemList.values()[item.id], amount);
    }

    public static int addItem(ItemStack stack) {
        return addItem(stack.getItem(), stack.getAmount());
    }

    public static ItemStack getStack(int inventoryIndex) {
        return inventory[inventoryIndex];
    }

    public static Item getItem(int inventoryIndex) {
        return inventory[inventoryIndex].getItem();
    }

    public static void setData(int slot, int data) {
        ItemStack oldStack = inventory[slot];
        setItem(slot, new ItemStack(ItemList.values()[oldStack.getID()], oldStack.getAmount(), data));
    }

    public static int getData(int index) {
        return inventory[index].getData();
    }

    public static int getIndexFirst(ItemList item1, int data1, ItemList item2, int data2) {
        for (int i = 0; i < inventory.length; i++) {
            ItemStack stack = inventory[i];
            if (stack.getID() == item1.getID() && stack.getData() == data1 ||
                    stack.getID() == item2.getID() && stack.getData() == data2)
                return i;
        }

        return -1;
    }

    public static int emptySpace() {
        int amount = 0;

        for (ItemStack item : inventory) {
            if (item.getID() == 0)
                amount++;
        }

        return amount;
    }
}
