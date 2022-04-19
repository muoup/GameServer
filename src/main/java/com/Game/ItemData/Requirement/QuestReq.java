package com.Game.ItemData.Requirement;

import com.Game.Entity.Player.Player;

public class QuestReq extends Req {
    private int[] quest;

    public QuestReq(int[] quest) {
        this.quest = quest;
    }

    public boolean isValid(Player player, boolean print) {
        for (int id : quest) {
            if (!player.questData.isComplete(id))
                return false;
        }

        return true;
    }
}
