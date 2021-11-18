package com.Game.Objects.SkillingAreas;

import com.Game.Inventory.ItemList;
import com.Game.ItemData.DropTable;

public enum RockType {
    /**
     * Stone rocks cannot lose their material because they are only stone.
     * Because it only checks if a rock has 0 stone left, -1 will mean infinite
     */
    stone("empty_rock.png", 1, 5, 0.5f, 2.5f, -1, -1, ItemList.stone),
    copper("copper_rock.png", 10, 45, 2.5f, 5.5f, 2, 5, ItemList.copperOre),
    tin("tin_rock.png", 10, 45, 2.5f, 5.5f, 2, 5, ItemList.tinOre);

    public DropTable drops;
    public String imageName;
    public float minTime, maxTime, xp;
    public int minRocks, maxRocks, level;

    RockType(String imageName, int level, float xp, float minTime, float maxTime, int minRocks, int maxRocks, ItemList rock) {
        this.imageName = imageName;
        this.level = level;
        this.xp = xp;
        this.minTime = minTime;
        this.maxTime = maxTime;
        this.minRocks = minRocks;
        this.maxRocks = maxRocks;

        drops = new DropTable();
        drops.add(rock, 1, 1);
    }

    /**
     * If the rock has a diverse DropTable, call an init method to add the items and their chances.
     *
     * @param initMethod
     */
    RockType(String imageName, int level, float xp, float minTime, float maxTime, int minRocks, int maxRocks, Runnable initMethod) {
        this.imageName = imageName;
        this.level = level;
        this.xp = xp;
        this.minTime = minTime;
        this.maxTime = maxTime;
        this.minRocks = minRocks;
        this.maxRocks = maxRocks;

        initMethod.run();
    }
}
