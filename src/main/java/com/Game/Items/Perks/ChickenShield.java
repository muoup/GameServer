package com.Game.Items.Perks;

import com.Game.Entity.Player.Player;
import com.Game.Inventory.Item;
import com.Game.Inventory.ItemStack;
import com.Game.PseudoData.ImageIdentifier;
import com.Game.Util.Other.RCOption;
import com.Game.Util.Other.SpriteSheet;

import java.awt.*;

public class ChickenShield extends Item {
    private SpriteSheet chickenSheet;
    private Color protCircleColor;
    private long protTime = 3000;
    private long protCooldown = 25000;

    public ChickenShield(int id, String name, String examineText, int worth, boolean stackable) {
        super(id, name, examineText, worth, stackable);

        chickenSheet = new SpriteSheet("/Items/chicken_shield.png", 48, 48);
        protCircleColor = new Color(133, 255, 133, 61);

        setImage(chickenSheet.getCell(0, 0));
    }

    public void OnClick(Player player, int index) {
        Long shieldTimer = player.getTimer("shield_cooldown");

        // If the player has the shield on cooldown then return
        if (shieldTimer != null && shieldTimer > System.currentTimeMillis()) return;

        // else set the shield timer to the current time + the cooldown
        player.setTimer("shield", protTime);
        player.setTimer("shield_cooldown", protCooldown);

        player.inventory.sendTimer(index, 1000 * 25L);
        player.sendMiscTimer("protcircle" + protCircleColor.getRGB(), protTime);

        // create a lamda expression to run when the player gets hit
        // when the player gets hit, the player is healed rather than damaged
        player.setOnHit((damage) -> {
            if (player.getTimer("shield") != null && player.getTimer("shield") <= System.currentTimeMillis())
                player.changeHealth((float) damage[0] * -1);

            return (float) damage[0];
        });
    }

    public void dataItemChange(ItemStack stack) {
        // set the right click options to an arraylist containing "Shield"
        stack.setOptions(new RCOption("Shield", this::OnClick));

        setImage(chickenSheet.getCell(0, (int) stack.getData()));
    }
}
