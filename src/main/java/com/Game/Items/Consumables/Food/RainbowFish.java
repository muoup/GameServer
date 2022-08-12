package com.Game.Items.Consumables.Food;

import com.Game.Util.Other.SpriteSheet;

public class RainbowFish extends Food {
    public RainbowFish(int id, String name, String examineText, int worth, boolean stackable) {
        super(id, name, examineText, worth, stackable, 650);

        setImage(SpriteSheet.fishSheet.getCell(4, 0));
    }
}
