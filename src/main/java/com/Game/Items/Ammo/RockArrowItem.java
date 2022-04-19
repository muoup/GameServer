package com.Game.Items.Ammo;

import com.Game.Entity.Entity;
import com.Game.Inventory.AccessoriesManager;
import com.Game.Inventory.Item;
import com.Game.Projectile.Projectile;
import com.Game.Projectile.RockArrow;
import com.Game.PseudoData.ImageIdentifier;
import com.Game.Util.Math.Vector2;
import com.Game.WorldManagement.World;

public class RockArrowItem extends Item {

    public RockArrowItem(int id, String name, String examineText, int worth, boolean stackable) {
        super(id, name, examineText, worth, stackable);
        this.equipStatus = AccessoriesManager.AMMO_SLOT;

        setImage("rock_arrow.png");
    }

    // TODO: Fix
    public Projectile createProjectile(Entity owner, Vector2 position, Vector2 direction, float damageMultiplier, float expMultiplier) {
        return new RockArrow(owner, direction, 2.5f * damageMultiplier, 2f);
    }
}
