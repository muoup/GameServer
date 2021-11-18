package com.Game.Items.Weapon;

import com.Game.Inventory.AccessoriesManager;
import com.Game.Inventory.Item;
import com.Game.Inventory.ItemSets;
import com.Game.Inventory.ItemStack;
import com.Game.Entity.Player.Player;
import com.Game.Projectile.Projectile;
import com.Game.PseudoData.ImageIdentifier;
import com.Game.Util.Math.DeltaMath;
import com.Game.Util.Math.Vector2;

public class Weapon extends Item {
    protected Projectile projectile;
    protected ItemSets itemSet;
    protected float weaponDamage;

    public Weapon(int id, String name, String examineText, int worth, boolean stackable) {
        super(id, name, examineText, worth, stackable);

        this.itemSet = ItemSets.none;
        this.equipStatus = AccessoriesManager.WEAPON_SLOT;
    }

    public void useWeapon(Player player, Vector2 position, Vector2 direction) {
        shoot(player, itemSet, position, direction, 1f);
    }

    public float getMultiplier() {
        return 1f;
    }

    public void shoot(Player player, ItemSets acceptable, Vector2 position, Vector2 direction, float damage) {
        ItemStack stack = player.accessory.getSlot(AccessoriesManager.AMMO_SLOT);

        if (stack.getAmount() <= 0 || stack.getID() == 0)
            return;

        if (acceptable.items.length == 0) {
            Projectile projectile = stack.getItem().createProjectile(position, direction, dmgMultiplier(damage) * getMultiplier());
            player.inventory.addAmount(AccessoriesManager.AMMO_SLOT, -1);
            adaptShot(projectile);
            return;
        }

        for (int i : acceptable.items) {
            if (stack.getID() == i) {
                Projectile projectile = stack.getItem().createProjectile(position, direction, dmgMultiplier(damage) * getMultiplier());
                player.inventory.addAmount(AccessoriesManager.AMMO_SLOT, -1);
                adaptShot(projectile);
                break;
            }
        }
    }

    public void adaptShot(Projectile projectile) {

    }

    // TODO: Implement Weapon Accuracy
    public float dmgMultiplier(float damage) {
        return DeltaMath.range(weaponDamage * 0.9f, weaponDamage * 1.1f);
    }
}
