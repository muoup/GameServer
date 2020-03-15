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

package com.Game.Player;

import com.Game.ConnectionHandling.Client;
import com.Game.ConnectionHandling.Save.ItemMemory;
import com.Game.ConnectionHandling.Save.SaveSettings;
import com.Game.ConnectionHandling.security.Obfuscator;
import com.Game.ConnectionHandling.security.Password;
import com.Game.Inventory.InventoryManager;
import com.Game.Skills.SkillsManager;
import com.Game.Util.Math.Vector2;

import java.net.InetAddress;
import java.util.ArrayList;

public class Player {

    // Connection Settings
    private InetAddress ipAddress;
    private int port;
    public int connected = 0;

    // Item Inventories Section
    public InventoryManager inventory;
    public ItemMemory[] accessoryItems;
    public ArrayList<ItemMemory> bankItems;

    // Stats
    public int[] questSaves;
    public SkillsManager skills;
    public int maxHealth = 100;

    // Real-Time Data
    public int health = 100;
    public float timer = 1.0f;

    // Player Properties
    public String username;
    public Vector2 pos;
    public int subWorld;

    /**
     * The Password, when set using {@link #setPassword(Password)}.
     * Typically, when set through this method, the password will be hashed.
     */
    public Password password;

    public Player(InetAddress ipAddress, int port) {
        this.ipAddress = ipAddress;
        this.port = port;
        bankItems = new ArrayList();
        initSkills();
    }

    public void initSkills() {
        this.skills = new SkillsManager(this);
        this.pos = Vector2.zero();
        this.username = "";
        this.password = new Password("", false, false);
        this.inventory = new InventoryManager();
        this.accessoryItems = new ItemMemory[SaveSettings.accessoryAmount];
        this.questSaves = new int[SaveSettings.questAmount];
        this.subWorld = 0;

        for (int i = 0; i < SaveSettings.accessoryAmount; i++) {
            accessoryItems[i] = new ItemMemory(0, 0);
        }
    }

    public InetAddress getIpAddress() {
        return ipAddress;
    }

    public int getPort() {
        return port;
    }

    public String toString() {
        return "Address: " + ipAddress + "\nPort: " + port + "\nName: " + username;
    }

    public String getUsername() {
        return username;
    }

    public Password getPassword() {
        return password;
    }

    public float getX() {
        return pos.x;
    }

    public float getY() {
        return pos.y;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(Password password) {
        this.password = password;
    }

    public void createPassword(String string) {
        Obfuscator obfuscator = new Obfuscator();
        password = new Password(obfuscator.hashPassword(string), true, true);
    }

    public void setPos(int x, int y, int subWorld) {
        pos = new Vector2(x, y);
        this.subWorld = subWorld;
    }

    public void sendMessage(String message) {
        Client.sendMessage(this, message);
    }
}
