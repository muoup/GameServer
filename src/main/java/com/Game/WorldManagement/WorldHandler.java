package com.Game.WorldManagement;

import com.Game.WorldManagement.Worlds.*;

public class WorldHandler {
    public static World[] serverWorlds = {
            new MainWorld(0),
            new Underground(1),
            new ChessDungeon(2),
            new Tropics(3),
            new FarmUnderground(4),
            new QueenLair(5)
    };

    public static final int main = 0;
    public static final int underground = 1;
    public static final int chessDungeon = 2;
    public static final int tropics = 3;
    public static final int farmUnderground = 4;
    public static final int queenLair = 5;

    public static void init() {
        for (int i = 0; i < serverWorlds.length; i++)
            serverWorlds[i].worldCreation();
    }

    public static void update() {
        for (int i = 0; i < serverWorlds.length; i++)
            if (!serverWorlds[i].empty())
                serverWorlds[i].update();
    }

    public static World getWorld(int id) {
        return serverWorlds[id];
    }
}
