package com.Game.Items.RawResource.Log;

import com.Game.ConnectionHandling.Client;
import com.Game.Inventory.Item;
import com.Game.Inventory.ItemList;
import com.Game.Inventory.ItemStack;
import com.Game.ItemData.Requirement.ActionRequirement;
import com.Game.Entity.Player.Player;
import com.Game.Skills.Skills;
import com.Game.Util.Other.RCOption;
import com.Game.Util.Other.SpriteSheet;

import java.util.ArrayList;

public class Log extends Item {
    protected ItemList bow;
    protected int arrowShaft = 15;
    private int tier;

    public Log(int id, int tier, ItemList bow, String name, String examineText, int worth, boolean stackable) {
        super(id, name, examineText, worth, false);
        this.bow = bow;
        this.stackable = stackable;

        setImage(SpriteSheet.woodSheet.getCell((int) (tier / 10.0), 0));
        setTier(tier);
    }

    public void setTier(int tier) {
        requirement = ActionRequirement.skill(Skills.FLETCHING, tier);
        this.tier = tier;
    }

    public void craftBow(Player player, int index) {
        if (player.skillCompare (Skills.FLETCHING, tier)) {
            craftActionLoop(player, 1000, 20, Skills.FLETCHING,
                    25 * (1 + (tier / 5)), 1,
                    new ItemStack((bow == null) ? ItemList.bow : bow, 1),
                    getClickStack(player, index));
        } else {
            Client.sendMessage(player, requirement.toString());
        }
    }

    public void craftShafts(Player player, int index) {
        // Craft Arrow Shafts
        if (player.skillCompare(Skills.FLETCHING, tier)) {
            craftActionLoop(player, 1000, 20, Skills.FLETCHING,
                    20 * (1 + tier / 5), 1, new ItemStack(ItemList.arrowShaft, 10 * (1 + tier / 20)),
                    getClickStack(player, index));
        } else {
            player.sendMessage(requirement.toString());
        }
    }

    public void dataItemChange(ItemStack stack) {
        stack.setOptions(
                new RCOption("Craft Bow", this::craftBow),
                new RCOption("Craft Arrow Shafts", this::craftShafts));
    }
}
