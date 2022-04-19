package com.Game.Items.Ammo;

import com.Game.Entity.Entity;
import com.Game.Inventory.AccessoriesManager;
import com.Game.Inventory.Item;
import com.Game.Projectile.Arrow;
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

    public Projectile createProjectile(Entity owner, Vector2 direction, float damageMultiplier) {
        return new Arrow(owner, direction, 1.7f * damageMultiplier, 450f, 1000);
    }
}
