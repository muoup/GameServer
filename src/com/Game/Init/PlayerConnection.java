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

package com.Game.Init;

import com.Game.Save.*;

import java.net.InetAddress;

public class PlayerConnection {
    private InetAddress ipAddress;
    private int port;
    // TODO: Save arrays of information for saving
    public boolean connected = true;

    public float[] skillXP;
    public ItemMemory[] inventoryItems;
    public ItemMemory[] accessoryItems;
    private static ItemMemory[] invTemp;
    private static ItemMemory[] accTemp;
    public int x, y;
    public String username;
    /**
     * The Password, when set using {@link #setPassword(String)}.
     * Typically, when set through this method, the password will be hashed.
     */
    public String password;

    public static void init() {
        invTemp = new ItemMemory[SaveSettings.inventoryAmount];
        accTemp = new ItemMemory[SaveSettings.accessoryAmount];

        for (int i = 0; i < invTemp.length; i++) {
            invTemp[i] = new ItemMemory(0, 0);
        }
        for (int i = 0; i < accTemp.length; i++) {
            accTemp[i] = new ItemMemory(0, 0);
        }
    }

    public PlayerConnection() {
        this.ipAddress = null;
        this.port = -1;
        initSkills();
    }

    public PlayerConnection(InetAddress ipAddress, int port) {
        this.ipAddress = ipAddress;
        this.port = port;
        initSkills();
    }

    public void initSkills() {
        skillXP = new float[SaveSettings.skillAmount];
        this.x = 0;
        this.y = 0;
        this.username = "";
        this.password = "";
        this.inventoryItems = new ItemMemory[invTemp.length];
        this.accessoryItems = new ItemMemory[accTemp.length];

        for (int i = 0; i < invTemp.length; i++) {
            inventoryItems[i] = new ItemMemory(0, 0);
        }
        for (int i = 0; i < accTemp.length; i++) {
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

    public String getPassword() {
        return password;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void printData() {
        System.out.println("SKILLS:");
        for (int i = 0; i < skillXP.length; i++) {
            System.out.println(i + ": " + skillXP[i]);
        }
        System.out.println("USERNAME: " + username + " PASSWORD: " + password);
        System.out.println("POS: " + x + ", " + y);
    }


}
