package com.Game.Objects.AreaTeleporter;

import com.Game.Objects.GameObject;
import com.Game.Entity.Player.Player;
import com.Game.WorldManagement.World;
import com.Game.WorldManagement.WorldHandler;

public class Teleporter extends GameObject {
    private int tx, ty;
    private World destination;

    public Teleporter (World objectWorld, int x, int y, int destination, int tx, int ty) {
        super(objectWorld, x, y);
        this.tx = tx;
        this.ty = ty;
        this.destination = WorldHandler.getWorld(destination);
    }

    public Teleporter(World objectWorld, int x, int y, World destination, int tx, int ty) {
        super(objectWorld, x, y);
        this.tx = tx;
        this.ty = ty;
        this.destination = destination;
    }

    public boolean isSuccessful(Player player) {
        return true;
    }

    public void onFail(Player player) {

    }

    public boolean onInteract(Player player) {
        if (isSuccessful(player)) {
            player.teleport(tx, ty, destination);
            return true;
        } else {
            onFail(player);
            return false;
        }
    }
}
