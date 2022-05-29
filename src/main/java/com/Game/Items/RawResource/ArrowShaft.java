package com.Game.Items.RawResource;

import com.Game.Inventory.Item;
import com.Game.Inventory.ItemList;
import com.Game.Inventory.ItemStack;
import com.Game.Entity.Player.Player;
import com.Game.CustomInterfaces.Loops.CraftActionLoop;
import com.Game.Skills.Skills;
import com.Game.Util.Other.RCOption;

public class ArrowShaft extends Item {
    public ArrowShaft(int id, String name, String examineText, int worth, boolean stackable) {
        super(id, name, examineText, worth, stackable);

        setImage("arrow_shaft.png");
    }

    public void combine(Player player, int index) {
        craftActionLoop(player, 1000, 20, Skills.FLETCHING,
                4f, 10, new ItemStack(ItemList.arrow, 1),
                new ItemStack(ItemList.feather, 1), new ItemStack(ItemList.arrowShaft, 1));
    }

    public void dataItemChange(ItemStack stack) {
        stack.setOptions(new RCOption("Combine", this::combine));
    }
}
