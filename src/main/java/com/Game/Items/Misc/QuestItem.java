package com.Game.Items.Misc;

import com.Game.Inventory.Item;
import com.Game.Util.Other.SpriteSheet;

public class QuestItem extends Item {
    public QuestItem(int id, int cellNum, String name, String examineText, int worth, boolean stackable) {
        super(id, name, examineText, worth, stackable);

        setImage(SpriteSheet.questItemSheet.getCell(cellNum, 0));
    }
}
