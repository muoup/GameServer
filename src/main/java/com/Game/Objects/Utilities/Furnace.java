package com.Game.Objects.Utilities;

import com.Game.Entity.Player.Player;
import com.Game.Inventory.InventoryManager;
import com.Game.Inventory.ItemList;
import com.Game.Inventory.ItemStack;
import com.Game.Objects.UsableGameObject;
import com.Game.Objects.Utilities.Interfaces.FurnaceInterface;
import com.Game.Skills.Skills;
import com.Game.Util.Other.Settings;
import com.Game.WorldManagement.World;

import java.util.Timer;

public class Furnace extends UsableGameObject {
    private final FurnaceInterface furnaceInterface;

    public Furnace(World world, int x, int y) {
        super(world, x, y);

        setImage("Furnace.png");
        setScale(48, 63);

        this.furnaceInterface = new FurnaceInterface();
        this.maxDistance = 64;
    }

    public boolean onInteract(Player player) {
        player.enableShop(furnaceInterface);

        return true;
    }
}
