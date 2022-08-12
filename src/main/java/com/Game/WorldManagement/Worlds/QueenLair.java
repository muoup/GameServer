package com.Game.WorldManagement.Worlds;

import com.Game.Objects.AreaTeleporter.InvisibleTeleporter;
import com.Game.WorldManagement.World;
import com.Game.WorldManagement.WorldHandler;

public class QueenLair extends World {

    public QueenLair(int id) {
        super(id);

        initImage("queens_lair.png");
    }

    public void worldCreation() {
        new InvisibleTeleporter(this, 725, 135, WorldHandler.chessDungeon, 1330, 1438);
    }
}
