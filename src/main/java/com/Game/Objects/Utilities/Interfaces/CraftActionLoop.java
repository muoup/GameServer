package com.Game.Objects.Utilities.Interfaces;

import com.Game.Entity.Player.Player;
import com.Game.Inventory.Item;
import com.Game.Inventory.ItemStack;
import com.Game.Util.Other.PlayerAction;
import com.Game.Util.Other.PlayerActionLoop;

public class CraftActionLoop extends PlayerActionLoop {
    public int index = -1;

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
                           PlayerAction grantExp, ItemStack output, ItemStack... inputs) {
        this(player, loopTime, loops, 1, grantExp, output, inputs);
    }

    public CraftActionLoop(Player player, long loopTime, int loops, int amountPer,
                           PlayerAction grantExp, ItemStack output, ItemStack... inputs) {
        super(player, null, loopTime);
        player.playerLoopsRemaining = loops;

        PlayerAction craftAction = (crafter) -> {
            int amount = amountPer;

            for (ItemStack input : inputs) {
                amount = Math.min(amount, player.inventory.itemCount(input) / input.getAmount());
            }

            if (amount == 0)
                return false;

            for (ItemStack input : inputs) {
                crafter.removeItem(input.getItemList(), input.getData(), input.getAmount() * amount);
                crafter.addItem(output, output.getAmount() * amount);

                grantExp.onAction(crafter);
            }

            player.playerLoopsRemaining--;

            return player.playerLoopsRemaining > 0;
        };

        setPlayerAction(craftAction);
    }

    public CraftActionLoop(Player player, long loopTime, int loops, int amountPer,
                           int skill, float amount, ItemStack output, ItemStack... inputs) {
        this(player, loopTime, loops, amountPer, (crafter) -> crafter.addExperience(skill, amount), output, inputs);
    }

    public CraftActionLoop(Player player, int index, long loopTime, int skill, float experience, int newData, ItemStack... input) {
        this(player, index, loopTime, skill, experience, newData, 1, input);
    }

    public CraftActionLoop(Player player, int index, long loopTime, int skill, float experience, int newData, int amount, ItemStack... input) {
        super(player, null, loopTime);
        this.index = index;
        player.playerLoopsRemaining = 1;

        ItemStack invInput = player.inventory.getStack(index).clone();
        ItemStack invOutput = invInput.clone();
        invOutput.setData(newData);

        PlayerAction craftAction = (crafter) -> {
            for (ItemStack item : input) {
                if (crafter.inventory.itemCount(item.getItemList()) < item.getAmount())
                    return false;

                crafter.removeItem(item.getItemList(), item.getData(), item.getAmount());
            }

            invOutput.amount = Math.min(player.inventory.itemCount(invInput), amount);

            crafter.removeItem(invInput, amount);
            crafter.addItem(invOutput);
            crafter.addExperience(skill, experience * invOutput.amount);

            return player.inventory.itemCount(invInput) != 0;
        };

        setPlayerAction(craftAction);
    }
}
