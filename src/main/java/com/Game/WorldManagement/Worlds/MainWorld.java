package com.Game.WorldManagement.Worlds;

import com.Game.Objects.AreaTeleporter.CaveEntrance;
import com.Game.Objects.AreaTeleporter.InvisibleTeleporter;
import com.Game.Objects.AreaTeleporter.TribalTeleport;
import com.Game.Objects.SkillingAreas.*;
import com.Game.Objects.Utilities.Anvil;
import com.Game.Objects.Utilities.Furnace;
import com.Game.Objects.Utilities.StorageChest;
import com.Game.WorldManagement.World;
import com.Game.WorldManagement.WorldHandler;

public class MainWorld extends World {

    public MainWorld(int id) {
        super(id);

        initImage("worldTest.png");
    }

    public void worldCreation() {
        // Forest Area
        new Tree(this, 1655, 1875, TreePreset.tree);
        new Tree(this, 1870, 1805, TreePreset.tree);
        new Tree(this, 2057, 1834, TreePreset.tree);
        new Tree(this, 1975, 1982, TreePreset.tree);
        new Tree(this, 1788, 2072, TreePreset.tree);
        new Tree(this, 1587, 2126, TreePreset.tree);
        new Tree(this, 2225, 2049, TreePreset.tree);
        new Tree(this, 2070, 2163, TreePreset.tree);
        new Tree(this, 1649, 2349, TreePreset.tree);
        new Tree(this, 2273, 1869, TreePreset.tree);
        new Tree(this, 2191, 1728, TreePreset.tree);
        new Tree(this, 1984, 1704, TreePreset.tree);
        new Tree(this, 1726, 1744, TreePreset.tree);
        new Tree(this, 1509, 1976, TreePreset.tree);
        new Tree(this, 1400, 2237, TreePreset.tree);
        new Tree(this, 2082, 2378, TreePreset.tree);
        new Tree(this, 2393, 1792, TreePreset.tree);
        new Tree(this, 2448, 2030, TreePreset.tree);
        new Tree(this, 1854, 2309, TreePreset.ash);
        new Tree(this, 2316, 2244, TreePreset.pine);
        new Tree(this, 2053, 2581, TreePreset.oakTree);
        new Tree(this, 1690, 2565, TreePreset.spruce);
        new Tree(this, 2386, 2493, TreePreset.mapleTree);

        // Rocks
        new MiningRock(this, 3496, 4520, RockType.stone);
        new MiningRock(this, 3668, 4473, RockType.stone);
        new MiningRock(this, 3797, 4602, RockType.stone);
        new MiningRock(this, 3656, 4692, RockType.copper);
        new MiningRock(this, 3444, 4811, RockType.tin);
//        new MiningRock(3759, 4832);

        new Furnace(this, 5042, 4382);
        new Anvil(this, 5042, 4658);

        // Chickens
        //new Chicken(4394, 2874);
        //new Chicken(4223, 2764);
        //new Chicken(4223, 2993);
        //new Chicken(4532, 2993);
        //new Chicken(4532, 2696);

        new StorageChest(this, 3038, 3395);

        // Beach Area
        new FishingArea(this, 885, 5595, FishingPreset.clownFish);
        new FishingArea(this, 971, 5697, FishingPreset.clownFish);

        new FishingArea(this, 1179, 5939, FishingPreset.blueFish);
        new FishingArea(this, 1250, 6024, FishingPreset.blueFish);

        new FishingArea(this, 5306, 1678, FishingPreset.seaWeed);
        new FishingArea(this, 5103, 1604, FishingPreset.seaWeed);

        new CaveEntrance(this, 5150, 3534, CaveEntrance.TeleType.caveEntrance); // Teleporter to underground area.

        // Quest NPC
        //new BirdWatcher(2024, 2881);

        // Boatsman
        //new BoatingCaptain(2, 841, 4303);

        // Fisher
        //new Fisher(1301, 5497);

        // Teleporter to Chess Dungeon
        new InvisibleTeleporter(this, 5507, 1643, WorldHandler.chessDungeon, 135, 125);

        // Teleporter to Tribal Lands
        new TribalTeleport(this, 500, 4100, WorldHandler.tropics, 1652, 3098);
    }
}
