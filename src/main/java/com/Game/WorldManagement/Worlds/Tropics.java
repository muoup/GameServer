package com.Game.WorldManagement.Worlds;

import com.Game.Entity.NPC.Kanuna;
import com.Game.Objects.AreaTeleporter.InvisibleTeleporter;
import com.Game.WorldManagement.World;
import com.Game.WorldManagement.WorldHandler;

public class Tropics extends World {
    public Tropics(int id) {
        super(id);

        initImage("tribal_lands.png");
    }

    public void worldCreation() {
        // Teleporter to Main Land
        new InvisibleTeleporter(this,1652, 3098, WorldHandler.main, 500, 4100);

        new Kanuna(this, 880, 1900);
    }

    public void update() {

    }
}
