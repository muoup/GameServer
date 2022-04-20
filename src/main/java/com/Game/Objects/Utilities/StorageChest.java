package com.Game.Objects.Utilities;

import com.Game.Objects.GameObject;
import com.Game.Entity.Player.Player;
import com.Game.WorldManagement.World;

public class StorageChest extends GameObject {
    public StorageChest(World world, int x, int y) {
        super(world, x, y);

        this.maxDistance = 72;

        setImage("chest.png");
        setScale(64, 64);
    }

    public boolean onInteract(Player player) {
        player.setHealth(player.maxHealth);
        player.showBank();
        return true;
    }
}
