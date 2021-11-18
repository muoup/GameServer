package com.Game.Items.Consumables.Loot;

import com.Game.Inventory.ItemList;

public class BirdNest extends LootGenerator {
    public BirdNest(int id, String name, String examineText, int worth, boolean stackable) {
        super(id, name, examineText, worth, stackable);

        setImage("bird_nest.png");

        // Crescendo of gold amounts. Probably a little too generous.
        table.add(ItemList.gold, 125, .75f);
        table.add(ItemList.gold, 500, .50f);
        table.add(ItemList.gold, 750, .25f);
        table.add(ItemList.gold, 2500, .5);

        // I don't know if this is at all balanced but it seems like a decent idea.
        table.add(ItemList.crystalBow, 1, .0025);
    }
}
