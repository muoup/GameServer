package com.Game.WorldManagement.Worlds;

import com.Game.Entity.Enemy.Underground.BabySpider;
import com.Game.Entity.Enemy.Underground.BigSpider;
import com.Game.Entity.Enemy.Underground.Cockroach;
import com.Game.Objects.AreaTeleporter.CaveEntrance;
import com.Game.WorldManagement.World;

public class Underground extends World {
    public Underground(int id) {
        super(id);
        initImage("underground.png");
    }

    @Override
    public void worldCreation() {
        new BabySpider(this, 3902, 507);
        new BabySpider(this, 3639, 419);
        new BabySpider(this, 3639, 653);
        new BabySpider(this, 4115, 404);

        new BigSpider(this, 878, 2133);
        new BigSpider(this, 1215, 2448);
        new BigSpider(this, 934, 2448);
        new BigSpider(this, 1281, 2174);

        new Cockroach(this, 3652, 2874);
        new Cockroach(this, 3821, 3024);
        new Cockroach(this, 3706, 3185);
        new Cockroach(this, 3971, 3214);
        new Cockroach(this, 3837, 3365);

        new CaveEntrance(this, 726, 172, CaveEntrance.TeleType.caveExit);
    }

    public void update() {

    }
}
