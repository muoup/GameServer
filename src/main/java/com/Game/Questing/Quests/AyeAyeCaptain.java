package com.Game.Questing.Quests;

import com.Game.Entity.Player.Player;
import com.Game.Questing.Quest;
import com.Game.Questing.QuestStatus;

public class AyeAyeCaptain extends Quest {
    public AyeAyeCaptain(int id, String name) {
        super(id, name);

        questClues = new String[] {
                "The captain would like to have a word with you.",
                "The captain needs about 10 logs, I'm not sure of what kind, but I hope he will take whatever I find.",
                "It seems that he needs some string for his sail. How much could he need? I think he said about 15 pieces.",
                "All that is left to do is ride his ship, I wonder where it will take me.",
                "The captain is happy to have his ship back in shape, and now I have a way to the tribal lands."
        };
    }

    public QuestStatus getStatus(Player player) {
        switch (player.getQuestData(id)) {
            case 0:
                return QuestStatus.INCOMPLETE;
            case 1:
            case 2:
            case 3:
                return QuestStatus.IN_PROGRESS;
            case 4:
                return QuestStatus.COMPLETE;
            default:
                return QuestStatus.BAD_DATA;
        }
    }
}
