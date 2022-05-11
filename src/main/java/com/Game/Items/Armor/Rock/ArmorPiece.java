package com.Game.Items.Armor.Rock;

import com.Game.Inventory.AccessoriesManager;
import com.Game.Inventory.Item;
import com.Game.Util.Other.SpriteSheet;

public class ArmorPiece extends Item {
    private ArmorType type;

    public ArmorPiece(int id, ArmorType type, int column, String name, String examineText, int worth, boolean stackable) {
        super(id, name, examineText, worth, stackable);
        this.type = type;

        setTier(column * 10 - ((column > 1) ? 10 : 0));
        setImage(SpriteSheet.armorSheet.getCell(column, type.ordinal()));

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

        armor = tier * 0.125f * multiplier;
    }
}

