package com.Game.Items.RawResource;

import com.Game.Entity.Player.Player;
import com.Game.Inventory.Item;
import com.Game.Inventory.ItemList;
import com.Game.Inventory.ItemStack;
import com.Game.PseudoData.ImageIdentifier;
import com.Game.Skills.Skills;
import com.Game.Util.Other.RCOption;

public class StringItem extends Item {
    public StringItem(int id, String name, String examineText, int worth, boolean stackable) {
        super(id, name, examineText, worth, stackable);
        setImage("string.png");
    }

    public void craftString(Player player, int index) {
        craftActionLoop(player, 750, 20, Skills.FLETCHING,
                15f, 1, new ItemStack(ItemList.bowString, 1),
                new ItemStack(ItemList.stringItem, 3));
    }

    public void dataItemChange(ItemStack stack) {
        stack.setOptions(new RCOption("Craft Bow String", this::craftString));
    }
}