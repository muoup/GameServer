package com.Game.WorldManagement.Worlds;

import com.Game.Entity.Enemy.Underground.BabySpider;
import com.Game.Entity.Enemy.Underground.BigSpider;
import com.Game.Entity.Enemy.Underground.Cockroach;
import com.Game.Items.Misc.BugRelic;
import com.Game.Objects.AreaTeleporter.CaveEntrance;
import com.Game.Objects.Special.BugRelicHolder;
import com.Game.Objects.Utilities.StorageChest;
import com.Game.WorldManagement.World;

public class Underground extends World {
    public Underground(int id) {
        super(id);
        initImage("underground.png");
    }

    @Override
    public void worldCreation() {
        repeat(8, (i) -> new BabySpider(this));

        repeat(4, (i) -> new BigSpider(this));

        repeat(2, (i) -> new Cockroach(this));

        new CaveEntrance(this, 487, 353, CaveEntrance.TeleType.caveExit);

        new StorageChest(this, 808, 762);

        new BugRelicHolder(this, 737, 4306);
    }
}
