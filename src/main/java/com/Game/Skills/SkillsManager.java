package com.Game.Skills;

import com.Game.ConnectionHandling.Client;
import com.Game.ConnectionHandling.Save.SaveSettings;
import com.Game.Player.Player;
import com.Game.PseudoData.ImageIdentifier;

public class SkillsManager {
    public static int RANGED = 0;
    public static int MELEE = 1;
    public static int FISHING = 2;
    public static int WOODCUTTING = 3;
    public static int FLETCHING = 4;
    public static int MINING = 5;
    public static int SMITHING = 6;

    public static final ImageIdentifier[] skillImages = {
            ImageIdentifier.singleImage("GUI/Skills/ranged.png"),
            ImageIdentifier.singleImage("GUI/Skills/melee.png"),
            ImageIdentifier.singleImage("GUI/Skills/fishing.png"),
            ImageIdentifier.singleImage("GUI/Skills/woodcutting.png"),
            ImageIdentifier.singleImage("GUI/Skills/fletching.png"),
            ImageIdentifier.singleImage("GUI/Skills/mining.png"),
            ImageIdentifier.singleImage("GUI/Skills/smithing.png"),
    };
    public static final String[] skillNames = {
            "Archery",
            "Attack",
            "Fishing",
            "Woodcutting",
            "Fletching",
            "Mining",
            "Smithing"
    };

    public int levels[];
    public int xp[];

    private Player player;

    public SkillsManager(Player player) {
        this.player = player;
        this.levels = new int[SaveSettings.skillAmount];
        this.xp = new int[SaveSettings.skillAmount];
    }

    public void setExperience(int skill, int amount) {
        xp[skill] = amount;
        deltaLevel(skill, 0, false);

        Client.sendSkill(player, skill, amount);
    }

    public void addExperience(int skill, int amount) {
        xp[skill] += amount;
        deltaLevel(skill, amount, true);

        Client.sendSkill(player, skill, amount);
    }

    private void deltaLevel(int skill, int added, boolean levelUp) {
        int initialLevel = expToLevel(xp[skill] - added);
        int level = expToLevel(xp[skill]);
        levels[skill] = level;

        if (initialLevel < level && levelUp) {
            Client.sendMessage(player, "Congratulations, you have reached level " + levels[skill]);
        }
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
