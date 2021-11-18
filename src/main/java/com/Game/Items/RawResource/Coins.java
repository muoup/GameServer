package com.Game.Items.RawResource;

import com.Game.Inventory.Item;

public class Coins extends Item {
    public Coins(int id, String name, String examineText, int worth, boolean stackable) {
        super(id, name, examineText, worth, stackable);

        setImage("gold_coin.png");
    }
}
