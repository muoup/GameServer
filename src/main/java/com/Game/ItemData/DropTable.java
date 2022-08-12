package com.Game.ItemData;

import com.Game.Inventory.ItemList;
import com.Game.Inventory.ItemStack;
import com.Game.Util.Math.DeltaMath;

import java.util.ArrayList;

public class DropTable {
    public ArrayList<ItemStack> stacks;
    public ArrayList<Double> chance;
    public ArrayList<Integer> range;

    public DropTable() {
        stacks = new ArrayList();
        chance = new ArrayList();
        range = new ArrayList();
    }

    public void addItem(ItemList item, int amount, double chance) {
        stacks.add(new ItemStack(item, amount, 0));
        this.chance.add(chance);
        this.range.add(0);
    }

    public void addItem(ItemList item, int amount, double chance, boolean isStacked) {
        stacks.add(new ItemStack(item, amount, 0, isStacked));
        this.chance.add(chance);
        this.range.add(0);
    }

    public void addItem(ItemList item, int min, int max, double chance) {
        stacks.add(new ItemStack(item, min, 0));
        this.chance.add(chance);
        this.range.add(max);
    }

    public void addItem(ItemList item, int min, int max, double chance, boolean isStacked) {
        stacks.add(new ItemStack(item, min, 0, isStacked));
        this.chance.add(chance);
        this.range.add(max);
    }

    public ArrayList<ItemStack> determineOutput() {
        ArrayList<ItemStack> drops = new ArrayList<ItemStack>();

        addItems:
        for (int i = 0; i < stacks.size(); i++) {
            ItemStack stack = stacks.get(i).clone();
            double percent = chance.get(i);

            stack.amount += (int) DeltaMath.range(0, range.get(i));

            if (DeltaMath.range(0, 1) <= percent) {
                for (ItemStack drop : drops) {
                    if (stack.compare(drop)) {
                        drop.amount += stack.amount;
                        continue addItems;
                    }
                }

                drops.add(stack);
            }
        }

        return drops;
    }

    public void wipe() {
        stacks.clear();
        chance.clear();
    }
}
