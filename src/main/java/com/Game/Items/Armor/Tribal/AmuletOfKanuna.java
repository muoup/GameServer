package com.Game.Items.Armor.Tribal;

import com.Game.Inventory.AccessoriesManager;
import com.Game.Inventory.Item;
import com.Game.ItemData.Requirement.ActionRequirement;
import com.Game.Skills.Skills;

public class AmuletOfKanuna extends Item {
    public AmuletOfKanuna(int id, String name, String examineText, int worth, boolean stackable) {
        super(id, name, examineText, worth, stackable);
        this.equipStatus = AccessoriesManager.NECKLACE_SLOT;
        this.damage = 0.06f;

        requirement = ActionRequirement.skill(Skills.RANGED, 10);
        setImage("necklacePower.png");
    }
}
