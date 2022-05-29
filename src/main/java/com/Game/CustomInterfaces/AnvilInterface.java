package com.Game.CustomInterfaces;

import com.Game.CustomInterfaces.Loops.CraftActionLoop;
import com.Game.Entity.NPC.Shop;
import com.Game.Entity.Player.Player;
import com.Game.Entity.Player.ShopHandler;
import com.Game.Inventory.ItemList;
import com.Game.Inventory.ItemStack;
import com.Game.Skills.Skills;

public class AnvilInterface extends Shop {
    public AnvilInterface() {
        super(new ItemStack[] {
                new ItemStack(ItemList.rockHelmet, -1, 1),
                new ItemStack(ItemList.rockChestplate, -1, 1),
                new ItemStack(ItemList.rockLeggings, -1, 1),
                new ItemStack(ItemList.rockBoots, -1, 1),
                ItemStack.shopBreakPoint,
                new ItemStack(ItemList.copperHelmet, -1, 1),
                new ItemStack(ItemList.copperChestplate, -1, 1),
                new ItemStack(ItemList.copperLeggings, -1, 1),
                new ItemStack(ItemList.copperBoots, -1, 1),
                ItemStack.shopBreakPoint,
                new ItemStack(ItemList.tinHelmet, -1, 1),
                new ItemStack(ItemList.tinChestplate, -1, 1),
                new ItemStack(ItemList.tinLeggings, -1, 1),
                new ItemStack(ItemList.tinBoots, -1, 1)
                             });
        this.shopVerb = "Smith";
        this.inventoryVerb = "N/A";
    }

    public int[] barRequirement = {
            3,
            5,
            4,
            2
    };

    public ItemStack[] barList = {
            new ItemStack(ItemList.stone, 0, 1),
            new ItemStack(ItemList.copperOre, 0, 1),
            new ItemStack(ItemList.tinOre, 0, 1),
    };

    public int[] experience = {
            15,
            30,
            30
    };

    public void shopInteraction(ShopHandler handler, int index, int amount) {
        int row = index / 5; // 5 Includes the break point
        int col = index % 5;

        ItemStack result = offeredItems[index].clone();
        ItemStack input = barList[row].clone();

        result.amount = 1;
        input.amount = barRequirement[col];

        Player player = handler.getPlayer();

        CraftActionLoop loop = new CraftActionLoop(player, 750 * barRequirement[col], amount,
                Skills.SMITHING, experience[row] * barRequirement[col], 1, result, input);

        player.createPlayerLoop(loop);
    }

    public void inventoryInteraction(ShopHandler handler, int index, int amount) {

    }
}
