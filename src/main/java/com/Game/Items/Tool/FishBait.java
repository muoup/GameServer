package com.Game.Items.Tool;

import com.Game.Inventory.Item;

public class FishBait extends Item {
    public FishBait(int id, String name, String examineText, int worth, boolean stackable) {
        super(id, name, examineText, worth, stackable);

        setImage("fish_bait.png");
    }
}
