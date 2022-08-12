package com.Game.Items.Consumables.Loot;

import com.Game.Inventory.Item;
import com.Game.Inventory.ItemStack;
import com.Game.Util.Other.SpriteSheet;

public class GradedFish extends Item {
    public GradedFish(int id, String name, String examineText, int worth, boolean stackable) {
        super(id, name, examineText, worth, stackable);

        setImage(SpriteSheet.fishSheet.getCell(3, 0));
    }

    public void dataItemChange(ItemStack stack) {
        setImage(SpriteSheet.fishSheet.getCell(3, (int) stack.getData()));

        stack.setWorth(worth * ((int) stack.getData() + 1));
    }
}
