package com.Game.Questing;

import com.Game.Questing.Quests.*;
public enum QuestList {
    BirdWatching(new BirdWatching(0, "Bird Watching")),
    CaptainOrders(new AyeAyeCaptain(1, "Captain's Orders"));

    public Quest quest;

    QuestList(Quest quest) {
        this.quest = quest;
    }

    public static Quest getIndex(int index) {
        return values()[index].quest;
    }
}
