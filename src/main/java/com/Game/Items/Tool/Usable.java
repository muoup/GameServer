package com.Game.Items.Tool;


import com.Game.Entity.Player.Player;
import com.Game.Inventory.Item;
import com.Game.Inventory.ItemStack;
import com.Game.Util.Other.RCOption;

public class Usable extends Item {
    public Usable(int id, String name, String examineText, int worth, boolean stackable) {
        super(id, name, examineText, worth, stackable);
    }

    public void OnClick(Player player, int index) {

    }

    public void dataItemChange(ItemStack stack) {
        stack.setOptions(new RCOption("Use", (player, index) -> {}));
    }
}
