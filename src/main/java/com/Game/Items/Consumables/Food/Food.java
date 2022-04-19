package com.Game.Items.Consumables.Food;

import com.Game.Inventory.Item;
import com.Game.Inventory.ItemStack;
import com.Game.Entity.Player.Player;
import com.Game.PseudoData.ImageIdentifier;
import com.Game.Util.Other.RCOption;

public class Food extends Item {
    int healAmount;
    String eatText;

    public Food(int id, String name, String examineText, int worth, boolean stackable, int healAmount) {
        super(id, name, examineText, worth, stackable);
        this.healAmount = healAmount;
        this.eatText = "You eat the " + name + " and it heals some health.";
    }

    public void OnClick(Player player, int index) {
        eatFood(player, index);
    }

    public void eatFood(Player player, int index) {
        if (player.health < player.maxHealth) {
            player.inventory.removeItem(index, 1);
            player.changeHealth(healAmount);
            player.sendMessage(eatText);
        } else {
            player.sendMessage("You are already at full health!");
        }
    }

    public void dataItemChange(ItemStack stack) {
        stack.setOptions(new RCOption("Eat", this::eatFood));
    }
}
