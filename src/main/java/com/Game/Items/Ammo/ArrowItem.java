package com.Game.Items.Ammo;

import com.Game.Inventory.AccessoriesManager;
import com.Game.Inventory.Item;
import com.Game.Projectile.Projectile;
import com.Game.PseudoData.ImageIdentifier;
import com.Game.Util.Math.Vector2;

public class ArrowItem extends Item {

    public ArrowItem(int id, String name, String examineText, int worth, boolean stackable) {
        super(id, name, examineText, worth, stackable);
        this.equipStatus = AccessoriesManager.AMMO_SLOT;
        this.stackable = stackable;

        setImage("arrow.png");
    }

    // TODO: Fix
//    public Projectile createProjectile(Vector2 position, Vector2 direction, float damageMultiplier, float expMultiplier) {
//        return new Arrow(position, direction, 1.7f * damageMultiplier, 2.5f, expMultiplier, true);
//    }
}
