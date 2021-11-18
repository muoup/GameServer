package com.Game.Objects.SkillingAreas;

import com.Game.Objects.GameObject;
import com.Game.Entity.Player.Player;
import com.Game.Skills.Skills;
import com.Game.Util.Math.DeltaMath;
import com.Game.WorldManagement.World;

public class MiningRock extends GameObject {

    private int rocks;
    private RockType rockType;
    private float rpTimer;
    private long resetTime = 0L;

    public MiningRock(World world, int x, int y, RockType rockType) {
        super(world, x, y);

        this.rockType = rockType;
        this.maxDistance = 48;
        this.rocks = (int) DeltaMath.range(rockType.minRocks, rockType.maxRocks);
        this.maxTimer = getTime();

        setImage(rockType.imageName);
        setScale(128, 128);
    }

    public float getTime() {
        if (rockType == null) {
            return -1;
        }
        return DeltaMath.range(rockType.minTime, rockType.maxTime);
    }

    public void update() {
        if (rocks == 0) {
            if (System.currentTimeMillis() > resetTime) {
                setImage(rockType.imageName);
                rocks = (int) DeltaMath.range(rockType.minRocks, rockType.maxRocks);
            }
        }
    }

    public boolean onInteract(Player player) {
        if (rocks == 0) {
            return false;
        }

        if (player.getLevel(Skills.MINING) < rockType.level) {
            player.sendMessage("You do not have the requirement of " + rockType.level + " to mine this rock.");
            return false;
        }

        if (player.inventory.isFull()) {
            player.sendMessage("You do not have any inventory space to complete this action!");
            return false;
        }

        if (player.objectInteration != this) {
            initInteraction(player);
        }

        if (System.currentTimeMillis() >= player.completionTime) {
            initInteraction(player);
            rocks--;
            rockType.drops.determineOutput().forEach(player.inventory::addItem);
            player.addExperience(Skills.MINING, rockType.xp);

            if (rocks == 0) {
                rpTimer = DeltaMath.range(rockType.minTime, rockType.maxTime);
                setImage("empty_rock.png");
            }
        }

        return true;
    }

    public void loseFocus() {

    }
}
