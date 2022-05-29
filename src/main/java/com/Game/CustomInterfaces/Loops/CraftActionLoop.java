package com.Game.CustomInterfaces.Loops;

import com.Game.Entity.Player.Player;
import com.Game.Inventory.ItemStack;
import com.Game.Util.Other.ExpAction;
import com.Game.Util.Other.PlayerAction;
import com.Game.Util.Other.PlayerActionLoop;

public class CraftActionLoop extends PlayerActionLoop {
    public int index = -1;

    public CraftActionLoop(Player player, long loopTime, int loops, ExpAction grantExp, int craftsPer, int outputCount, ItemStack... inputOutputs) {
        super(player, null, loopTime);
        player.playerLoopsRemaining = loops;

        ItemStack outputs[] = new ItemStack[outputCount];
        ItemStack inputs[] = new ItemStack[inputOutputs.length - outputCount];

        // Separate the outputs and inputs, the array starts with outputCount outputs and then the rest are inputs
        for (int i = 0; i < inputOutputs.length; i++) {
            if (i < outputCount) {
                outputs[i] = inputOutputs[i];
            }
            else {
                inputs[i - outputCount] = inputOutputs[i];
            }
        }

        for (ItemStack input : inputs) {
            if (player.inventory.itemCount(input) < input.getAmount()) {
                player.sendMessage("You don't have enough " + input.getName() + " to craft.");

                if (input.getAmount() > 1) {
                    player.sendMessage("You need " + input.getAmount() + " " + input.getName() + " to craft.");
                }
            }
        }

        PlayerAction craftAction = (crafter) -> {
            int amt = craftsPer;

            for (ItemStack input : inputs) {
                int count = player.inventory.itemCount(input);

                amt = Math.min(amt, count / input.getAmount());
            }

            if (amt == 0)
                return false;

            for (ItemStack input : inputs)
                player.inventory.removeItem(input, amt);

            for (ItemStack output : outputs)
                player.inventory.addItem(output, amt);

            grantExp.grant(player, amt);

            player.playerLoopsRemaining--;
            return player.playerLoopsRemaining > 0;
        };

        setPlayerAction(craftAction);
    }

    public CraftActionLoop(Player player, long loopTime, int loops, int skill, float exp, int craftsPer, ItemStack... outputAndInputs) {
        this(player, loopTime, loops, (p, a) -> p.addExperience(skill, exp), craftsPer, 1, outputAndInputs);
    }

}

//    public CraftActionLoop(Player player, long loopTime, int loops,
//                           ItemStack input, ItemStack output, ExpAction grantExp) {
//        super(player, null, loopTime);
//        player.playerLoopsRemaining = loops;
//
//        PlayerAction craftAction = (crafter) -> {
//            if (crafter.inventory.itemCount(input.getItemList(), input.getData()) < input.getAmount())
//                return false;
//
//            crafter.removeItem(input.getItemList(), input.getData(), input.getAmount());
//            crafter.addItem(output);
//
//            grantExp.grant(crafter, 1);
//            player.playerLoopsRemaining--;
//
//            return player.playerLoopsRemaining > 0;
//        };
//
//        setPlayerAction(craftAction);
//    }
//
//    public CraftActionLoop(Player player, long loopTime, int loops,
//                           ExpAction grantExp, ItemStack output, ItemStack... inputs) {
//        this(player, loopTime, loops, 1, grantExp, output, inputs);
//    }
//
//    public CraftActionLoop(Player player, long loopTime, int loops, int amountPer,
//                           ExpAction grantExp, ItemStack output, ItemStack... inputs) {
//        super(player, null, loopTime);
//        player.playerLoopsRemaining = loops;
//
//        PlayerAction craftAction = (crafter) -> {
//            int amount = amountPer;
//
//            for (ItemStack input : inputs) {
//                amount = Math.min(amount, player.inventory.itemCount(input) / input.getAmount());
//            }
//
//            if (amount == 0)
//                return false;
//
//            for (ItemStack input : inputs) {
//                crafter.removeItem(input.getItemList(), input.getData(), input.getAmount() * amount);
//                crafter.addItem(output, output.getAmount() * amount);
//
//                grantExp.grant(crafter, amount);
//            }
//
//            player.playerLoopsRemaining--;
//
//            return player.playerLoopsRemaining > 0;
//        };
//
//        setPlayerAction(craftAction);
//    }
//
//    public CraftActionLoop(Player player, long loopTime, int loops, int amountPer,
//                           int skill, float amount, ItemStack output, ItemStack... inputs) {
//        this(player, loopTime, loops, amountPer, (crafter, mult) -> crafter.addExperience(skill, amount * mult), output, inputs);
//    }
//
//    public CraftActionLoop(Player player, int index, long loopTime, int skill, float experience, int newData, ItemStack... input) {
//        this(player, index, loopTime, skill, experience, newData, 1, input);
//    }
//
//    public CraftActionLoop(Player player, int index, long loopTime, int skill, float experience, int newData, int amount, ItemStack... input) {
//        super(player, null, loopTime);
//        this.index = index;
//        player.playerLoopsRemaining = 1;
//
//        ItemStack invInput = player.inventory.getStack(index).clone();
//        ItemStack invOutput = invInput.clone();
//        invOutput.setData(newData);
//
//        PlayerAction craftAction = (crafter) -> {
//            for (ItemStack item : input) {
//                if (crafter.inventory.itemCount(item.getItemList()) < item.getAmount())
//                    return false;
//
//                crafter.removeItem(item.getItemList(), item.getData(), item.getAmount());
//            }
//
//            invOutput.amount = Math.min(player.inventory.itemCount(invInput), amount);
//
//            crafter.removeItem(invInput, amount);
//            crafter.addItem(invOutput);
//            crafter.addExperience(skill, experience * invOutput.amount);
//
//            return player.inventory.itemCount(invInput) != 0;
//        };
//
//        setPlayerAction(craftAction);
//