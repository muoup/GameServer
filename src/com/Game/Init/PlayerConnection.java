package com.Game.Init;

import com.Game.Save.*;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class PlayerConnection {
    private InetAddress ipAddress;
    private int port;
    public boolean connected = true;

    public float[] skillXP;
    public ItemMemory[] inventoryItems;
    public ItemMemory[] accessoryItems;
    private static ItemMemory[] invTemp;
    private static ItemMemory[] accTemp;
    public int x, y, subWorld;
    private String username;
    /**
     * The Password, when set using {@link #setPassword(String)}.
     * Typically, when set through this method, the password will be hashed.
     */
    private String password;
    private byte[] salt = getSalt(); // Salt is added to the password to make the hash more secure.
    public boolean isPasswordHashed = false; // By default, the password should not be considered hashed.

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
        this.skillXP = new float[SaveSettings.skillAmount];
        this.x = 0;
        this.y = 0;
        this.username = "";
        this.password = "";
        this.inventoryItems = new ItemMemory[invTemp.length];
        this.accessoryItems = new ItemMemory[accTemp.length];
        this.subWorld = 0;

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
        this.password = hashPassword(password);
    }

    public void setPos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * This takes the unhashed password typically set by {@link #setPassword(String)} and securely hashes it with
     * a salted SHA-256 hash.
     * @param unhashed The unhashed password in String format from {@link #setPassword(String)}.
     * @return The hashed password. This also overrides the password field with the hashed password, which may cause errors in the future.
     */
    private String hashPassword(String unhashed)  {
        MessageDigest md;
        byte[] bytes = null;
        StringBuilder sb = new StringBuilder();
        try {
            md = MessageDigest.getInstance("SHA-256");
            md.update(salt);

            bytes = md.digest(unhashed.getBytes(StandardCharsets.UTF_8));

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            //Implement failure code here. The program shouldn't exit, but clearly shouldn't be silent.
        }
        for (int i = 0; i < bytes.length; i++) {
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));


        }
        String hashed = sb.toString();
        password = hashed;
        isPasswordHashed = true;
        return hashed;
    }

    /**
     * This method uses a Secure Random pseudo-random generation method to generate a byte array to be used as the password
     * salt.
     * @return The password salt
     */
    public static byte[] getSalt() {
        SecureRandom sr = null;
        try {
            sr = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] salt = new byte[16];

        sr.nextBytes(salt);

        return salt;
    }
}
