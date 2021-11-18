package com.Game.Objects.SkillingAreas;

import com.Game.Inventory.ItemList;
import com.Game.Entity.Player.Player;
import com.Game.Skills.Skills;
import com.Game.Util.Math.DeltaMath;

public enum TreePreset {

    tree("tree.png", 40, 2.5f, 6f,
            2, 9, 1, ItemList.log),
    ash("ash_tree.png", 60, 2.5f, 6.25f,
            2, 9, 10, ItemList.ashLog),
    pine("pine_tree.png", 80, 2.75f, 6.5f,
            1, 12, 20, ItemList.pineLog),
    oakTree("oak_tree.png", 120, 2.75f, 6.5f,
            1, 12, 30, ItemList.oakLog),
    spruce("spruce_tree.png", 160, 3.25f, 7f,
            1, 7, 40, ItemList.spruceLog),
    mapleTree("maple_tree.png", 200, 3.25f, 7f,
            1, 7, 50, ItemList.mapleLog);

    float xp, minTimer, maxTimer;
    int minWood, maxWood, lvlReq;
    ItemList wood;
    String imageName;

    TreePreset(String imageName, float xp, float minTimer, float maxTimer, int minWood, int maxWood, int lvlReq, ItemList wood) {
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
        return (int) (DeltaMath.range(minTimer, maxTimer) * (1.0f - 0.005f * (player.getLevel(Skills.FISHING) - lvlReq)) * 1000);
    }

    public float getXp() {
        return xp;
    }

    public int getWoodAmount() {
        return Math.round(DeltaMath.range(minWood, maxWood));
    }
}
