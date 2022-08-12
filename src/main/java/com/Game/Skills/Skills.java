package com.Game.Skills;

import com.Game.ConnectionHandling.Client;
import com.Game.ConnectionHandling.Save.SaveSettings;
import com.Game.Entity.Player.Player;
import com.Game.PseudoData.ImageIdentifier;
import com.Game.Util.Other.Settings;

public class Skills {
    public static int RANGED = 0;
    public static int MELEE = 1;
    public static int LIFEPOINTS = 2;
    public static int FISHING = 3;
    public static int WOODCUTTING = 4;
    public static int FLETCHING = 5;
    public static int MINING = 6;
    public static int SMITHING = 7;

    public static final ImageIdentifier[] skillImages = {
            ImageIdentifier.singleImage("GUI/Skills/ranged.png"),
            ImageIdentifier.singleImage("GUI/Skills/melee.png"),
            ImageIdentifier.singleImage("GUI/Skills/lifepoints.png"),
            ImageIdentifier.singleImage("GUI/Skills/fishing.png"),
            ImageIdentifier.singleImage("GUI/Skills/woodcutting.png"),
            ImageIdentifier.singleImage("GUI/Skills/fletching.png"),
            ImageIdentifier.singleImage("GUI/Skills/mining.png"),
            ImageIdentifier.singleImage("GUI/Skills/smithing.png"),
    };

    public static final String[] skillNames = {
            "Archery",
            "Attack",
            "Life Points",
            "Fishing",
            "Woodcutting",
            "Fletching",
            "Mining",
            "Smithing"
    };

    public int levels[];
    public float xp[];

    private Player player;

    public Skills(Player player) {
        this.player = player;
        this.levels = new int[SaveSettings.skillAmount];
        this.xp = new float[SaveSettings.skillAmount];
    }

    public void setExperience(int skill, float amount) {
        xp[skill] = amount;
        deltaLevel(skill, 0, false, false);
    }

    public void addExperience(int skill, float amount) {
        if (amount == 0)
            return;

        amount *= Settings.xpMultiplier;
        xp[skill] += amount;
        deltaLevel(skill, amount, true, true);
    }

    public void deltaLevel(int skill, float added, boolean levelUp, boolean popup) {
        int initialLevel = expToLevel(xp[skill] - added);
        int level = expToLevel(xp[skill]);
        levels[skill] = level;

        Client.sendSkill(player, skill, popup);

        if (initialLevel < level && levelUp) {
            Client.sendMessage(player, "Congratulations, you have reached level " + levels[skill]);
        }

        if (skill == Skills.LIFEPOINTS)
            player.setMaxHealth(50 * levels[LIFEPOINTS]);
    }

    public static int expToLevel(float exp) {
        int result = 0;
        int i = 0;

        while (result - 1 < exp) {
            i++;
            result += (int) (Math.pow(1.085, i - 1) * 150);
        }

        return Math.max(i, 1);
    }

    public static int levelToExp(int lvl) {
        int result = 0;

        for (int i = 2; i <= lvl; i++) {
            result = (int) ((Math.pow(1.085, i - 2) * 150) + result);
        }

        return result;
    }
}
