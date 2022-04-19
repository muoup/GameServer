package com.Game.ItemData.Requirement;

import com.Game.Entity.Player.Player;
import com.Game.Skills.Skills;

public class LevelReq extends Req {
    private int skill, level;

    public LevelReq(int skill, int level) {
        this.skill = skill;
        this.level = level;
    }

    public boolean isValid(Player player, boolean print) {
        return player.skills.levels[skill] >= level;
    }

    public int getSkill() {
        return skill;
    }

    public int getLevel() {
        return level;
    }

    public String getReqMessage() {
        return "You require level " + level + " " + Skills.skillNames[skill] + " to complete this action.";
    }
}
