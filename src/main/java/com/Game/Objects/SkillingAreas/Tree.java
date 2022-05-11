package com.Game.Objects.SkillingAreas;

import com.Game.Inventory.ItemList;
import com.Game.Objects.GameObject;
import com.Game.Entity.Player.Player;
import com.Game.Skills.Skills;
import com.Game.WorldManagement.World;

import java.util.ArrayList;
import java.util.Random;

public class Tree extends GameObject {

    TreePreset preset;
    int woodAmount;
    private long delta = 0;
    private float resetTime = 0;

    public ArrayList<Player> interacters = new ArrayList();

    public Tree(World world, int x, int y, TreePreset preset) {
        super(world, x, y);

        this.preset = preset;
        setImage(preset.imageName);
        maxDistance = 64f;
        woodAmount = preset.getWoodAmount();
    }

    public void update() {
        if (woodAmount == 0) {
            if (System.currentTimeMillis() - delta > resetTime) {
                setImage(preset.imageName);
                woodAmount = preset.getWoodAmount();
            }
        }

        for (int i = 0; i < interacters.size(); i++) {
            Player player = interacters.get(i);
            if (System.currentTimeMillis() > player.completionTime) {
                woodAmount--;
                player.addItem(preset.wood, 1);
                player.addExperience(Skills.WOODCUTTING, preset.getXp());

                // If the 'Bird Watching' Quest is underway, there is a chance that the player collects
                // a bird that is needed in order to finish the quest.
                if (player.getQuestData(0) == 1) {
                    // The chance that the player collects the bird is a 1 in 16 chance.
                    // This should only take around 2 or 3 minutes to complete hopefully.

                    // Making the birds nest drop the parrot bird will introduce the player to the idea of a bird's nest
                    // without having to explain it which is quite handy.
                    if (new Random().nextInt(15) == 5 && player.inventory.itemCount(ItemList.parrotBird) == 0) {
                        player.addItem(ItemList.parrotBird, 1);
                    }
                }

                if (Math.random() * 100 < 0.05 * Math.pow(player.getLevel(Skills.WOODCUTTING), 2/3)) {
                    player.addItem(ItemList.birdNest, 1);
                }

                if (woodAmount == 0) {
                    delta = System.currentTimeMillis();
                    resetTime = (preset.getTimer(player) * 2) * 1000;
                    setImage("toppled_" + preset.imageName);

                    for (int i1 = 0; i1 < interacters.size(); i1++) {
                        Player p = interacters.get(i1);
                        p.loseFocus();
                    }

                    interacters.clear();
                }

                onInteract(player);
            }
        }
    }

    public boolean onInteract(Player player) {
        player.loseFocus();
        player.resetAnimation();

        if (woodAmount == 0) {
            player.changeSprite("idle");
            return false;
        }

        if (player.getLevel(Skills.WOODCUTTING) < preset.lvlReq) {
            player.sendMessage("You do not have the required woodcutting level of " + preset.lvlReq);
            return false;
        }

        if (player.inventory.isFull()) {
            player.sendMessage("You do not have any inventory space!");
            return false;
        }

        initInteraction(player);

        if (!interacters.contains(player)) {
            interacters.add(player);
        }

        player.changeSprite("chop");

        return true;
    }

    public int getMillisTimer(Player player) {
        return preset.getTimer(player);
    }

    public void loseFocus(Player player) {
        interacters.remove(player);
    }
}