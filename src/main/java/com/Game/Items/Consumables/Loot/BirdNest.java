package com.Game.Items.Consumables.Loot;

import com.Game.Entity.Player.Player;
import com.Game.Inventory.ItemList;
import com.Game.Inventory.ItemStack;

import java.util.ArrayList;

public class BirdNest extends LootGenerator {
    public BirdNest(int id, String name, String examineText, int worth, boolean stackable) {
        super(id, name, examineText, worth, stackable);

        setImage("bird_nest.png");

        // Crescendo of gold amounts. Probably a little too generous.
        table.addItem(ItemList.gold, 125, .75f);
        table.addItem(ItemList.gold, 500, .50f);
        table.addItem(ItemList.gold, 750, .25f);
        table.addItem(ItemList.gold, 2500, .05);

        // I don't know if this is at all balanced but it seems like a decent idea.
        table.addItem(ItemList.crystalBow, 1, .025);
    }

    public void OnClick(Player player, int index) {
        if (player.inventory.emptySpace() < 2) {
            player.sendMessage("You don't have enough inventory space to open this.");
        }

        table.determineOutput().forEach(player::addItem);
        player.removeItem(index, 1);
    }
}
