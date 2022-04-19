package com.Game.Objects.AreaTeleporter;

import com.Game.PseudoData.ImageIdentifier;
import com.Game.WorldManagement.World;

public class InvisibleTeleporter extends Teleporter {
    public InvisibleTeleporter(World world, int x, int y, int destination, int tx, int ty) {
        super(world, x, y, destination, tx, ty);

        this.maxDistance = 32;
        this.image = ImageIdentifier.emptyImage();
    }
}
