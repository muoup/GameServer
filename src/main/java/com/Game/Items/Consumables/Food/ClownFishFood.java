package com.Game.Items.Consumables.Food;

import com.Game.Util.Other.SpriteSheet;

public class ClownFishFood extends Food {

    public ClownFishFood(int id, String name, String examineText, int worth, boolean stackable) {
        super(id, name, examineText, worth, stackable, 150);

        setImage(SpriteSheet.fishSheet.getCell(0, 0));
    }
}
