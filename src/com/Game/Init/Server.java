package com.Game.Init;

import com.Game.Save.ItemMemory;
import com.Game.Save.ManageSave;
import com.Game.Save.SaveSettings;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private int port;
    private Thread listenThread;
    private Thread checkConnect;
    private boolean listening = false;
    private DatagramSocket socket;
    private List<PlayerConnection> connections;
    private final int MAX_PACKET_SIZE = 1024;
    private byte[] dataBuffer = new byte[MAX_PACKET_SIZE * 10];

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
                boolean connect = handleLogin(username, password, Integer.parseInt(index[2]), packet);
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
                int i = Integer.parseInt(index[3].trim());
                if (i == -1)
                    break;
                PlayerConnection movement = connections.get(i);
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
                System.out.println("Skill: " + skill + " XP: " + xp);
                findPlayer(index[2]).skillXP[skill] += xp;
                break;
            case "08":
                index = message.split(":");
                int slot = Integer.parseInt(index[0]);
                int id = Integer.parseInt(index[1]);
                int amount = Integer.parseInt(index[2]);
                connection = findPlayer(index[3]);
                System.out.println("SLOT: " + slot + " ID: " + id + " AMOUNT: " + amount);
                connection.inventoryItems[slot].id = id;
                connection.inventoryItems[slot].amount = amount;
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

    public boolean handleLogin(String username, String password, int connection, DatagramPacket packet) {
        if (connection == 0) {
            boolean isConnect = ManageSave.loginCorrect(username, password);
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
        PlayerConnection connection = ManageSave.loadPlayerData(username, packet);
//        connection.connected = true;
        connections.add(connection);
        send("01" + (connections.size() - 1), packet.getAddress(), packet.getPort());
        String send = "04" + connection.getUsername() + ":" + connection.getX() + ":" + connection.getY();
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
