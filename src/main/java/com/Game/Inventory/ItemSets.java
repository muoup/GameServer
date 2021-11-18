package com.Game.Inventory;

public enum ItemSets {
    none(new int[0]),
    arrows(new int[]{
            3, // Arrow
            14 // Rock Arrow
    }),
    wood(new int[] {
            2,
            23,
            24,
            25,
            26,
            7
    });

    public int[] items;

    ItemSets(int[] items) {
        this.items = items;
    }
}
