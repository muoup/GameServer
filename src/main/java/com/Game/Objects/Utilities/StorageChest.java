package com.Game.Objects.Utilities;

import com.Game.Inventory.ItemStack;
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
        for (ItemStack stack : player.inventory.inventory)
            if (stack.preventBanking) {
                player.sendMessage("The magical force of your " + stack.getName() + " prevents you from opening the chest.");
                return false;
            }

        player.setHealth(player.maxHealth);
        player.showBank();
        return true;
    }
}
