package com.Game.WorldManagement.Worlds;

import com.Game.Entity.Enemy.UndergroundFarm.Clucky;
import com.Game.Objects.AreaTeleporter.InvisibleTeleporter;
import com.Game.WorldManagement.World;
import com.Game.WorldManagement.WorldHandler;

public class FarmUnderground extends World {
    public FarmUnderground(int id) {
        super(id);

        initImage("underground_farmhouse.png");
    }

    public void worldCreation() {
        new Clucky(this, 935, 1171);
        new InvisibleTeleporter(this, 86, 58, WorldHandler.main, 3411, 921);
    }
}
