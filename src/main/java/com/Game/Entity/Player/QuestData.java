package com.Game.Entity.Player;

import com.Game.ConnectionHandling.Save.SaveSettings;
import com.Game.Questing.Quest;
import com.Game.Questing.QuestList;

public class QuestData {
    private int[] questData;
    private Player player;

    public QuestData(Player player) {
        this.player = player;
        questData = new int[SaveSettings.questAmount];
    }

    public void importData(int[] preData) {
        // In case a new quest is added, preData may not have the same structure as the required questData.
        for (int i = 0; i < preData.length; i++) {
            questData[i] = preData[i];
        }
    }

    public int getData(int quest) {
        return questData[quest];
    }

    public void changeData(int quest, int set) {
        QuestList.getIndex(quest).setData(player, set);
    }

    public void setData(int quest, int set) {
        this.questData[quest] = set;
    }

    public String getInfoForPacket(int id) {
        Quest quest = QuestList.getIndex(id);
        return quest.getName(player) + ":" + quest.getColor(player) + ":" + quest.getClue(player);
    }

    public int[] getDataArray() {
        return questData;
    }

    public boolean isComplete(int id) {
        return QuestList.getIndex(id).isComplete(player);
    }
}
