package com.Game.Objects.Utilities.Interfaces;

import com.Game.Entity.Player.Player;
import com.Game.Inventory.Item;
import com.Game.Inventory.ItemStack;
import com.Game.Util.Other.PlayerAction;
import com.Game.Util.Other.PlayerActionLoop;

public class CraftActionLoop extends PlayerActionLoop {
    public CraftActionLoop(Player player, long loopTime, int loops,
                           ItemStack input, ItemStack output, PlayerAction grantExp) {
        super(player, null, loopTime);
        player.playerLoopsRemaining = loops;

        PlayerAction craftAction = (crafter) -> {
            if (crafter.inventory.itemCount(input.getItemList(), input.getData()) < input.getAmount())
                return false;

            crafter.removeItem(input.getItemList(), input.getData(), input.getAmount());
            crafter.addItem(output);

            grantExp.onAction(crafter);
            player.playerLoopsRemaining--;

            return player.playerLoopsRemaining > 0;
        };

        setPlayerAction(craftAction);
    }

    public CraftActionLoop(Player player, long loopTime, int loops,
                           ItemStack[] inputs, ItemStack output, PlayerAction grantExp) {
        super(player, null, loopTime);
        player.playerLoopsRemaining = loops;

        PlayerAction craftAction = (crafter) -> {
            for (ItemStack input : inputs) {
                if (crafter.inventory.itemCount(input.getItemList()) < input.getAmount())
                    return false;

                crafter.removeItem(input.getItemList(), input.getData(), input.getAmount());
                crafter.addItem(output);

                grantExp.onAction(crafter);
                player.playerLoopsRemaining--;

                if (player.playerLoopsRemaining <= 0)
                    return false;
            }

            return true;
        };

        setPlayerAction(craftAction);
    }
}
