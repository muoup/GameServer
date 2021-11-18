package com.Game.Items.RawResource;

import com.Game.Entity.Player.Player;
import com.Game.Inventory.Item;
import com.Game.Inventory.ItemList;
import com.Game.Inventory.ItemStack;
import com.Game.PseudoData.ImageIdentifier;

public class StringItem extends Item {
    public StringItem(int id, String name, String examineText, int worth) {
        super(id, name, examineText, worth, true);
        setImage("string.png");
    }

    public void OnClick(Player player, int index) {
        convert(player, 3, 1, ItemList.bowString);
    }

    public void setData(ItemStack stack) {
        stack.options.add("Craft Bow String");
    }

    public String getOptionText(int i, int data, ItemStack stack) {
        return "Craft Bow String";
    }
}