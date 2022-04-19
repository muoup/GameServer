package com.Game.Objects.AreaTeleporter;

import com.Game.WorldManagement.World;

public class Ladder extends Teleporter {
    public Ladder(World objectWorld, int x, int y, int destination, int tx, int ty) {
        super(objectWorld, x, y, destination, tx, ty);
        this.maxDistance = 200;

        setImage("ladderDown.png");
        setScale(64, 64);
    }
}
