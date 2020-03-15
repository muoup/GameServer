package com.Game.Items.Armor.Rock;

import com.Game.Inventory.AccessoriesManager;
import com.Game.Inventory.Item;
import com.Game.Util.Other.SpriteSheet;

public class ArmorPiece extends Item {
    private ArmorType type;

    public ArmorPiece(int id, ArmorType type, int tier, String name, String examineText, int worth) {
        super(id, name, examineText, SpriteSheet.armorSheet.image((int) (tier / 10.0), type.ordinal()), worth);
        this.type = type;

        setTier(tier);

        switch (type) {
            case helmet:
                equipStatus = AccessoriesManager.HELMET_SLOT;
                break;
            case chestplate:
                equipStatus = AccessoriesManager.CHESTPLATE_SLOT;
                break;
            case leggings:
                equipStatus = AccessoriesManager.LEGGING_SLOT;
                break;
            case boots:
                equipStatus = AccessoriesManager.BOOT_SLOT;
                break;
            default:
                System.err.println(type.name() + " is not a supported armor piece");
                break;
        }
    }

    // TODO: Armor tier calculation.
    public void setTier(int tier) {
        float multiplier = type.getMultiplier();

        armor = tier * 0.125f + 10f;
    }
}

