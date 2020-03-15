package com.Game.ItemData.Requirement;

import com.Game.Player.Player;

public class QuestReq extends Req {
    private int quest;

    public QuestReq(int quest) {
        this.quest = quest;
    }

    public boolean isValid(Player player, boolean print) {
        return true; // TODO: change when quests are implemented server-side
    }
}
