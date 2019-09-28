package com.Game.Save;

/**
 * Used to mimic an ItemStack from the client. Holds an id and an amount
 * which is easy to send over the network in the form of a packet.
 */
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
