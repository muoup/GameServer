package com.Game.Objects.SkillingAreas;

import com.Game.Inventory.ItemList;
import com.Game.Entity.Player.Player;
import com.Game.Inventory.ItemStack;
import com.Game.Skills.Skills;
import com.Game.Util.Math.DeltaMath;
import com.Game.Util.Other.ItemAction;
import com.Game.Util.Other.ItemPlayerCalculation;
import com.Game.Util.Other.PlayerAction;

public enum FishingPreset {

    clownFish(35, 1.5f, 6.5f,
            1, ItemList.clownfish),
    blueFish(65, 3f, 8.5f,
            10, ItemList.bluefish),
    seaWeed(95, 2.5f, 7.5f,
            20, ItemList.seaWeed),
    gradedFish(7f, 15f,
            30, FishingPreset::gradedFish),

    rainbowFish(100, 3f, 8.5f,
            40, ItemList.rainbowFish);

    float xp, minTimer, maxTimer;
    int lvlReq;
    ItemStack fish;
    ItemPlayerCalculation action;

    FishingPreset(float xp, float minTimer, float maxTimer, int lvlReq, ItemList fish) {
        this.xp = xp;
        this.minTimer = minTimer;
        this.maxTimer = maxTimer;
        this.fish = new ItemStack(fish, 1);
        this.lvlReq = lvlReq;
    }

    FishingPreset(float xp, float minTimer, float maxTimer, int lvlReq, ItemStack fish) {
        this.xp = xp;
        this.minTimer = minTimer;
        this.maxTimer = maxTimer;
        this.fish = fish;
        this.lvlReq = lvlReq;
    }

    FishingPreset(float minTimer, float maxTimer, int lvlReq, ItemPlayerCalculation action) {
        this.xp = 0;
        this.minTimer = minTimer;
        this.maxTimer = maxTimer;
        this.lvlReq = lvlReq;
        this.action = action;
    }

    public int getTimer(Player player) {
        return (int) (DeltaMath.range(minTimer, maxTimer) * (1.0f - 0.005f * (player.getLevel(Skills.FISHING) - lvlReq)) * 1000);
    }

    public float getXp() {
        return xp;
    }

    public ItemStack getFish(Player player) {
        if (fish != null)
            return fish.clone();

        if (action != null)
            return action.calculate(player);

        return null;
    }

    public static ItemStack gradedFish(Player player) {
        double rand = Math.random();

        double lowest = 0.1 - 0.009f * Math.sqrt(100 - Math.min(player.skills.levels[Skills.FISHING], 100));
        double middle = (1 - lowest) / 9;

        if (rand < lowest) {
            player.addExperience(Skills.FISHING, 7000);
            return new ItemStack(ItemList.gradedFish, 1, 2);
        } else if (rand > 1 - middle) {
            player.addExperience(Skills.FISHING, 2000);
            return new ItemStack(ItemList.gradedFish, 1, 1);
        } else {
            player.addExperience(Skills.FISHING, 250);
            return new ItemStack(ItemList.gradedFish, 1, 0);
        }
    }
}
