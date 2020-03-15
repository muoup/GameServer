package com.Game.Items.RawResource;

import com.Game.Items.Item;
import com.Game.Items.ItemStack;
import com.Util.Other.SpriteSheet;

public class Ore extends Item {
    private int imageColumn;

    public Ore(int id, int imageColumn, String name, String examineText, int maxStack, int worth) {
        super(id, SpriteSheet.oreSheet.getCell(imageColumn, 0), name, examineText, maxStack, worth);
        this.imageColumn = imageColumn;
    }

    public void setData(ItemStack stack) {
        if (stack.data == 0) {
            stack.image = image;
        } else {
            stack.image = SpriteSheet.oreSheet.getCell((imageColumn == 2) ? 1 : imageColumn, 1);
        }
    }
}
