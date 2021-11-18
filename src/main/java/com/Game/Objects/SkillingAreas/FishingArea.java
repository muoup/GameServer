package com.Game.Objects.SkillingAreas;

import com.Game.Inventory.ItemList;
import com.Game.Objects.GameObject;
import com.Game.Entity.Player.Player;
import com.Game.PseudoData.ImageIdentifier;
import com.Game.Skills.Skills;
import com.Game.WorldManagement.World;

public class FishingArea extends GameObject {

    private FishingPreset preset;

    public FishingArea(World world, int x, int y, FishingPreset preset) {
        super(world, x, y);

        image = ImageIdentifier.emptyImage();
        maxDistance = 32f;
        this.preset = preset;
    }

    public boolean onInteract(Player player) {
        if (player.getLevel(Skills.FISHING) < preset.lvlReq) {
            player.sendMessage("You do not have the required fishing level of " + preset.lvlReq);
            return false;
        }

        if (player.inventory.isFull()) {
            player.sendMessage("You do not have any inventory space!");
            return false;
        }

        if (player.objectInteration != this) {
            initInteraction(player);
        }

        if (player.timerComplete()) {
            initInteraction(player);
            maxTimer = preset.getTimer(player);

            player.inventory.addItem(preset.fish, 1);
            player.addExperience(Skills.FISHING, preset.getXp());

            if (player.inventory.itemCount(ItemList.fishBait) >= 1) {
                player.inventory.removeItem(ItemList.fishBait, 1);
            }
        }

        return true;
    }

    public void loseFocus(Player player) {
        player.resetInteraction();
    }
}