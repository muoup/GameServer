package com.Game.Items.Consumables.Food;

public class Seaweed extends Food {
    public Seaweed(int id, String name, String examineText, int worth, boolean stackable) {
        super(id, name, examineText, worth, stackable, 75);

        setImage("sea_weed.png");
    }
}
