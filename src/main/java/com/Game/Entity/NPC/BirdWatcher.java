package com.Game.Entity.NPC;

import com.Game.ConnectionHandling.Client;
import com.Game.Entity.Player.Player;
import com.Game.Inventory.InventoryManager;
import com.Game.Inventory.ItemList;
import com.Game.Skills.Skills;
import com.Game.WorldManagement.World;

public class BirdWatcher extends NPC {

    public BirdWatcher(int id, World world, int x, int y) {
        super(id, world, x, y);

        setImage("birdWatcher.png");
    }

    public void onInteract(Player player) {
        switch (player.getQuestData(0)) {
            case 0:
                player.setChoice(() -> questStart(player), () -> Client.clearTextBox(player),
                        "I would like to help.", "Maybe later.",
                        "I need your help. Would you mind lending me a helping hand?");
                break;
            case 1:
                if (player.inventory.itemCount(ItemList.parrotBird) >= 1)
                    onBird(player);
                else
                    Client.sendText(player, "It does not seem that you have gotten a bird, please come back when you have.");
                break;
            case 2:
                Client.sendText(player, "Thank you very much traveller, you will forever have my thanks!");
        }
    }

    private void onBird(Player player) {
        if (player.inventory.isFull()) {
            Client.sendText(player, "It appears that you do not have any inventory space, please come back and try again.");
            return;
        }

        player.inventory.removeItem(ItemList.parrotBird, 1);
        player.inventory.addItem(ItemList.gold, 1000);
        player.addExperience(Skills.WOODCUTTING, 1000);
        Client.sendText(player,"Thank you very much, I will always be grateful for your deed.");
        Client.sendMessage(player,"For helping the Bird Watcher, you have recieved 1000 Coins and 500 Woodcutting experience!");
        player.setQuestData(0, 2);
    }

    public void questStart(Player player) {
        Client.sendText(player, "Fantastic! I've always come by here to look at the birds"
                + "and I have always wanted one for myself, would you be able"
                + "to get one for me?");
        player.setQuestData(0, 1);
    }

    public void move() {

    }
}
