package com.Game.Save;

import com.Game.Init.PlayerConnection;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
                    String username = "";
                    for (int i = 1; i < parts.length; i++) {
                        username += parts[i];
                    }
                    data.setUsername(username);
                    break;
                case "Password:":
                    String password = "";
                    for (int i = 1; i < parts.length; i++) {
                        password += parts[i];
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
                    for (int i = 1; i < parts.length; i++) {
                        if (i % 2 == 1)
                            data.inventoryItems[(i - 1) / 2].id = Integer.parseInt(parts[i]);
                        else
                            data.inventoryItems[(i - 1) / 2].amount = Integer.parseInt(parts[i]);
                    }
                    break;
                case "Accessory:":
                    System.out.println(data + " " + data.accessoryItems.length);
                    for (int i = 1; i < parts.length; i++) {
                        if (i % 2 == 1)
                            data.accessoryItems[(i - 1) / 2].id = Integer.parseInt(parts[i]);
                         else
                            data.accessoryItems[(i - 1) / 2].amount = Integer.parseInt(parts[i]);

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
        connection.setPassword(password);
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

        writer.println("Login: " + data.getUsername());

        writer.println("Password: " + data.getPassword());
        writer.println("Pos: " + data.x + " " + data.y + " " + data.subWorld);

        String skillsLine = "Skills:";

        for (float i : data.skillXP)
            skillsLine += " " + df.format(i);

        String invLine = "Inventory:";
        for (ItemMemory mem : data.inventoryItems)
            invLine += " " + mem.id + " " + mem.amount;

        writer.println(invLine);

        String accLine = "Accessory:";
        for (ItemMemory mem : data.accessoryItems)
            accLine += " " + mem.id + " " + mem.amount;

        writer.println(accLine);

        writer.println(skillsLine);
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
        File file = new File("src/saves/" + username.toLowerCase() + ".psave");

        Scanner scanner;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            System.err.println("FILE NOT FOUND: " + username.toLowerCase() + ".psave");
            return false;
        }
        scanner.nextLine();
        String[] loginLine = scanner.nextLine().split(" ");
        String pass = loginLine[1]; // Hashed passwords are one word (?) so the for loop is no longer needed.

        // TODO: Fix password system so that it uses hashed password eventually.
        // For now, passwords and usernames can only be one word long, until they are hashed.

        return pass.equals(password.trim());
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
