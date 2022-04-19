package com.Game.Objects.SkillingAreas;

import com.Game.Objects.GameObject;
import com.Game.Entity.Player.Player;
import com.Game.Skills.Skills;
import com.Game.Util.Math.DeltaMath;
import com.Game.WorldManagement.World;

import java.util.ArrayList;

public class MiningRock extends GameObject {

    private int rocks;
    private RockType rockType;
    private long nextReset;
    private ArrayList<Player> interacters;

    public MiningRock(World world, int x, int y, RockType rockType) {
        super(world, x, y);

        this.rockType = rockType;
        this.maxDistance = 48;
        this.rocks = (int) DeltaMath.range(rockType.minRocks, rockType.maxRocks);
        this.maxTimer = getTime();
        this.interacters = new ArrayList<>();

        setImage(rockType.imageName);
        setScale(128, 128);
    }

    public float getTime() {
        if (rockType == null) {
            return -1;
        }
        return DeltaMath.range(rockType.minTimer, rockType.maxTimer);
    }

    public void update() {
        if (rocks == 0) {
            if (System.currentTimeMillis() > nextReset) {
                setImage(rockType.imageName);
                setScale(128, 128);
                rocks = (int) DeltaMath.range(rockType.minRocks, rockType.maxRocks);
            }
        }

        for (int i = 0; i < interacters.size(); i++) {
            Player player = interacters.get(i);

            if (System.currentTimeMillis() >= player.completionTime) {
                onInteract(player);
                rocks--;
                rockType.drops.determineOutput().forEach(player.inventory::addItem);
                player.addExperience(Skills.MINING, rockType.xp);

                if (rocks == 0) {
                    nextReset = System.currentTimeMillis() + (long) DeltaMath.range(rockType.minTimer, rockType.maxTimer);
                    setImage(RockType.stone.imageName);
                    setScale(128, 128);
                    player.loseFocus();
                }
            }
        }
    }

    public boolean onInteract(Player player) {
        player.loseFocus();

        if (rocks == 0) {
            return false;
        }

        if (player.getLevel(Skills.MINING) < rockType.lvlReq) {
            player.sendMessage("You do not have the requirement of " + rockType.lvlReq + " to mine this rock.");
            return false;
        }

        if (player.inventory.isFull()) {
            player.sendMessage("You do not have any inventory space to complete this action!");
            return false;
        }

        initInteraction(player);

        if (!interacters.contains(player)) {
            interacters.add(player);
        }

        return true;
    }

    public int getMillisTimer(Player player) {
        return rockType.getTimer(player);
    }

    public void loseFocus(Player player) {
        interacters.remove(player);
    }
}
