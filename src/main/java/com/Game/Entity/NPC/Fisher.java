package com.Game.Entity.NPC;

import com.Game.Entity.Player.Player;
import com.Game.WorldManagement.World;

public class Fisher extends NPC {
    public Fisher(World world, int x, int y) {
        super(world, x, y);

        setImage("fish_npc.png");
    }

    public void onInteract(Player player) {
        player.enableShop(Shop.fishing);
    }
}
