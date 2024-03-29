package com.Game.ItemData.Requirement;

import com.Game.Entity.Player.Player;

import java.util.ArrayList;

public class ActionRequirement {
    private ArrayList<Req> requirements;

    private ActionRequirement() {
        requirements = new ArrayList<>();
    }

    public static ActionRequirement create() {
        return new ActionRequirement();
    }

    public static ActionRequirement skill(int skill, int level) {
        ActionRequirement req = new ActionRequirement();
        req.addLevelReqs(skill, level);
        return req;
    }

    public static ActionRequirement quest(int... quests) {
        ActionRequirement req = new ActionRequirement();
        req.addQuestReqs(quests);
        return req;
    }

    public static ActionRequirement none() {
        ActionRequirement req = new ActionRequirement();
        return req;
    }

    public void addLevelReqs(int... data) {
        if (data.length % 2 != 0) {
            System.err.println("LevelReq input is not valid!");
            return;
        }

        for (int i = 0; i < data.length; i += 2) {
            requirements.add(new LevelReq(data[i], data[i + 1]));
        }
    }

    public void addQuestReqs(int... data) {
        requirements.add(new QuestReq(data));
    }

    public boolean meetsRequirement(Player player) {
        for (Req requirement : requirements) {
            if (!requirement.isValid(player, false))
                return false;
        }

        return true;
    }

    public Req getRequirement(int index) {
        return requirements.get(index);
    }

    public int getLevelReq(int skill) {
        for (Req req : requirements) {
            if (req instanceof LevelReq) {
                LevelReq levelReq = (LevelReq) req;
                if (levelReq.getSkill() == skill)
                    return levelReq.getLevel();
            }
        }

        return -1;
    }

    public void sendNonComplete(Player player) {
        player.sendMessage("Sorry! You do not meet the requirements to complete this action: ");

        for (Req req : requirements) {
            if (!req.isValid(player, false))
                player.sendMessage(req.getReqMessage());
        }
    }
}
