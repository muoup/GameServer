package com.Game.Questing;

import com.Game.Entity.Player.Player;

import java.awt.*;

public class Quest {
    public static final Color[] statusColor = {
            Color.DARK_GRAY,
            Color.RED,
            Color.YELLOW,
            Color.BLUE,
            Color.GREEN
    };

    protected final int id;
    public String name;
    public String[] questClues = { "No clues, dev is probably lazy lmao." };

    public Quest(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public QuestStatus getStatus(Player player) {
        return QuestStatus.NO_DATA;
    }

    public boolean isComplete(Player player) {
        return getStatus(player) == QuestStatus.COMPLETE;
    }

    public void setData(Player player, int data) {
        player.questData.setData(id, data);
        if (isComplete(player))
            player.sendMessage("Congratulations! You have completed " + name + "!");
    }

    public void printClue(Player player, int data) {
        player.sendMessage(questClues[data]);
    }

    public String getClue(Player player) {
        return questClues[player.getQuestData(id)];
    }

    public String getColor(Player player) {
        if (getStatus(player).ordinal() > statusColor.length - 1)
            return "" + Color.ORANGE.getRGB();

        return "" + statusColor[getStatus(player).ordinal()].getRGB();
    }

    public String getName(Player player) {
        return name;
    }
}
