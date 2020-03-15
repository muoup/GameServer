package com.Game.Items.Weapon.Superclasses;

import com.Game.GUI.Inventory.AccessoriesManager;
import com.Game.GUI.Skills.Skills;
import com.Game.Items.ItemRequirement;
import com.Game.Items.ItemSets;
import com.Game.Items.Weapon.Weapon;
import com.Game.Projectile.Dagger;
import com.Util.Math.Vector2;
import com.Util.Other.SpriteSheet;

/**
 * The superclass for bow fishing. Extend if creating a new bow item.
 */
public class DaggerWeapon extends Weapon {
    public DaggerWeapon(int id, int cellID, int tier, String name, String examineText, int maxStack, int worth) {
        super(id, SpriteSheet.meleeSheet.getCell(cellID, 0), name, examineText, maxStack, worth);

        this.itemSet = ItemSets.arrows;
        this.equipStatus = AccessoriesManager.WEAPON_SLOT;

        setWeaponTier(tier);
    }

    public void setImage(int cell) {
        this.image = SpriteSheet.meleeSheet.getCell(cell, 0);
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
        weaponDamage = (float) (Math.floor(tier / 10) + 1) * 0.350f + 1.800f;
        requirement = new ItemRequirement(Skills.MELEE, tier);
        expMultiplier = 1 + 0.125f * tier;
    }

    public float getMultiplier() {
        return 1 + 0.0115f * Skills.getLevel(Skills.RANGED);
    }

    public void shoot(ItemSets acceptable, Vector2 position, Vector2 direction, float damage, float expMultiplier) {
        new Dagger(position, direction, dmgMultiplier(damage) * getMultiplier(), 2.5f, expMultiplier, true);
    }
}
