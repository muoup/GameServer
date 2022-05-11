package com.Game.Items.RawResource;

import com.Game.Inventory.Item;
import com.Game.Inventory.ItemStack;
import com.Game.PseudoData.ImageIdentifier;
import com.Game.Util.Other.SpriteSheet;

public class Ore extends Item {
    private int imageColumn;

    public Ore(int id, int imageColumn, String name, String examineText, int worth, boolean stackable) {
        super(id, name, examineText, worth, stackable);
        this.imageColumn = imageColumn;

        setImage(SpriteSheet.oreSheet.getCell(imageColumn, 0));
    }

    public void dataItemChange(ItemStack stack) {
        if (stack.data == 0) {
            stack.image = image;
            stack.name = name;
        } else {
            stack.image = SpriteSheet.oreSheet.getCell(imageColumn, 1);
            stack.name = ("" + name).replace("Ore", "Bar");
        }
    }
}
