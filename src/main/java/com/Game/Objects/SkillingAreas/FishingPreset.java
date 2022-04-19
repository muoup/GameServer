package com.Game.Objects.SkillingAreas;

import com.Game.Inventory.ItemList;
import com.Game.Entity.Player.Player;
import com.Game.Skills.Skills;
import com.Game.Util.Math.DeltaMath;

public enum FishingPreset {

    clownFish(35, 1.5f, 6.5f,
            1, ItemList.clownfish),
    blueFish(65, 3f, 8.5f,
            10, ItemList.bluefish),
    seaWeed(95, 2.5f, 7.5f,
            20, ItemList.seaWeed);

    float xp, minTimer, maxTimer;
    int lvlReq;
    ItemList fish;

    FishingPreset(float xp, float minTimer, float maxTimer, int lvlReq, ItemList fish) {
        this.xp = xp;
        this.minTimer = minTimer;
        this.maxTimer = maxTimer;
        this.fish = fish;
        this.lvlReq = lvlReq;
    }

    public int getTimer(Player player) {
        return (int) (DeltaMath.range(minTimer, maxTimer) * (1.0f - 0.005f * (player.getLevel(Skills.FISHING) - lvlReq)) * 1000);
    }

    public float getXp() {
        return xp;
    }
}
