package com.Game.Items.Consumables.Food;

public class ClownFishFood extends Food {

    public ClownFishFood(int id, String name, String examineText, int worth, boolean stackable) {
        super(id, name, examineText, worth, stackable, 25);

        setImage("clownfish.png");
    }
}
