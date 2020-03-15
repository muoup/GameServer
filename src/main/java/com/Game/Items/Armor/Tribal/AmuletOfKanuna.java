package com.Game.Items.Armor.Tribal;

import com.Game.Inventory.AccessoriesManager;
import com.Game.Inventory.Item;
import com.Game.PseudoData.ImageIdentifier;

public class AmuletOfKanuna extends Item {
    public AmuletOfKanuna(int id, String name, String examineText, ImageIdentifier image, int worth) {
        super(id, name, examineText, image, worth);
        this.equipStatus = AccessoriesManager.NECKLACE_SLOT;
        this.damageMulti = 0.06f;
    }
}
