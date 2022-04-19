package com.Game.Items.Weapon.Bows;


import com.Game.Inventory.AccessoriesManager;
import com.Game.Inventory.ItemSets;
import com.Game.ItemData.Requirement.ActionRequirement;
import com.Game.Items.Weapon.Weapon;
import com.Game.Projectile.Projectile;
import com.Game.Skills.Skills;
import com.Game.Util.Other.SpriteSheet;

/**
 * This item is not going to extend BowWeapon because it is not made from
 * logs, so it does not have an unstrung getCell, and also because it's damage
 * is not going to follow the tier system.
 */
public class CrystalBow extends Weapon {
    public CrystalBow(int id, String name, String examineText, int worth, boolean stackable) {
        super(id, name, examineText, worth, stackable);

        itemSet = ItemSets.arrows;
        equipStatus = AccessoriesManager.WEAPON_SLOT;

        requirement = ActionRequirement.skill(Skills.RANGED, 45);

        setImage(SpriteSheet.bowSheet.getCell(0, 3));
    }

    public void adaptShot(Projectile projectile) {
        projectile.multiShot(7.5, 128, 3);
    }
}
