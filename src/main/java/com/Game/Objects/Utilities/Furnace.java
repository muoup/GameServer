package com.Game.Objects.Utilities;

import com.Game.Entity.Player.Player;
import com.Game.Objects.UsableGameObject;
import com.Game.CustomInterfaces.FurnaceInterface;
import com.Game.WorldManagement.World;

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
