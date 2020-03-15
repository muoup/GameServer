package com.Game.Items.Armor.Rock;

public enum ArmorType {
    helmet(1), chestplate(2.95f), leggings(2.1f), boots(0.75f);

    float armorMultiplier;

    ArmorType(float armorMultiplier) {
        this.armorMultiplier = armorMultiplier;
    }

    public float getMultiplier() {
        return armorMultiplier;
    }
}
