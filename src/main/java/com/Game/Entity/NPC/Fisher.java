package com.Game.Entity.NPC;

import com.Game.WorldManagement.World;

public class Fisher extends NPC {
    public Fisher(int id, World world, int x, int y) {
        super(id, world, x, y);

        setImage("fish_npc.png");
    }

    // TODO: Shops
    public void onInteract() {
        //GUI.enableShop(Shop.fishing);
    }
}
