package com.Game.WorldManagement;

import com.Game.WorldManagement.Worlds.*;

public class WorldHandler {
    public static World[] serverWorlds = {
        new MainWorld(0),
        new Underground(1),
        new ChessDungeon(2),
        new Tropics(3)
    };

    public static World main = serverWorlds[0];
    public static World underground = serverWorlds[1];
    public static World chessDungeon = serverWorlds[2];
    public static World tropics = serverWorlds[3];

    public static void init() {
        for (int i = 0; i < serverWorlds.length; i++)
            if (!serverWorlds[i].empty())
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
