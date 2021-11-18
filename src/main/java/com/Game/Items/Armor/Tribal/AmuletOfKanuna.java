package com.Game.Items.Armor.Tribal;

import com.Game.Inventory.AccessoriesManager;
import com.Game.Inventory.Item;
import com.Game.PseudoData.ImageIdentifier;

public class AmuletOfKanuna extends Item {
    public AmuletOfKanuna(int id, String name, String examineText, int worth, boolean stackable) {
        super(id, name, examineText, worth, stackable);
        this.equipStatus = AccessoriesManager.NECKLACE_SLOT;
        this.damageMulti = 0.06f;

        setImage("necklacePower.png");
    }
}
