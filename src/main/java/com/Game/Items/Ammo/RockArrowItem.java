package com.Game.Items.Ammo;

import com.Game.Inventory.AccessoriesManager;
import com.Game.Inventory.Item;
import com.Game.PseudoData.ImageIdentifier;

public class RockArrowItem extends Item {

    public RockArrowItem(int id, String name, String examineText, ImageIdentifier image, int worth) {
        super(id, name, examineText, image, worth);
        this.equipStatus = AccessoriesManager.AMMO_SLOT;
    }

    // TODO: Fix
//    public Projectile createProjectile(Vector2 position, Vector2 direction, float damageMultiplier, float expMultiplier) {
//        return new RockArrow(position, direction, 2.5f * damageMultiplier, 2f, expMultiplier, true);
//    }
}
