package com.Game.Items.RawResource;

import com.Game.Inventory.Item;

public class Feather extends Item {
    public Feather(int id, String name, String examineText, int worth, boolean stackable) {
        super(id, name, examineText, worth, stackable);

        setImage("feather.png");
    }
}
