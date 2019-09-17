package com.Game.Init;

import java.net.InetAddress;

public class PlayerConnection {
    private int x;
    private int y;
    private InetAddress ipAddress;
    private int port;
    private String username;
    // TODO: Save arrays of information for saving
    // Array of Stats
    private Save playerSave;
    public boolean connected = true;
    // Array of Inventory
    // Array of Equipment

    public PlayerConnection(InetAddress ipAddress, int port) {
        this.ipAddress = ipAddress;
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setPos(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
