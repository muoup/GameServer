package com.Game.Items.RawResource;

import com.Game.GUI.Skills.Skills;
import com.Game.Items.Item;
import com.Game.Items.ItemList;
import com.Game.Items.ItemStack;

public class ArrowShaft extends Item {
    public ArrowShaft(int id, String imageName, String name, String examineText, int maxStack, int worth) {
        super(id, imageName, name, examineText, maxStack, worth);
    }

    public void OnClick(int index) {
        int c_amt = combine(index, ItemList.feather, ItemList.arrow, 15);
        Skills.addExperience(Skills.FLETCHING, 1.5f * c_amt);
    }

    public void setData(ItemStack stack) {
        stack.options.add("Combine");
    }
}
