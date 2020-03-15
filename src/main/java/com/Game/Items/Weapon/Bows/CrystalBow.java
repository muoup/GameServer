package com.Game.Items.Weapon.Bows;

import com.Game.GUI.Inventory.AccessoriesManager;
import com.Game.GUI.Skills.Skills;
import com.Game.Items.ItemRequirement;
import com.Game.Items.ItemSets;
import com.Game.Items.Weapon.Weapon;
import com.Game.Projectile.Projectile;
import com.Util.Other.SpriteSheet;

/**
 * This item is not going to extend BowWeapon because it is not made from
 * logs, so it does not have an unstrung image, and also because it's damage
 * is not going to follow the tier system.
 */
public class CrystalBow extends Weapon {
    public CrystalBow(int id, String name, String examineText, int maxStack, int worth) {
        super(id, SpriteSheet.bowSheet.getCell(0, 3), name, examineText, maxStack, worth);

        itemSet = ItemSets.arrows;
        equipStatus = AccessoriesManager.WEAPON_SLOT;

        weaponDamage = 1.25f;
        requirement = new ItemRequirement(Skills.RANGED, 45);
    }

    public void adaptShot(Projectile projectile) {
        projectile.multiShot(7.5, 128, 3);
    }
}
