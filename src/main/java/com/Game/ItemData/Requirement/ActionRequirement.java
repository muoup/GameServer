package com.Game.ItemData.Requirement;

import java.util.ArrayList;

public class ActionRequirement {
    private ArrayList<Req> requirements;

    private ActionRequirement() {
        requirements = new ArrayList();
    }

    public static ActionRequirement create() {
        return new ActionRequirement();
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

    public void addQuestReq(int... data) {
        for (int quest : data) {
            // TODO: Quest requirement functionality
        }
    }
}
