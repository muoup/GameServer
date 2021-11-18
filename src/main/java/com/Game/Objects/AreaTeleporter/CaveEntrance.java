package com.Game.Objects.AreaTeleporter;

import com.Game.WorldManagement.World;
import com.Game.WorldManagement.WorldHandler;

public class CaveEntrance extends Teleporter {
    public enum TeleType {
        caveEntrance(784, 230, WorldHandler.underground),
        caveExit(5150, 3534, WorldHandler.main);

        public final int x, y;
        public World destination;

        TeleType(int x, int y, World destination) {
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
