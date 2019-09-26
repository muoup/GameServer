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

import com.Game.Save.ItemMemory;
import com.Game.Save.ManageSave;
import com.Game.Save.SaveSettings;
import com.Game.security.*;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Server {
    private int port;
    private Thread listenThread;
    private Thread checkConnect;
    private Thread commandThread;
    private Thread saveThread;
    private boolean listening = false;
    private DatagramSocket socket;
    private List<PlayerConnection> connections;
    private final int MAX_PACKET_SIZE = 1024;
    private byte[] dataBuffer = new byte[MAX_PACKET_SIZE * 10];
    private static final String serverVersion = "0.0.1a";

    public Server(int port) {
        this.port = port;
        connections = new ArrayList<PlayerConnection>();
    }

    public void start() {
        try {
            socket = new DatagramSocket(port);
        } catch (SocketException e) {
            e.printStackTrace();
        }

        listening = true;

        listenThread = new Thread(() -> listen());
        listenThread.start();

        checkConnect = new Thread(() -> checkConnection());
        checkConnect.start();

        commandThread = new Thread(() -> commands());
        commandThread.start();

        saveThread = new Thread(() -> savePlayerData());
        saveThread.start();
    }

    private void listen() {
        while (listening) {
            DatagramPacket packet = new DatagramPacket(dataBuffer, dataBuffer.length);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }

            process(packet);
        }
    }

    private void commands() {
        String listen;
        Scanner scanner = new Scanner(System.in);
        while (true) {
            listen = scanner.nextLine();
            handleCommands(listen);
        }
    }

    private void handleCommands(String command) {
        String[] parts = command.split(" ");
        switch (parts[0]) {
            case "say":
                if (parts.length == 1) {
                    System.out.println("say [message]");
                    break;
                }
                String message = "";
                for (int i = 1; i < parts.length; i++) {
                    message += parts[i];
                }
                chatMessage("[Server] " + message);
                break;
            case "stop":
                chatMessage("Server shutting down...");
                for (PlayerConnection c : connections) {
                    ManageSave.savePlayerData(c);
                    send("99", c.getIpAddress(), c.getPort());
                }
                System.exit(0);
                break;
            case "finger":
                connections.forEach(System.out::println);
                break;
        }
    }

    private void savePlayerData() {
        while (true) {
            for (PlayerConnection c : connections) {
                ManageSave.savePlayerData(c);
            }
            System.out.println("Saved player data.");
            try {
                saveThread.sleep(25000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void checkConnection() {
        while (listening) {
            try {
                for (PlayerConnection c : connections) {
                    if (c.connected) {
                        send("76".getBytes(), c.getIpAddress(), c.getPort());
                        c.connected = false;
                    } else {
                        send("99".getBytes(), c.getIpAddress(), c.getPort());
                        playerDisconnect(c);
                        connections.remove(c);
                        break;
                    }
                }
                checkConnect.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void playerDisconnect(PlayerConnection connection) {
        ManageSave.savePlayerData(connection);
        for (PlayerConnection c : connections) {
            send("66" + connection.getUsername(), c.getIpAddress(), c.getPort());
        }
    }

    private void process(DatagramPacket packet) {
        //dumpPacket(packet);

        dataBuffer = new byte[MAX_PACKET_SIZE * 10];
        String contents = new String(packet.getData());

        String code = contents.substring(0, 2);
        String message = contents.substring(2).trim();
        String[] index;
        PlayerConnection connection;
        switch (code) {
            case "69": // connection code
                index = message.split(":");
                String username = index[0].trim();
                String password = index[1].trim();
                String clientVersion = index[5].trim();
                boolean connect = handleLogin(username, password, Integer.parseInt(index[2]), packet, clientVersion);
                if (!connect)
                    break;
                connection = handleLogin(packet, username, password, Integer.parseInt(index[2].trim()), Integer.parseInt(index[3].trim()), Integer.parseInt(index[4].trim()));
                for (PlayerConnection c : connections) {
                    if (c.getUsername() != connection.getUsername()) {
                        send((12 + "" + c.getX() + ":" + c.getY() + ":" + c.getUsername()).getBytes(), packet.getAddress(), packet.getPort());
                        send((12 + "" + connection.getX() + ":" + connection.getY() + ":" + connection.getUsername()).getBytes(), c.getIpAddress(), c.getPort());
                    }
                }
                break;
            case "76":
                connection = findPlayer(message);
                if (connection != null)
                    connection.connected = true;
                break;
            case "13": // Chat box message code
                chatMessage(contents);
                break;
            case "55": // Disconnect code
                connection = findPlayer(message);
                connections.remove(connection);
                playerDisconnect(connection);
                break;
            case "15":
                index = message.split(":");
                PlayerConnection movement = findPlayer(index[0]);
                movement.setPos(Integer.parseInt(index[1]), Integer.parseInt(index[2]));
                if (movement == null)
                    break;
                for (PlayerConnection c : connections) {
                    if (c.getUsername() != movement.getUsername()) {
                        send(("15" + movement.getUsername() + ":" + movement.getX() + ":" + movement.getY()).getBytes(), c.getIpAddress(), c.getPort());
                    }
                }
                break;
            case "07":
                index = message.split(":");
                int skill = Integer.parseInt(index[0]);
                float xp = Float.parseFloat(index[1]);
                findPlayer(index[2]).skillXP[skill] += xp;
                break;
            case "08":
                index = message.split(":");
                int slot = Integer.parseInt(index[0]);
                int id = Integer.parseInt(index[1]);
                int amount = Integer.parseInt(index[2]);
                connection = findPlayer(index[3]);
                connection.inventoryItems[slot].id = id;
                connection.inventoryItems[slot].amount = amount;
                break;
            case "09":
                index = message.split(":");
                int aslot = Integer.parseInt(index[0]);
                int aid = Integer.parseInt(index[1]);
                int aamount = Integer.parseInt(index[2]);
                connection = findPlayer(index[3]);
                connection.accessoryItems[aslot].id = aid;
                connection.accessoryItems[aslot].amount = aamount;
                break;
        }
    }

    public PlayerConnection findPlayer(String username) {
        for (PlayerConnection c : connections) {
            if (c.getUsername().equalsIgnoreCase(username.trim()))
                return c;
        }

        return null;
    }

    //TODO implement a LoginHandler
    public boolean handleLogin(String username, String password, int connection, DatagramPacket packet, String clientVersion) {
        Password pass = new Password(password, true, false); //Encode password as Password
        if (!clientVersion.equals(serverVersion)) {
            send("02" + "v", packet.getAddress(), packet.getPort());
            return false;
        }
        if (connection == 0) {
            boolean isConnect = ManageSave.loginCorrect(username, pass);
            if (findPlayer(username) != null && isConnect) {
                send("02" + "p", packet.getAddress(), packet.getPort());
                return false;
            }
            String capName = ManageSave.getUsername(username);
            send("02" + "l" + ((isConnect) ? "c:" + capName : "i:N/A"), packet.getAddress(), packet.getPort());
            return isConnect;
        } else if (connection == 1) {
            boolean isConnect = !ManageSave.usernameExists(username);
            String capName = ManageSave.getUsername(username);
            send("02" + "r" + ((isConnect) ? "c:" + capName : "i:N/A"), packet.getAddress(), packet.getPort());
            return isConnect;
        } else {
            System.err.println("Bad connection code: " + connection);
        }
        return false;
    }

    public void chatMessage(String message) {
        for (PlayerConnection c : connections) {
            send(message.getBytes(), c.getIpAddress(), c.getPort());
        }
    }

    public PlayerConnection handleLogin(DatagramPacket packet, String username, String password, int connectionCode, int x, int y) {
        // This is wear loading and saving would go, nothing for now
        PlayerConnection connection = (connectionCode == 0) ? ManageSave.loadPlayerData(username, packet) : ManageSave.createPlayerData(username, password, packet);
        connections.add(connection);
        String send = "04" + username + ":" + connection.getX() + ":" + connection.getY();
        for (int i = 0; i < SaveSettings.skillAmount; i++) {
            send += ":" + connection.skillXP[i];
        }
        send(send, packet.getAddress(), packet.getPort());
        send = "05";
        for (int i = 0; i < SaveSettings.inventoryAmount; i++) {
            ItemMemory mem = connection.inventoryItems[i];
            send += ":" + mem.id + ":" + mem.amount;
        }
        send(send, packet.getAddress(), packet.getPort());
        send = "06";
        for (int i = 0; i < SaveSettings.accessoryAmount; i++) {
            ItemMemory mem = connection.accessoryItems[i];
            send += ":" + mem.id + ":" + mem.amount;
        }
        send(send, packet.getAddress(), packet.getPort());
        return connection;
    }

    public void send(byte[] data, InetAddress address, int port) {
        assert(socket.isConnected());
//        System.out.println("Packet Sent: " + new String(data) + " " + address.getHostAddress() + ":" + port + " Length: " + data.length);
        DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(String data, InetAddress address, int port) {
        send(data.getBytes(), address, port);
    }
}
