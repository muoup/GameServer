package com.Game.Items.RawResource;

import com.Game.Inventory.Item;
import com.Game.Inventory.ItemStack;
import com.Game.PseudoData.ImageIdentifier;
import com.Game.Util.Other.SpriteSheet;

public class Ore extends Item {
    private int imageColumn;

    public Ore(int id, int imageColumn, String name, String examineText, int worth, boolean stackable) {
        super(id, name, examineText, worth, false);
        this.imageColumn = imageColumn;

        setImage(ImageIdentifier.subImage("Items/ore_sheet.png", imageColumn, 0, 48, 48));
    }

    public void setData(ItemStack stack) {
        if (stack.data == 0) {
            stack.image = image;
        } else {
            stack.image = SpriteSheet.oreSheet.getCell((imageColumn == 2) ? 1 : imageColumn, 1);
        }
    }
}
