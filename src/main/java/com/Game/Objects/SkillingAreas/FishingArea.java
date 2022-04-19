package com.Game.Objects.SkillingAreas;

import com.Game.Inventory.ItemList;
import com.Game.Objects.GameObject;
import com.Game.Entity.Player.Player;
import com.Game.PseudoData.ImageIdentifier;
import com.Game.Skills.Skills;
import com.Game.WorldManagement.World;

import java.util.ArrayList;

public class FishingArea extends GameObject {

    private FishingPreset preset;
    private ArrayList<Player> interacters;

    public FishingArea(World world, int x, int y, FishingPreset preset) {
        super(world, x, y);

        image = ImageIdentifier.emptyImage();
        maxDistance = 32f;
        interacters = new ArrayList<>();
        this.preset = preset;
    }

    public void update() {
        for (int i = 0; i < interacters.size(); i++) {
            Player player = interacters.get(i);
            if (System.currentTimeMillis() > player.completionTime) {
                player.addItem(preset.fish, 1);
                player.addExperience(Skills.FISHING, preset.getXp());

                if (player.hasItem(ItemList.fishBait, 0)) {
                    player.removeItem(ItemList.fishBait, 0, 1);
                }

                onInteract(player);
            }
        }
    }

    public boolean onInteract(Player player) {
        player.loseFocus();

        if (player.getLevel(Skills.FISHING) < preset.lvlReq) {
            player.sendMessage("You do not have the required fishing level of " + preset.lvlReq);
            player.loseFocus();
            return false;
        }

        if (player.inventory.isFull()) {
            player.sendMessage("You do not have any inventory space!");
            player.loseFocus();
            return false;
        }

        initInteraction(player);

        if (!interacters.contains(player)) {
            interacters.add(player);
        }
        return true;
    }

    public int getMillisTimer(Player player) {
        return (int) (preset.getTimer(player) * (player.hasItem(ItemList.fishBait, 0) ? 0.6f : 1));
    }

    public void loseFocus(Player player) {
        interacters.remove(player);
    }
}