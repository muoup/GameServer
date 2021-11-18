package com.Game.Items.RawResource;

import com.Game.Inventory.Item;

public class BowString extends Item {
    public BowString(int id, String name, String examineText, int worth, boolean stackable) {
        super(id, name, examineText, worth, stackable);

        setImage("bow_string.png");
    }
}
