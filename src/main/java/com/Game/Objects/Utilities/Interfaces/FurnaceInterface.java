package com.Game.Objects.Utilities.Interfaces;

import com.Game.Entity.Player.Player;
import com.Game.Entity.Player.ShopHandler;
import com.Game.Inventory.ItemList;
import com.Game.Inventory.ItemStack;
import com.Game.Skills.Skills;
import com.Game.Util.Other.PlayerAction;

public class FurnaceInterface extends ModifiedShop {
    private int[] experience = {
            2,
            4,
            4,
            8,
            16
    };

    public FurnaceInterface() {
        super(new ItemStack[] {
                new ItemStack(ItemList.stone, -1, 1),
                new ItemStack(ItemList.copperOre, -1, 1),
                new ItemStack(ItemList.tinOre, -1, 1),
                new ItemStack(ItemList.ironOre, -1, 1),
                new ItemStack(ItemList.goldOre, -1, 1)
                                    },
                "Smelt", "N/A");
    }

    public void shopInteraction(ShopHandler handler, int index, int amount) {
        ItemStack result = offeredItems[index].clone();
        ItemStack input = result.clone();
        input.setData(0);

        result.amount = 1;
        input.amount = 1;

        Player player = handler.getPlayer();

        CraftActionLoop loop = new CraftActionLoop(player, 1250, amount, input, result,
                (crafter) -> crafter.addExperience(Skills.SMITHING, experience[index]));

        player.createPlayerLoop(loop);
    }

    public void inventoryInteraction(ShopHandler handler, int index, int amount) {

    }
}
