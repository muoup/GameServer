package com.Game.Entity.NPC;

import com.Game.ConnectionHandling.Client;
import com.Game.Entity.Player.Player;
import com.Game.WorldManagement.World;

public class SpawnWizard extends NPC {
    public SpawnWizard(World world, int x, int y) {
        super(world, x, y);

        setImage("SpawnNPC.png");
    }

    public void onInteract(Player player) {
        Client.sendText(player,"Hey [name], good to see you around. " +
                "There's tons of treasure to be found on this here island, but there's also a lot of danger. " +
                "Watch your back and be careful!");
    }
}
