package com.Game.Items.RawResource.Log;

import com.Game.GUI.Chatbox.ChatBox;
import com.Game.GUI.Skills.Skills;
import com.Game.Items.Item;
import com.Game.Items.ItemList;
import com.Game.Items.ItemRequirement;
import com.Game.Items.ItemStack;
import com.Util.Other.SpriteSheet;

public class Log extends Item {
    protected ItemList bow;
    protected int arrowShaft = 15;

    public Log(int id, int tier, ItemList bow, String name, String examineText, int maxStack, int worth) {
        super(id, SpriteSheet.woodSheet.getCell((int) (tier / 10.0), 0), name, examineText, maxStack, worth);
        setTier(tier);
        this.bow = bow;
    }

    public void setTier(int tier) {
        requirement = new ItemRequirement(Skills.FLETCHING, tier);
    }

    public void ClickIdentities(int index) {
        // Craft Bow
        if (requirement.meetsRequirement()) {
            replaceInventory(index, new ItemStack((bow == null) ? ItemList.bow : bow, 1, 0));
            Skills.addExperience(Skills.FLETCHING, 25 * (1 + requirement.getLevel() / 5));
        } else {
            ChatBox.sendMessage(requirement.toString());
        }
    }

    public void OnRightClick(int index, int option) {
        switch (option) {
            case 1:
                // Craft Arrow Shafts
                if (requirement.meetsRequirement()) {
                    replaceInventory(index, new ItemStack(ItemList.arrowShaft, arrowShaft));
                    Skills.addExperience(Skills.FLETCHING, 20 * (1 + requirement.getLevel() / 5));
                } else {
                    ChatBox.sendMessage(requirement.toString());
                }
                break;
        }
    }

    public void setData(ItemStack stack) {
        stack.options.clear();
        stack.options.add("Craft Bow");
        stack.options.add("Craft Arrow Shafts");
    }

    public String getOptionText(int i, int data, ItemStack stack) {
        return "Craft " + stack.name + " Bow";
    }
}
