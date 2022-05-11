package com.Game.WorldManagement.Worlds;

import com.Game.Entity.Enemy.Chicken;
import com.Game.Entity.NPC.BirdWatcher;
import com.Game.Entity.NPC.BoatingCaptain;
import com.Game.Entity.NPC.Fisher;
import com.Game.Entity.NPC.SpawnWizard;
import com.Game.Objects.AreaTeleporter.CaveEntrance;
import com.Game.Objects.AreaTeleporter.InvisibleTeleporter;
import com.Game.Objects.AreaTeleporter.Ladder;
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
        new Tree(this, 2084, 2136, TreePreset.tree);
        new Tree(this, 2333, 2319, TreePreset.tree);
        new Tree(this, 2094, 2358, TreePreset.tree);
        new Tree(this, 1814, 2182, TreePreset.ash);
        new Tree(this, 1830, 2413, TreePreset.tree);
        new Tree(this, 2043, 2611, TreePreset.pine);
        new Tree(this, 2334, 2575, TreePreset.tree);
        new Tree(this, 2592, 2511, TreePreset.ash);
        new Tree(this, 2510, 2808, TreePreset.tree);
        new Tree(this, 2184, 2861, TreePreset.tree);
        new Tree(this, 1708, 2683, TreePreset.tree);
        new Tree(this, 2457, 3107, TreePreset.ash);
        new Tree(this, 2808, 2927, TreePreset.tree);
        new Tree(this, 1890, 3080, TreePreset.pine);
        new Tree(this, 2143, 3319, TreePreset.tree);
        new Tree(this, 1881, 2833, TreePreset.ash);
        new Tree(this, 1517, 2941, TreePreset.tree);
        new Tree(this, 1405, 2622, TreePreset.tree);
        new Tree(this, 1567, 2358, TreePreset.tree);
        new Tree(this, 1438, 2125, TreePreset.ash);
        new Tree(this, 1230, 2390, TreePreset.tree);
        new Tree(this, 1151, 2089, TreePreset.pine);
        new Tree(this, 1001, 2626, TreePreset.tree);
        new Tree(this, 1194, 2865, TreePreset.ash);
        new Tree(this, 1017, 3060, TreePreset.tree);
        new Tree(this, 1374, 3216, TreePreset.tree);
        new Tree(this, 1695, 3423, TreePreset.tree);
        new Tree(this, 1447, 3582, TreePreset.ash);
        new Tree(this, 1656, 3168, TreePreset.tree);
        new Tree(this, 3506, 2395, TreePreset.pine);
        new Tree(this, 3809, 2310, TreePreset.tree);
        new Tree(this, 3753, 2499, TreePreset.ash);
        new Tree(this, 3533, 2646, TreePreset.tree);
        new Tree(this, 3685, 2832, TreePreset.tree);
        new Tree(this, 3637, 3051, TreePreset.tree);
        new Tree(this, 3877, 2999, TreePreset.ash);
        new Tree(this, 3954, 2643, TreePreset.tree);
        new Tree(this, 4136, 2398, TreePreset.pine);
        new Tree(this, 4253, 2758, TreePreset.tree);
        new Tree(this, 4284, 3127, TreePreset.ash);
        new Tree(this, 4725, 2977, TreePreset.tree);
        new Tree(this, 4572, 2541, TreePreset.tree);
        new Tree(this, 4491, 2189, TreePreset.tree);
        new Tree(this, 5082, 2712, TreePreset.ash);
        new Tree(this, 4954, 2324, TreePreset.tree);

        // Rocks
        new MiningRock(this, 4042, 3679, RockType.stone);
        new MiningRock(this, 4409, 3663, RockType.stone);
        new MiningRock(this, 4232, 3743, RockType.stone);
        new MiningRock(this, 4401, 4063, RockType.copper);
        new MiningRock(this, 4393, 3926, RockType.copper);
        new MiningRock(this, 4013, 3952, RockType.tin);
        new MiningRock(this, 4236, 3880, RockType.tin);
        new MiningRock(this, 4173, 4056, RockType.tin);

        // Chickens
        new Chicken(this);
        new Chicken(this);
        new Chicken(this);
        new Chicken(this);
        new Chicken(this);
        new Chicken(this);

        new StorageChest(this, 1660, 1727);
        new StorageChest(this, 2783, 3693);
        new StorageChest(this, 5637, 4902);
        new StorageChest(this, 871, 4518);
        new StorageChest(this, 4921, 2060);

        // Smithing Area
        new Furnace(this, 2795, 4345);
        new Anvil(this, 3724, 4345);

        // Beach Area
        new FishingArea(this, 885, 5595, FishingPreset.clownFish);
        new FishingArea(this, 971, 5697, FishingPreset.clownFish);

        new FishingArea(this, 1179, 5939, FishingPreset.clownFish);
        new FishingArea(this, 1250, 6024, FishingPreset.clownFish);

        // Lake Area
        new FishingArea(this, 5306, 1678, FishingPreset.seaWeed);
        new FishingArea(this, 5103, 1604, FishingPreset.seaWeed);

        new FishingArea(this, 4994, 1670, FishingPreset.blueFish);
        new FishingArea(this, 5307, 1604, FishingPreset.blueFish);

        // NPCS
        new BirdWatcher(0, this ,2024, 2881);
        new BoatingCaptain(1, this, 841, 4303);
        new Fisher(2, this, 1301, 5497);
        new SpawnWizard(3, this, 1669, 1221);

        // Teleporter to Chess Dungeon
        new InvisibleTeleporter(this, 5507, 1643, WorldHandler.chessDungeon, 135, 125);

        // Teleporter to Underground
//        new CaveEntrance(this, 5150, 3534, CaveEntrance.TeleType.caveEntrance);

        // Teleporter to Tribal Lands
        new TribalTeleport(this, 500, 4100, WorldHandler.tropics, 1652, 3098);

        new Ladder(this, 3411, 921, WorldHandler.farmUnderground, 100, 100);
    }
}
