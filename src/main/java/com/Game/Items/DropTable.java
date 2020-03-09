package com.Game.Items;

import com.Game.Inventory.ItemList;
import com.Game.Inventory.ItemStack;
import com.Game.Util.Math.DeltaMath;

import java.util.ArrayList;

public class DropTable {
    public ArrayList<ItemStack> stacks;
    public ArrayList<Double> chance;

    public DropTable() {
        stacks = new ArrayList<ItemStack>();
        chance = new ArrayList<Double>();
    }

    public void add(ItemList item, int amount, double chance) {
        stacks.add(new ItemStack(item, amount, 0));
        this.chance.add(chance);
    }

    public ArrayList<ItemStack> determineOutput() {
        ArrayList<ItemStack> drops = new ArrayList<ItemStack>();

        for (int i = 0; i < stacks.size(); i++) {
            ItemStack stack = stacks.get(i);
            double percent = chance.get(i);

            if (DeltaMath.range(0, 1) <= percent) {
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
