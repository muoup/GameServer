package com.Game.Init;

import com.Game.Save.ItemMemory;
import com.Game.Save.ManageSave;
import com.Game.Save.SaveSettings;

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

        // Starts up threads for different tasks.
        // See method documentation for explanations.
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

    /**
     * Constant listening for console commands. Running on separate thread.
     */
    private void commands() {
        String listen;
        Scanner scanner = new Scanner(System.in);
        while (true) {
            listen = scanner.nextLine();
            handleCommands(listen);
        }
    }

    /**
     * Do not call this method. Used for console commands in the server.
     * Say does not work rn.
     * Stop saves all player data and shuts down the server/
     * @param command String form of the command, does not start with any prefix.
     */
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
        }
    }

    /**
     * Every 25 seconds, all player data is saved in case the server crashes
     * or in case I forget to run stop in the console before hand.
     */
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

    /**
     * Basic method of detecting if a player has timed out.
     * Every ten seconds every client is sent a ping message which requires a pong.
     * If the client does not respond to the ping in another ten seconds, they are timed out.
     * This is mostly used to determine if a player's client has crashed so that the player can re-log
     * without having to restart the server.
     */
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

    /**
     * If the player times out or logs out, all other clients are told
     * that the player has logged out and to stop rendering them.
     * @param connection PlayerConnection file, get from handleLogin();
     */
    private void playerDisconnect(PlayerConnection connection) {
        ManageSave.savePlayerData(connection);
        for (PlayerConnection c : connections) {
            send("66" + connection.getUsername(), c.getIpAddress(), c.getPort());
        }
    }

    /**
     * Never call this method except from listen().
     * Used for interpreting packet data.
     * @param packet Packet that was received.
     */
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
                    if (!c.getUsername().equals(connection.getUsername())) {
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
                movement.setPos(Integer.parseInt(index[1]), Integer.parseInt(index[2]), Integer.parseInt(index[3]));
                if (movement == null)
                    break;
                for (PlayerConnection c : connections) {
                    if (c.getUsername() != movement.getUsername()) {
                        send("15" + movement.getUsername() + ":" + movement.getX() + ":" + movement.getY() + ":" + movement.subWorld, c.getIpAddress(), c.getPort());
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

    /**
     * Takes in a username and searches all connected players and returns
     * a PlayerConnection if the usernames match. Returns null if it fails.
     * @param username Username of Requested Player
     * @return The PlayerConnection file of the requested player.
     */
    public PlayerConnection findPlayer(String username) {
        for (PlayerConnection c : connections) {
            if (c.getUsername().equalsIgnoreCase(username.trim()))
                return c;
        }

        return null;
    }

    /**
     * Takes in information about the player and sends packets to the player if their connection fails or succeeds.
     * @param username Player's Username
     * @param password Player's Password
     * @param connection The type of connection from the player (0 - login, 1 - register)
     * @param packet Packet of the Connected Player
     * @param clientVersion Version of the player's client. Used to prevent old versions from connecting to the server.
     * @return
     */
    public boolean handleLogin(String username, String password, int connection, DatagramPacket packet, String clientVersion) {
        // TODO: Reimplement login once Connor Finishes.
        if (!clientVersion.equals(serverVersion)) {
            send("02" + "v", packet.getAddress(), packet.getPort());
            return false;
        }
        if (connection == 0) {
            boolean isConnect = ManageSave.loginCorrect(username, password);
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

    /**
     * Sends a message to all connected player's chatbars.
     * @param message Message to broadcast.
     */
    public void chatMessage(String message) {
        for (PlayerConnection c : connections) {
            send(message.getBytes(), c.getIpAddress(), c.getPort());
        }
    }

    /**
     * Creates a PlayerConnection object for new player, and sends the player data from their save file.
     * Make sure to run boolean handleLogin first to make sure it is a legal login.
     * @param packet Packet of Connecting Player
     * @param username Username of Connecting PLayer
     * @param password Password of Connecting Player
     * @param connectionCode Connection Code of Player (0 - login, 1 - register, more..?);
     * @param x Player x-coordinate
     * @param y Player y-coordinate
     * @return PlayerConnection Object of the newly connected player to add to the ArrayList of players.
     */
    public PlayerConnection handleLogin(DatagramPacket packet, String username, String password, int connectionCode, int x, int y) {
        PlayerConnection connection = (connectionCode == 0) ? ManageSave.loadPlayerData(username, packet) : ManageSave.createPlayerData(username, password, packet);
        connections.add(connection);
        String send = "04" + username + ":" + connection.getX() + ":" + connection.getY() + ":" + connection.subWorld;
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

    /**
     * Sends a string to a specified address and port (UDP).
     * @param data Message to send to client.
     * @param address Address of client.
     * @param port Port of Client.
     */
    public void send(String data, InetAddress address, int port) {
        send(data.getBytes(), address, port);
    }
}
