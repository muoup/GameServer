package com.Game.Items.Ammo;

import com.Game.Inventory.AccessoriesManager;
import com.Game.Inventory.Item;
import com.Game.PseudoData.ImageIdentifier;

public class ArrowItem extends Item {

    public ArrowItem(int id, String name, String examineText, ImageIdentifier image, int worth) {
        super(id, name, examineText, image, worth);
        this.equipStatus = AccessoriesManager.AMMO_SLOT;
    }

    // TODO: Fix
//    public Projectile createProjectile(Vector2 position, Vector2 direction, float damageMultiplier, float expMultiplier) {
//        return new Arrow(position, direction, 1.7f * damageMultiplier, 2.5f, expMultiplier, true);
//    }
}
