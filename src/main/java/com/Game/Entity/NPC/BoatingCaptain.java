package com.Game.Entity.NPC;

import com.Game.ConnectionHandling.Client;
import com.Game.Entity.Player.Player;
import com.Game.Inventory.ItemList;
import com.Game.Inventory.ItemSets;
import com.Game.WorldManagement.World;

public class BoatingCaptain extends NPC {
    public BoatingCaptain(int id, World world, int x, int y) {
        super(id, world, x, y);

        setImage("captain.png");
    }

    public void onInteract(Player player) {
        int temp;

        switch (player.getQuestData(1)) {
            case 0:
                Client.sendChoice(player, "Okay, can you get me ten pieces of wood for me?",
                "I can help with that", "Not right now",
                "Can you help me out? My boat is in shambles right now and you would be of great help to me.");
                break;
            case 1:
                temp = player.inventory.itemCount(ItemSets.wood);

                if (temp >= 10) {
                    Client.sendMessage(player, "Thank you very much for your wood, this will work as a good foundation", "I don't mean to bother you more, but could you also get me about fifteen pieces of string? Thank you very much traveller.");
                    player.setQuestData(1, 2);
                    player.inventory.removeItem(ItemSets.wood, 10);
                } else
                    Client.sendMessage(player, "It does not seem you have what I need. Could you get me ten pieces of wood.");
                break;
            case 2:
                temp = player.inventory.itemCount(ItemList.stringItem);

                if (temp >= 15) {
                    Client.sendMessage(player,"I knew you could do it! I now have everything I need to continue on my journey. You may now use my boat free of charge.");
                    player.setQuestData(1, 3);
                    player.inventory.removeItem(ItemList.stringItem, 15);
                } else
                    Client.sendMessage(player, "It does not seem you have what I need. Could you get me fifteen strings of string.");
                break;
            case 3:
                Client.sendMessage(player, "Come on now, try out my boat, it is brand new.");
                break;
            case 4:
                Client.sendMessage(player, "Thank you for everything you have done. I can now pursue my dream as a captain.");
                break;
        }
    }
}
