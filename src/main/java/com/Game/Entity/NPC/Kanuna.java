package com.Game.Entity.NPC;

import com.Game.Entity.Player.Player;
import com.Game.WorldManagement.World;

public class Kanuna extends NPC {
    public Kanuna(int id, World world, int x, int y) {
        super(id, world, x, y);
        setImage("tribalKanuna.png");
    }

    // TODO: Shops
    public void onInteract(Player player) {
        player.enableShop(Shop.kanuna);
    }
}
