package com.Game.Entity.NPC;

import com.Game.ConnectionHandling.Client;
import com.Game.Entity.Player.Player;
import com.Game.Inventory.ItemList;
import com.Game.WorldManagement.World;

public class RelicCollector extends NPC {
    public RelicCollector(World world, int x, int y) {
        super(world, x, y);

        setImage("relic_collector.png");
    }

    public void onInteract(Player player) {
        if (player.inventory.itemCount(ItemList.bugRelic) > 0) {
            player.inventory.removeItem(ItemList.bugRelic, 1);
            player.inventory.addItem(ItemList.gold, 1000);
            Client.sendText(player, "This is exactly what I needed! Thank you very much!");
        } else {
            Client.sendText(player, "Hey, if you see any relics in that cave over there, can you bring them back to me?" +
                    " I'll give you some gold for your efforts.");
        }
    }
}
