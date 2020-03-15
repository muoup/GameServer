package com.Game.Items.RawResource;

import com.Game.Items.Item;
import com.Game.Items.ItemList;
import com.Game.Items.ItemStack;

public class StringItem extends Item {
    public StringItem(int id, String imageName, String name, String examineText, int maxStack, int worth) {
        super(id, imageName, name, examineText, maxStack, worth);
    }

    public void OnClick(int index) {
        convert(3, 1, ItemList.bowString);
    }

    public void setData(ItemStack stack) {
        stack.options.add("Craft Bow String");
    }

    public String getOptionText(int i, int data, ItemStack stack) {
        return "Craft Bow String";
    }
}