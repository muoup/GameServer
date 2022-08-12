package com.Game.Objects.AreaTeleporter;

import com.Game.WorldManagement.World;
import com.Game.WorldManagement.WorldHandler;

public class CaveEntrance extends Teleporter {
    public enum TeleType {
        caveEntrance(487, 353, WorldHandler.underground),
        caveExit(5509, 3743, WorldHandler.main);

        public final int x, y;
        public int destination;

        TeleType(int x, int y, int destination) {
            this.x = x;
            this.y = y;
            this.destination = destination;
        }
    }

    public CaveEntrance(World world, int x, int y, TeleType teleType) {
        super(world, x, y, teleType.destination, teleType.x, teleType.y);
        this.maxDistance = 200f;

        setImage("caveEntrance.png");
    }
}
