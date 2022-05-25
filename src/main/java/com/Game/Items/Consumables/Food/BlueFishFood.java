package com.Game.Items.Consumables.Food;

import com.Game.Util.Other.SpriteSheet;

public class BlueFishFood extends Food {
    public BlueFishFood(int id, String name, String examineText, int worth, boolean stackable) {
        super(id, name, examineText, worth, stackable, 250);

        setImage(SpriteSheet.fishSheet.getCell(1, 0));
    }
}
