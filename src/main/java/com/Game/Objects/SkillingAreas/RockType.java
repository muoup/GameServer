package com.Game.Objects.SkillingAreas;

import com.Game.Entity.Player.Player;
import com.Game.Inventory.ItemList;
import com.Game.ItemData.DropTable;
import com.Game.Skills.Skills;
import com.Game.Util.Math.DeltaMath;

public enum RockType {
    /**
     * Stone rocks cannot lose their material because they are only stone.
     * Because it only checks if a rock has 0 stone left, -1 will mean infinite
     */
    stone("empty_rock.png", 1, 10, 500, 2500, 0, 0,
            -1, -1, ItemList.stone),
    copper("copper_rock.png", 10, 20, 2500, 5500, 5000, 10000,
            2, 5, ItemList.copperOre),
    tin("tin_rock.png", 10, 20, 2500, 5500, 5000, 10000,
            2, 5, ItemList.tinOre);

    public DropTable drops;
    public String imageName;
    public float xp;
    public int minRocks, maxRocks, lvlReq;
    public long minTimer, maxTimer, minRespawn, maxRespawn;

    RockType(String imageName, int level, float xp, long minTime, long maxTime, long minRespawn, long maxRespawn, int minRocks, int maxRocks, ItemList rock) {
        this.imageName = imageName;
        this.lvlReq = level;
        this.xp = xp;
        this.minTimer = minTime;
        this.maxTimer = maxTime;
        this.minRespawn = minRespawn;
        this.maxRespawn = maxRespawn;
        this.minRocks = minRocks;
        this.maxRocks = maxRocks;

        drops = new DropTable();
        drops.addItem(rock, 1, 1);
    }

    /**
     * If the rock has a diverse DropTable, call an init method to add the items and their chances.
     *
     * @param initMethod
     */
    RockType(String imageName, int level, float xp, long minTime, long maxTime, long minRespawn, long maxRespawn, int minRocks, int maxRocks, Runnable initMethod) {
        this(imageName, level, xp, minTime, maxTime, minRespawn, maxRespawn, minRocks, maxRocks, ItemList.empty);

        initMethod.run();
    }

    public int getTimer(Player player) {
        return (int) (DeltaMath.range(minTimer, maxTimer) * (1.0f - 0.005f * (player.getLevel(Skills.FISHING) - lvlReq)));
    }
}
