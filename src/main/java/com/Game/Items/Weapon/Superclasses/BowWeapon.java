package com.Game.Items.Weapon.Superclasses;

import com.Game.GUI.Inventory.AccessoriesManager;
import com.Game.GUI.Skills.Skills;
import com.Game.Items.ItemList;
import com.Game.Items.ItemRequirement;
import com.Game.Items.ItemSets;
import com.Game.Items.ItemStack;
import com.Game.Items.Weapon.Weapon;
import com.Util.Other.SpriteSheet;

import java.awt.image.BufferedImage;

/**
 * The superclass for bow fishing. Extend if creating a new bow item.
 */
public class BowWeapon extends Weapon {
    BufferedImage unstrung;
    BufferedImage strung;

    public BowWeapon(int id, int cell, int tier, String name, String examineText, int maxStack, int worth) {
        super(id, new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB), name, examineText, maxStack, worth);
        this.itemSet = ItemSets.arrows;
        this.equipStatus = AccessoriesManager.WEAPON_SLOT;

        setImage(cell);
        setWeaponTier(tier);
    }

    public void setImage(int cell) {
        unstrung = SpriteSheet.bowSheet.getCell(cell, 0);
        strung = SpriteSheet.bowSheet.getCell(cell, 1);

        this.image = unstrung;
    }

    // TODO: Find an algorithm to make a damage and accuracy from weapon tier.

    /**
     * Sets the weapon's damage using its tier, if the weapon is special, this method
     * is not required.
     *
     * @param tier The tier of the weapon
     */
    public void setWeaponTier(int tier) {
        // For now this just 0.3 times the tier (including 1)
        weaponDamage = (float) (Math.floor(tier / 10) + 1) * 0.365f + 1.785f;
        requirement = new ItemRequirement(0, tier);
        expMultiplier = 1 + 0.125f * tier;
    }

    public float getMultiplier() {
        return 1 + 0.0125f * Skills.getLevel(Skills.RANGED);
    }

    public void OnClick(int index) {
        if (getData(index) == 0) {
            if (combine(index, ItemList.bowString, 1, 1))
                Skills.addExperience(Skills.FLETCHING, 30 * (1 + requirement.getLevel() / 5));
        }
    }

    public void setData(ItemStack stack) {
        if (stack.getData() == 0) {
            stack.options.add("Craft Bow");
            stack.setImage(unstrung);
            stack.setEquipStatus(-1);
            stack.name = "Unstrung " + name;
        } else {
            stack.setImage(strung);
            stack.setEquipStatus(equipStatus);
            stack.name = name;
        }
    }

    public String getOptionText(int i, int data, ItemStack stack) {
        return "String " + stack.name.replace("Unstrung ", "");
    }
}
