package com.Game.Items.Perks;

import com.Game.Entity.Player.Player;
import com.Game.Inventory.Item;
import com.Game.Inventory.ItemStack;
import com.Game.PseudoData.ImageIdentifier;
import com.Game.Util.Other.RCOption;

public class ChickenShield extends Item {
    public ChickenShield(int id, String name, String examineText, int worth, boolean stackable) {
        super(id, name, examineText, worth, stackable);

        setImage(ImageIdentifier.singleImage("/Items/chicken_shield.png"));
    }

    public void OnClick(Player player, int index) {
        Long shieldTimer = player.getTimer("shield");

        // If the player has the shield on cooldown then return
        if (shieldTimer != null && shieldTimer > System.currentTimeMillis()) return;

        // else set the shield timer to the current time + the cooldown
        player.setTimer("shield", System.currentTimeMillis() + 1000 * 10L);
        player.setTimer("shield_cooldown", System.currentTimeMillis() + 1000 * 25L);

        // create a lamda expression to run when the player gets hit
        // when the player gets hit, the player is healed rather than damaged
        player.setOnHit((damage) -> {
            if (player.getTimer("shield") != null && player.getTimer("shield") <= System.currentTimeMillis())
                player.changeHealth((float) damage[0] * -1);

            player.onHit = null;
            return (float) damage[0];
        });
    }

    public void dataItemChange(ItemStack stack) {
        // set the right click options to an arraylist containing "Shield"
        stack.setOptions(new RCOption("Shield", this::OnClick));
    }
}
