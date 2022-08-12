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

import com.Game.Inventory.ItemStack;
import com.Game.Entity.Player.Player;
import com.Game.ConnectionHandling.security.*;
import com.Game.WorldManagement.WorldHandler;

import java.io.*;
import java.net.DatagramPacket;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

//for testing purposes only

//end testing imports

public class ManageSave {
    public static DecimalFormat df = new DecimalFormat("0.00");

    /**
     * Used for creating a PlayerConnection from a saved file.
     * @param playername Username and name of player save file (without file type).
     * @param packet Player's packet to send player's information.
     * @return PlayerConnection loaded from a .psave file.
     */
    public static Player loadPlayerData(String playername, DatagramPacket packet) {
        File getFile = new File("src/saves/" + playername.toLowerCase() + ".psave");
        Player data = new Player(packet.getAddress(), packet.getPort());
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
                    String pass = parts[1];
                    Password password;
                    if (Integer.parseInt(parts[2]) == 1) {
                        password = new Password(pass, true, true);
                    } else {
                        password = new Password(pass, true, false);
                    }
                    data.setPassword(password);
                    break;
                case "Pos:":
                    data.setWorld(WorldHandler.getWorld(Integer.parseInt(parts[3])));
                    data.setPos(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
                    break;
                case "Health:":
                    data.setHealth(Float.parseFloat(parts[1]));
                    break;
                case "Skills:":
                    for (int i = 1; i < parts.length; i++) {
                        data.setExperience(i - 1, Float.parseFloat(parts[i]));
                    }
                    break;
                case "Inventory:":
                    String line = scanner.nextLine();
                    for (int i = 0; i < SaveSettings.inventoryAmount; i++) {
                        String[] cut = line.split(" ");
                        if (!Objects.equals(cut[1], "0"))
                            data.inventory.setItem(i, interpretItemStack(cut));
                        line = scanner.nextLine();
                    }
                    break;

                case "Accessories:":
                    String aline = scanner.nextLine();
                    for (int i = 0; i < SaveSettings.accessoryAmount; i++) {
                        String[] cut = aline.split(" ");
                        data.accessory.setSlot(i, interpretItemStack(cut));
                        aline = scanner.nextLine();
                    }
                case "Accessory:":
                    for (int i = 1; i < parts.length; i++) {
                        if (i % 2 == 1)
                            data.accessory.accessories[(i - 1) / 2].id = Integer.parseInt(parts[i]);
                        else
                            data.accessory.accessories[(i - 1) / 2].amount = Integer.parseInt(parts[i]);
                    }
                    break;
                case "Quests:":
                    for (int i = 0; i < SaveSettings.questAmount; i++) {
                        String qline = scanner.nextLine();
                        if (qline.trim().equals(""))
                            break;
                        String[] cut = qline.split(" ");
                        data.setQuestData(i, Integer.parseInt(cut[1]));
                    }
                    break;
                case "Bank:":
                    String bline;
                    do {
                        bline = scanner.nextLine();
                        String[] cut = bline.split(" ");
                        data.addBankItem(new ItemStack(Integer.parseInt(cut[0]), Integer.parseInt(cut[1]), Integer.parseInt(cut[2])));
                    } while (bline.trim() != "" && scanner.hasNext());
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
    public static Player createPlayerData(String playername, String password, DatagramPacket packet) {
        Player connection = new Player(packet.getAddress(), packet.getPort());
        connection.setUsername(playername);
        connection.createPassword(password);
        connection.teleport(SaveSettings.startX, SaveSettings.startY, 0);
        savePlayerData(connection);

        return connection;
    }

    /**
     * Saves a PlayerConnection to a saved file. File should exist from createPlayerData()
     * @param data PlayerConnection to save to a file.
     * @return PlayerConnection that was sent it for some cleaner syntax.
     */
    public static Player savePlayerData(Player data) {
        if (data == null)
            return null;

        File getFile = new File("src/saves/" + data.getUsername().toLowerCase() + ".psave");
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
        writer.println("Pos: " + data.getX() + " " + data.getY() + " " + data.getWorld().id);
        writer.println("Health: " + data.health);

        String skillsLine = "Skills:";

        for (float i : data.skills.xp)
            skillsLine += " " + df.format(i);

        writer.println("\nInventory:");

        ItemStack[] inventoryItems = data.inventory.inventory;
        for (int i = 0; i < inventoryItems.length; i++) {
            ItemStack item = inventoryItems[i];
            writer.println(i + " " + item);
        }

        writer.println("\nAccessories:");

        ItemStack[] accessoryItems = data.accessory.accessories;
        for (int i = 0; i < accessoryItems.length; i++) {
            ItemStack item = accessoryItems[i];
            writer.println(i + " " + item);
        }

        writer.println("");

        writer.println(skillsLine);

        writer.println("\nQuests:");

        int[] quests = data.questData.getDataArray();

        for (int i = 0; i < quests.length; i++) {
            writer.println(i + " " + quests[i]);
        }

        ArrayList<ItemStack> bankItems = data.banking.getItems();
        if (bankItems.size() > 0) {
            writer.println("\nBank:");

            for (ItemStack item : bankItems) {
                writer.println(item.id + " " + item.amount + " " + item.data + " " + false);
            }
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
        }
        return handler.match(toMatch);
    }

    /**
     * This method does not entirely work, this was just used to return the case sensitive username for the chatbox ingame
     * @param username File Username
     * @return How the username was capitalized when registered (not working) not worth working on rn however.
     */
    public static String getUsername(String username) {
        File file = new File("src/Saves/" + username.toLowerCase() + ".psave");

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
        File getFile = new File("src/Saves/" + username.toLowerCase() + ".psave");
        return getFile.exists();
    }

    private static ItemStack interpretItemStack(String[] itemLine) {
        return new ItemStack(Integer.parseInt(itemLine[1]), Integer.parseInt(itemLine[2]), Long.parseLong(itemLine[3]), Boolean.parseBoolean(itemLine[4]));
    }
    
}
