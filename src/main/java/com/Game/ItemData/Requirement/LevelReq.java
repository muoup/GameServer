package com.Game.ItemData.Requirement;

import com.Game.Player.Player;
import com.Game.Skills.SkillsManager;

public class LevelReq extends Req {
    private int skill, level;

    public LevelReq(int skill, int level) {

    }

    public boolean isValid(Player player, boolean print) {
        return player.skills.levels[skill] > level;
    }

    public String getReqMessage() {
        return "You require level " + level + " " + SkillsManager.skillNames[skill] + " to complete this action.";
    }
}
