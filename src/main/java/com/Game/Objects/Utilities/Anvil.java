package com.Game.Objects.Utilities;

import com.Game.Entity.Player.Player;
import com.Game.Objects.GameObject;
import com.Game.CustomInterfaces.AnvilInterface;
import com.Game.WorldManagement.World;

public class Anvil extends GameObject {
    private final AnvilInterface anvilInterface;

    public Anvil(World world, int x, int y) {
        super(world, x, y);
        setImage("anvil.png");
        setScale(80, 80);

        this.anvilInterface = new AnvilInterface();
        this.maxDistance = 64;
    }

    public boolean onInteract(Player player) {
        player.enableShop(anvilInterface);

        return true;
    }
}
