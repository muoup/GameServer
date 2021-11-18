package com.Game.Questing.Quests;

import com.Game.Entity.Player.Player;
import com.Game.Questing.Quest;
import com.Game.Questing.QuestStatus;

public class BirdWatching extends Quest {
    public BirdWatching(int id, String name) {
        super(id, name);
        questClues = new String[] {
            "It looks like the Bird Watcher is in need of help, I should go help him out.",
            "The Bird Watcher talked about a bird in the tree. I wonder how I can get one for him.",
            "The Bird Watcher now has a pet bird thanks to your help."
        };
    }

    public QuestStatus getStatus(Player player) {
        switch (player.getQuestData(id)) {
            case 0:
                return QuestStatus.INCOMPLETE;
            case 1:
                return QuestStatus.IN_PROGRESS;
            case 2:
                return QuestStatus.COMPLETE;
            default:
                return QuestStatus.BAD_DATA;
        }
    }
}
