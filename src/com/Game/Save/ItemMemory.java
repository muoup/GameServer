package com.Game.Save;

public class ItemMemory {
    public int id;
    public int amount;

    public ItemMemory(int id, int amount) {
        this.id = id;
        this.amount = amount;
    }

    public String toString() {
        return "ITEM: " + id + ", " + amount;
    }

    public ItemMemory clone() {
        return new ItemMemory(id, amount);
    }
}
