package com.Game.Items.RawResource;

import com.Game.Inventory.Item;
import com.Game.Inventory.ItemList;
import com.Game.Inventory.ItemStack;
import com.Game.Entity.Player.Player;
import com.Game.PseudoData.ImageIdentifier;
import com.Game.Skills.Skills;
import com.Game.Util.Other.RCOption;

public class ArrowShaft extends Item {
    public ArrowShaft(int id, String name, String examineText, int worth, boolean stackable) {
        super(id, name, examineText, worth, stackable);

        setImage("arrow_shaft.png");
    }

    public void combine(Player player, int index) {
        int c_amt = combine(player, index, ItemList.feather, ItemList.arrow, 15);
        player.skills.addExperience(Skills.FLETCHING, 1.5f * c_amt);
    }

    public void dataItemChange(ItemStack stack) {
        stack.setOptions(new RCOption("Combine", this::combine));
    }
}
