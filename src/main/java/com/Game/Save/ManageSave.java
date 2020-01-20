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

package com.Game.Save;

import com.Game.Init.PlayerConnection;
import com.Game.security.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.text.DecimalFormat;
import java.util.Scanner;

public class ManageSave {
    public static DecimalFormat df = new DecimalFormat("0.00");

    /**
     * Used for creating a PlayerConnection from a saved file.
     * @param playername Username and name of player save file (without file type).
     * @param packet Player's packet to send player's information.
     * @return PlayerConnection loaded from a .psave file.
     */
    public static PlayerConnection loadPlayerData(String playername, DatagramPacket packet) {
        File getFile = new File("src/saves/" + playername.toLowerCase() + ".psave");
        PlayerConnection data = new PlayerConnection(packet.getAddress(), packet.getPort());
        Scanner scanner;
        try {
            scanner = new Scanner(getFile);
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + playername.toLowerCase() + ".psave");
            return null;
        }
        String[] parts;
        while (scanner.hasNext()) {
            parts = scanner.nextLine().split(" ");
            switch (parts[0]) {
                case "Login:":
                    StringBuilder usr = new StringBuilder();
                    for (int i = 1; i < parts.length; i++) {
                        usr.append(parts[i]);
                    }
                    String username = usr.toString();
                    data.setUsername(username);
                    break;
                case "Password:":
                    String pass = "";
                    pass = parts[1];
                    Password password;
                    if (Integer.parseInt(parts[2]) == 1) {
                        password = new Password(pass, true, true);
                    } else {
                        password = new Password(pass, true, false);
                    }
                    data.setPassword(password);
                    break;
                case "Pos:":
                    data.x = Integer.parseInt(parts[1]);
                    data.y = Integer.parseInt(parts[2]);
                    data.subWorld = Integer.parseInt(parts[3]);
                    break;
                case "Skills:":
                    for (int i = 1; i < parts.length; i++) {
                        data.skillXP[i - 1] = Float.parseFloat(parts[i]);
                    }
                    break;
                case "Inventory:":
                    String line = scanner.nextLine();
                    for (int i = 0; i < SaveSettings.inventoryAmount; i++) {
                        String[] cut = line.split(" ");
                        data.inventoryItems[i] =
                                new ItemMemory(Integer.parseInt(cut[1]), Integer.parseInt(cut[2]), Integer.parseInt(cut[3]));
                        line = scanner.nextLine();
                    }
                    break;

                case "Accessories:":
                    String aline = scanner.nextLine();
                    for (int i = 0; i < SaveSettings.accessoryAmount; i++) {
                        String[] cut = aline.split(" ");
                        data.accessoryItems[i] =
                                new ItemMemory(Integer.parseInt(cut[1]), Integer.parseInt(cut[2]), Integer.parseInt(cut[3]));
                        aline = scanner.nextLine();
                    }
                case "Accessory:":
                    for (int i = 1; i < parts.length; i++) {
                        if (i % 2 == 1)
                            data.accessoryItems[(i - 1) / 2].id = Integer.parseInt(parts[i]);
                        else
                            data.accessoryItems[(i - 1) / 2].amount = Integer.parseInt(parts[i]);
                    }
                    break;
                case "Quests:":
                    String qline = scanner.nextLine();
                    for (int i = 0; i < SaveSettings.questAmount; i++) {
                        String[] cut = qline.split(" ");
                        data.questSaves[i] = Integer.parseInt(cut[1]);
                    }
                    break;
            }
        }

        return data;
    }

    /**
     * Creates a blank PlayerConnection and saves it to /saves/
     * @param playername Name of new player
     * @param password Password of new player
     * @param packet Packet of new player for Server UDP
     * @return PlayerConnection that was just created/
     */
    public static PlayerConnection createPlayerData(String playername, String password, DatagramPacket packet) {
        PlayerConnection connection = new PlayerConnection(packet.getAddress(), packet.getPort());
        connection.setUsername(playername);
        connection.createPassword(password);
        connection.setPos(SaveSettings.startX, SaveSettings.startY, 0);
        savePlayerData(connection);

        return connection;
    }

    /**
     * Saves a PlayerConnection to a saved file. File should exist from createPlayerData()
     * @param data PlayerConnection to save to a file.
     * @return PlayerConnection that was sent it for some cleaner syntax.
     */
    public static PlayerConnection savePlayerData(PlayerConnection data) {
        File getFile = new File("src/saves/" + data.getUsername().toLowerCase() + ".psave");;
        PrintWriter writer;

        try {
            writer = new PrintWriter(getFile);
        } catch (FileNotFoundException e) {
            return null;
        }
        writer.println("Login: " + data.username);
        if (data.password.getState() == PasswordState.HASHED) {
            writer.println("Password: " + data.password.getPassword(new ManageSave()) + " " + "1");
        } else {
            writer.println("Password: " + data.password.getPassword(new VulnerableLogin(data.password)) + " " + "0"); //Must be passed an instance of VulnerableLogin to bypass security measures
        }
        writer.println("Pos: " +  data.x + " " + data.y + " " + data.subWorld);

        String skillsLine = "Skills:";

        for (float i : data.skillXP)
            skillsLine += " " + df.format(i);

        writer.println("\nInventory:");

        ItemMemory[] inventoryItems = data.inventoryItems;
        for (int i = 0; i < inventoryItems.length; i++) {
            ItemMemory mem = inventoryItems[i];
            writer.println(i + " " + mem.id + " " + mem.amount + " " + mem.data);
        }

        writer.println("\nAccessories:");

        ItemMemory[] accessoryItems = data.accessoryItems;
        for (int i = 0, accessoryItemsLength = accessoryItems.length; i < accessoryItemsLength; i++) {
            ItemMemory mem = accessoryItems[i];
            writer.println(i + " " + mem.id + " " + mem.amount + " " + mem.data);
        }

        writer.println("");

        writer.println(skillsLine);

        writer.println("\nQuests:");

        int[] quests = data.questSaves;

        for (int i = 0; i < quests.length; i++) {
            writer.println(i + " " + quests[i]);
        }

        writer.close();

        return data;
    }

    /**
     * Determines if the login given is correct. Tests login from /"username".psave
     * @param username Username to test against file
     * @param password Password to test against file
     * @return True or false dependant on if the login is correct.
     */

    public static boolean loginCorrect(String username, String password) {
        LoginHandler handler = new HashedLogin(password);
        Password toMatch = new Password("", false, false); //marked for garbage collection
        try {
            File saveFile = handler.findSave(username);
            toMatch = handler.readPassword(saveFile);
        } catch (IOException e) {
            System.err.println("File not found: " + username + ".psave");
        } finally {
            return handler.match(toMatch);
        }
    }

    /**
     * This method does not entirely work, this was just used to return the case sensitive username for the chatbox ingame
     * @param username File Username
     * @return How the username was capitalized when registered (not working) not worth working on rn however.
     */
    public static String getUsername(String username) {
        File file = new File("src/saves/" + username.toLowerCase() + ".psave");

        Scanner scanner;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            System.err.println("FILE NOT FOUND: " + username.toLowerCase() + ".psave");
            return null;
        }
        String[] loginLine = scanner.nextLine().split(" ");
        return loginLine[1].trim();
    }

    public static boolean usernameExists(String username) {
        File getFile = new File("src/saves/" + username.toLowerCase() + ".psave");
        return getFile.exists();
    }
}
