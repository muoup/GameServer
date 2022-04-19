package com.Game.Items.RawResource;

import com.Game.Entity.Player.Player;
import com.Game.Inventory.Item;
import com.Game.Inventory.ItemList;
import com.Game.Inventory.ItemStack;
import com.Game.PseudoData.ImageIdentifier;
import com.Game.Util.Other.RCOption;

public class StringItem extends Item {
    public StringItem(int id, String name, String examineText, int worth, boolean stackable) {
        super(id, name, examineText, worth, stackable);
        setImage("string.png");
    }

    public void craftString(Player player, int index) {
        convert(player, 3, 1, ItemList.bowString);
    }

    public void dataItemChange(ItemStack stack) {
        stack.setOptions(new RCOption("Craft Bow String", this::craftString));
    }
}