package com.Game.Items.RawResource.Log;

import com.Game.ConnectionHandling.Client;
import com.Game.Inventory.Item;
import com.Game.Inventory.ItemList;
import com.Game.Inventory.ItemStack;
import com.Game.ItemData.Requirement.ActionRequirement;
import com.Game.Entity.Player.Player;
import com.Game.Skills.Skills;
import com.Game.Util.Other.SpriteSheet;

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

    public void ClickIdentities(Player player, int index) {
        // Craft Bow
        if (player.skillCompare (Skills.FLETCHING, tier)) {
            replaceInventory(player, index, new ItemStack((bow == null) ? ItemList.bow : bow, 1, 0));
            player.skills.addExperience(Skills.FLETCHING, 25 * (1 + tier / 5));
        } else {
            Client.sendMessage(player, requirement.toString());
        }
    }

    public void OnRightClick(Player player, int index, int option) {
        switch (option) {
            case 1:
                // Craft Arrow Shafts
                if (player.skillCompare(Skills.FLETCHING, tier)) {
                    replaceInventory(player, index, new ItemStack(ItemList.arrowShaft, arrowShaft));
                    player.addExperience(Skills.FLETCHING, 20 * (1 + tier / 5));
                } else {
                    player.sendMessage(requirement.toString());
                }

                break;
        }
    }

    public void setData(Player player, ItemStack stack) {
        stack.options.clear();
        stack.options.add("Craft Bow");
        stack.options.add("Craft Arrow Shafts");
    }

    public String getOptionText(Player player, int i, int data, ItemStack stack) {
        return "Craft " + stack.getName() + " Bow";
    }
}
