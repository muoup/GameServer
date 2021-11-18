package com.Game.Objects.AreaTeleporter;

import com.Game.Entity.Player.Player;
import com.Game.WorldManagement.World;

public class TribalTeleport extends InvisibleTeleporter {
    public TribalTeleport(World world, int x, int y, World destination, int tx, int ty) {
        super(world, x, y, destination, tx, ty);
    }

    public boolean isSuccessful(Player player) {
        if (player.getQuestData(1) == 3)
            player.setQuestData(1, 4);

        return player.isQuestComplete(1);
    }

    public void unSuccessful(Player player) {
        player.sendMessage("Maybe you should ask the captain before you just ride his ship.");
    }
}
