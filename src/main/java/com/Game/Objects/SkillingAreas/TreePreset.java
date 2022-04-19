package com.Game.Objects.SkillingAreas;

import com.Game.Inventory.ItemList;
import com.Game.Entity.Player.Player;
import com.Game.Skills.Skills;
import com.Game.Util.Math.DeltaMath;

public enum TreePreset {

    tree("tree.png", 10, 2500, 6000, 1000, 3000,
            2, 9, 1, ItemList.log),
    ash("ash_tree.png", 20, 2500, 6250, 1500, 3500,
            2, 9, 10, ItemList.ashLog),
    pine("pine_tree.png", 30, 2750, 6500, 2000, 4000,
            1, 12, 20, ItemList.pineLog),
    oak("oak_tree.png", 40, 2750, 6500, 2500, 4500,
            1, 12, 30, ItemList.oakLog),
    spruce("spruce_tree.png", 50, 3250, 7000, 3000, 5000,
            1, 7, 40, ItemList.spruceLog),
    maple("maple_tree.png", 60, 3250, 7000, 3500, 5500,
            1, 7, 50, ItemList.mapleLog);

    float xp;
    int minWood, maxWood, lvlReq;
    long minTimer, maxTimer, minRespawn, maxRespawn;
    ItemList wood;
    String imageName;

    TreePreset(String imageName, float xp, long minTimer, long maxTimer, long minRespawn, long maxRespawn, int minWood, int maxWood, int lvlReq, ItemList wood) {
        this.imageName = imageName;
        this.xp = xp;
        this.minTimer = minTimer;
        this.maxTimer = maxTimer;
        this.minWood = minWood;
        this.maxWood = maxWood;
        this.lvlReq = lvlReq;
        this.wood = wood;
    }

    public int getTimer(Player player) {
        return (int) (DeltaMath.range(minTimer, maxTimer) * (1.0f - 0.005f * (player.getLevel(Skills.FISHING) - lvlReq)));
    }

    public float getXp() {
        return xp;
    }

    public int getWoodAmount() {
        return Math.round(DeltaMath.range(minWood, maxWood));
    }
}
