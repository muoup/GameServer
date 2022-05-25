package com.Game.Items.Consumables.Food;

import com.Game.Util.Other.SpriteSheet;

public class Seaweed extends Food {
    public Seaweed(int id, String name, String examineText, int worth, boolean stackable) {
        super(id, name, examineText, worth, stackable, 400);

        setImage(SpriteSheet.fishSheet.getCell(2, 0));
    }
}
