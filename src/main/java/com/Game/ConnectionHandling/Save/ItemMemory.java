/*
 * Copyright (c) 2019 Zachary Verlardi
 *
 * This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.Game.ConnectionHandling.Save;

/**
 * Used to mimic an ItemStack from the client. Holds an id and an amount
 * which is easy to send over the network in the form of a packet.
 */
public class ItemMemory {
    public int id;
    public int amount;
    public int data;

    public ItemMemory(int id, int amount) {
        this.id = id;
        this.amount = amount;
    }

    public ItemMemory(int id, int amount, int data) {
        this.id = id;
        this.amount = amount;
        this.data = data;
    }

    public String toString() {
        return "ITEM: " + id + ", " + amount;
    }

    public ItemMemory clone() {
        return new ItemMemory(id, amount);
    }
}
