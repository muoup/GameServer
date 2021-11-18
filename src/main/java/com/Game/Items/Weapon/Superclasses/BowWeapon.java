package com.Game.Items.Weapon.Superclasses;

import com.Game.Inventory.AccessoriesManager;
import com.Game.Inventory.ItemList;
import com.Game.Inventory.ItemSets;
import com.Game.Inventory.ItemStack;
import com.Game.ItemData.Requirement.ActionRequirement;
import com.Game.Items.Weapon.Weapon;
import com.Game.Entity.Player.Player;
import com.Game.PseudoData.ImageIdentifier;
import com.Game.Skills.Skills;
import com.Game.Util.Other.SpriteSheet;

/**
 * The superclass for bow fishing. Extend if creating a new bow item.
 */
public class BowWeapon extends Weapon {
    ImageIdentifier unstrung;
    ImageIdentifier strung;
    private int tier;

    public BowWeapon(int id, int cell, int tier, String name, String examineText, int worth, boolean stackable) {
        super(id, name, examineText, worth, stackable);
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
        requirement = ActionRequirement.skill(0, tier);
        this.tier = tier;
    }

    public float getMultiplier(Player player) {
        return 1 + 0.0125f * player.getLevel(Skills.RANGED);
    }

    public void OnClick(Player player, int index) {
        if (player.inventory.getData(index) == 0) {
            if (combine(player, index, ItemList.bowString, 1, 1))
                player.skills.addExperience(Skills.FLETCHING, 30 * (1 + tier / 5));
        }
    }

    public void setData(ItemStack stack) {
        if (stack.getData() == 0) {
            stack.options.add("Craft Bow");
            stack.setImage(unstrung);
            stack.setEquipStatus(-1);
            stack.name = "Unstrung " + name;
            stack.setOptions("String " + stack.getName().replace("Unstrung ", ""));
        } else {
            stack.setImage(strung);
            stack.setEquipStatus(equipStatus);
            stack.setName(name);
        }
    }
}
