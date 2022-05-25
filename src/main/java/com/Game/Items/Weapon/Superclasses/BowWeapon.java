package com.Game.Items.Weapon.Superclasses;

import com.Game.Inventory.AccessoriesManager;
import com.Game.Inventory.ItemList;
import com.Game.Inventory.ItemSets;
import com.Game.Inventory.ItemStack;
import com.Game.ItemData.Requirement.ActionRequirement;
import com.Game.Items.Weapon.Weapon;
import com.Game.Entity.Player.Player;
import com.Game.Objects.Utilities.Interfaces.CraftActionLoop;
import com.Game.PseudoData.ImageIdentifier;
import com.Game.Skills.Skills;
import com.Game.Util.Other.RCOption;
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
        this.equipStatus = -1;

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
        shotCooldown = 650 - 50 * tier / 10;

        this.tier = tier;
    }

    public void string(Player player, int index) {
        int level = player.getLevel(Skills.FLETCHING);

        if (level < tier) {
            player.sendMessage("You need a fletching level of " + tier + " to string this bow.");
            return;
        }

        CraftActionLoop loop = new CraftActionLoop(player, index, (long) (750 + 1000 / Math.sqrt(1 + level - tier)),
                Skills.FLETCHING, 30 * (1 + tier / 5),
                1, new ItemStack(ItemList.bowString, 1));

        player.createPlayerLoop(loop);
    }

    public void dataItemChange(ItemStack stack) {
        if (stack.getData() == 0) {
            stack.setImage(unstrung);
            stack.setOptions(
                    new RCOption("String " + stack.getName().replace("Unstrung ", ""), this::string));
        } else {
            stack.setImage(strung);
            stack.setEquipStatus(AccessoriesManager.WEAPON_SLOT);
            stack.setName(name.replace("Unstrung ", ""));
            stack.setOptions();
        }
    }
}
