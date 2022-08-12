package com.Game.Objects.AreaTeleporter;

import com.Game.WorldManagement.World;

public class Ladder extends Teleporter {
    public Ladder(World world, int x, int y, int destination, int tx, int ty) {
        super(world, x, y, destination, tx, ty);
        this.maxDistance = 200;

        setImage("ladderDown.png");
        setScale(64, 64);
    }
}
