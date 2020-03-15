package com.Game.Items.Misc;

import com.Game.Items.Item;
import com.Util.Other.SpriteSheet;

public class QuestItem extends Item {
    public QuestItem(int id, int cellNum, String name, String examineText, int maxStack, int worth) {
        super(id, SpriteSheet.questItemSheet.getCell(cellNum, 0), name, examineText, maxStack, worth);
    }
}
