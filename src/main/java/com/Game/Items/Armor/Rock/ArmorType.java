package com.Game.Items.Armor.Rock;

public enum ArmorType {
    helmet(3), chestplate(5), leggings(4), boots(2);

    float armorMultiplier;

    ArmorType(float armorMultiplier) {
        this.armorMultiplier = armorMultiplier;
    }

    public float getMultiplier() {
        return armorMultiplier;
    }
}
