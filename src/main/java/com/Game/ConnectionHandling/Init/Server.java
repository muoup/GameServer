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

package com.Game.ConnectionHandling.Init;


import com.Game.ConnectionHandling.Save.ManageSave;
import com.Game.ConnectionHandling.Save.SaveSettings;
import com.Game.Entity.NPC.NPC;
import com.Game.Inventory.ItemStack;
import com.Game.Entity.Player.Player;
import com.Game.Objects.SkillingAreas.Tree;
import com.Game.Objects.SkillingAreas.TreePreset;
import com.Game.Skills.Skills;
import com.Game.Util.Math.Vector2;
import com.Game.Util.Other.Settings;
import com.Game.WorldManagement.WorldHandler;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Scanner;

public class Server {
    private int port;
    private Thread gameLoop, listenThread, checkConnect, commandThread, saveThread;
    private boolean listening = false;
    private static DatagramSocket socket;
    private ArrayList<Player> connections;
    private final int MAX_PACKET_SIZE = 1024;
    private byte[] dataBuffer = new byte[MAX_PACKET_SIZE * 10];
    private static final String serverVersion = "0.1.0a";
    private static int deltaTime;
    private static long updateLength;
    private static double fps = 0;

    public Server(int port) {
        this.port = port;
        connections = new ArrayList();
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

        gameLoop = new Thread(() -> gameLoop());
        gameLoop.start();
    }

    private void gameLoop() {
        // NOTE: just don't touch any of this, I got this off of StackOverFlow
        double lastFpsTime = 0;
        double lfps = 0;
        long lastLoopTime = System.nanoTime();

        while (true) {
            long optimalTime = 1000000000 / Settings.fpsCap;
            long now = System.nanoTime();
            updateLength = now - lastLoopTime;
            deltaTime = (int) updateLength / 1000000;

            lastLoopTime = now;

            double delta = updateLength / ((double) optimalTime);

            lastFpsTime += updateLength;
            lfps++;

            // Occurs once every second. Used to refresh the frame count per second.
            if (lastFpsTime >= 250000000) {
                lastFpsTime = 0;
                fps = lfps * 4;
                lfps = 0;
            }

            if (delta < 2)
                WorldHandler.update();

            long sleepTime = (lastLoopTime - System.nanoTime() + optimalTime) / 1000000;

            if (sleepTime < 0)
                continue;

            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
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
     * @param command string form of the command, does not start with any prefix.
     */
    private void handleCommands(String command) {
        String[] parts = command.split(" ");
        try {
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
                    for (Player c : connections) {
                        ManageSave.savePlayerData(c);
                        send("99", c.getIpAddress(), c.getPort());
                    }
                    System.exit(0);
                    break;
                case "xprate":
                    Settings.xpMultiplier = Float.parseFloat(parts[1]);
                    break;
                case "finger":
                    connections.forEach(System.out::println);
                    break;
            }
        } catch (Exception e) {
            System.err.println("Invalid Command / Args");
        }

    }

    private void handlePlayerCommand(String message) {
        String[] parts = message.split(";");

        Player player = findPlayer(parts[0]);
        String commandPart = parts[1].substring(2);
        String cmd = commandPart.split(" ")[0];
        String[] params = commandPart.replace(cmd + " ", "").split(" ");

        runCommand(player, cmd, params);
    }

    private static void runCommand(Player player, String command, String[] parameters) {
        if (player == null) {
            System.err.println("BAD PLAYER DATA! " + player);
            return;
        }

        try {
            switch (command.toLowerCase()) {
                case "pos":
                    player.sendMessage(player.getPosition().toString());
                    break;
                case "health":
                    player.sendMessage(Float.toString(player.health));
                    break;
                case "tp":
                    if (parameters.length == 2 || parameters.length == 3) {
                        player.sendMessage("Poof!");
                        if (parameters.length == 3) {
                            player.sendMessage("Changed Subworld");
                        }
                        player.setPos(Integer.parseInt(parameters[0]), Integer.parseInt(parameters[1]));
                    } else {
                        player.sendMessage("Correct parameters are (x, y) or (x, y, subWorld)");
                    }
                    break;
                case "setlevel":
                    if (parameters.length >= 2) {
                        int skill = Integer.parseInt(parameters[0]);
                        int level = Integer.parseInt(parameters[1]);

                        player.setExperience(skill, Skills.levelToExp(level));
                    }
                    break;
                case "sethealth":
                    if (parameters.length >= 1) {
                        player.setHealth(Float.parseFloat(parameters[0]));
                    }
                    break;
                case "item":
                    if (parameters.length >= 2) {
                        int item = Integer.parseInt(parameters[0]);
                        int amount = Integer.parseInt(parameters[1]);
                        int data = 0;
                        if (parameters.length >= 3)
                            data = Integer.parseInt(parameters[2]);

                        player.addItem(new ItemStack(item, amount, data));
                    }
                    break;
                case "nt":
                case "newtree":
                    int x = player.getX();
                    int y = player.getY();
                    String type = parameters[0];

                    switch (type) {
                        case "tree":
                            System.out.println("new Tree(this, " + x + ", " + y + ", TreePreset.tree);");
                            new Tree(player.getWorld(), x, y, TreePreset.tree);
                            break;
                        case "maple":
                            System.out.println("new Tree(this, " + x + ", " + y + ", TreePreset.maple);");
                            new Tree(player.getWorld(), x, y, TreePreset.maple);
                            break;
                        default:
                            player.sendMessage("That is not a valid tree type!");
                    }
                    break;
                default:
                    player.sendMessage("That is not a valid command, please check your spelling and try again.");
                    System.out.println("Command: " + command);
            }
        } catch (NumberFormatException e) {
            player.sendMessage("One of the parameters in that needed to be a number. It would have thrown an error, so it has been voided.");
        }
    }

    /**
     * Every 25 seconds, all player data is saved in case the server crashes
     * or in case I forget to run stop in the console before hand.
     */
    private void savePlayerData() {
        while (true) {
            for (Player c : connections) {
                ManageSave.savePlayerData(c);
            }
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
                for (Player c : connections) {
                    if (c.connected < 3) {
                        send("76".getBytes(), c.getIpAddress(), c.getPort());
                        c.connected++;
                    } else {
                        send("99".getBytes(), c.getIpAddress(), c.getPort());
                        playerDisconnect(c);
                        connections.remove(c);
                        break;
                    }
                }
                checkConnect.sleep(3000);
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
    private void playerDisconnect(Player connection) {
        ManageSave.savePlayerData(connection);
        for (Player c : connections) {
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

        // This may just be a one-of-a-kind error, so for now im just gonna see how much it happens
        if (contents.equals("weirdconcurrenterror")) {
            System.err.println("i have never seen this before, i have no idea how much this will happen");
            return;
        }

        String code = contents.substring(0, 2);
        String message = contents.substring(2).trim();
        String[] index;
        Player connection;

        try {
            switch (code) {
                case "69": // connection code
                    index = message.split(":");
                    String username = index[0].trim();
                    String password = index[1].trim();
                    String connectionCode = index[2].trim();
                    String clientVersion = index[3].trim();
                    boolean connect = handleLogin(username, password, Integer.parseInt(connectionCode), packet, clientVersion);
                    send("00" + SaveSettings.questAmount + ":" + SaveSettings.skillAmount, packet.getAddress(), packet.getPort());
                    if (!connect)
                        break;
                    connection = getPlayerObject(packet, username, password, Integer.parseInt(connectionCode));
                    for (Player c : connections) {
                        if (!c.getUsername().equals(connection.getUsername())) {
                            send(c, "12", connection.getX() + ":" + connection.getY() + ":" + connection.getUsername());
                        }
                    }
                    break;
                case "76":
                    connection = findPlayer(message);
                    if (connection != null)
                        connection.connected = 0;
                    break;
                case "13": // Chat box message code
                    chatMessage(contents);
                    break;
                case "55": // Disconnect code
                    connection = findPlayer(message);
                    playerDisconnect(connection);
                    connections.remove(connection);
                    break;
                case "15":
                    index = message.split(":");
                    Player movement = findPlayer(index[0]);

                    if (movement == null)
                        break;

                    Vector2 newPos = new Vector2(Integer.parseInt(index[1]), Integer.parseInt(index[2]));

                    if (Vector2.distance(movement.getPosition(), newPos) > 1f) {
                        movement.setPos((int) newPos.x, (int) newPos.y);
                        movement.loseFocus();
                    }

                    for (Player c : connections) {
                        if (!c.getUsername().equals(movement.getUsername())) {
                            send("15" + movement.getUsername() + ":" + movement.getX() + ":" + movement.getY() + ":" + index[4], c.getIpAddress(), c.getPort());
                        }
                    }
                    break;
                case "oi": // Object Interaction
                    Player objectInteracter = findPlayer(message);
                    objectInteracter.interact();
                    break;
                case "lf": // Lose Focus
                    Player focusLost = findPlayer(message);
                    focusLost.loseFocus();
                    break;
                case "bc": // Bank Change (Request)
                    index = message.split(";");
                    Player bankRequest = findPlayer(index[0]);
                    bankRequest.handleBankRequest(index);
                    break;
                case "gc": // GUI Close
                    Player closer = findPlayer(message);
                    closer.hideBank();
                    break;
                case "ss": // Swap Slots
                    index = message.split(";");
                    Player swapper = findPlayer(index[0]);
                    swapper.swapSlots(Integer.parseInt(index[1]), Integer.parseInt(index[2]));
                    break;
                case "bs": // Bank Swap
                    index = message.split(";");
                    Player bswapper = findPlayer(index[0]);
                    bswapper.swapBankSlots(Integer.parseInt(index[1]), Integer.parseInt(index[2]));
                    break;
                case "rc": // Right Click Item
                    index = message.split(";");
                    Player rcer = findPlayer(index[0]);
                    rcer.rightClick(Integer.parseInt(index[1]), index[2]);
                    break;
                case "lc": // Left Click Item
                    index = message.split(";");
                    Player click = findPlayer(index[0]);
                    click.click(Integer.parseInt(index[1]));
                    break;
                case "un": // Unequip Item
                    index = message.split(";");
                    Player unequip = findPlayer(index[0]);
                    unequip.unequip(Integer.parseInt(index[1]));
                    break;
                case "si": // Shop Interact
                    index = message.split(";");
                    Player buyItem = findPlayer(index[0]);
                    buyItem.shop.handlePacket(index);
                    break;
                case "ch": // Textbox Choice (Chosen)
                    index = message.split(";");
                    Player chooser = findPlayer(index[0]);
                    NPC.choose(chooser, Integer.parseInt(index[1]), index[2]);
                    break;
                case "sb": // Shoot Bullet
                    index = message.split(";");
                    Player shooter = findPlayer(index[0]);
                    shooter.shoot(Vector2.fromString(index[1]));
                    break;
                case "wb": // Withdraw [into] inventory
                    index = message.split(";");
                    Player withdrawer = findPlayer(index[0]);
                    withdrawer.banking.withdrawItem(Integer.parseInt(index[1]), 1);
                    break;
                case "gi": // GroundItem Interact
                    index = message.split(";");
                    Player pickerUper = findPlayer(index[0]);
                    pickerUper.pickUp(message);
                    break;
                case "cm": // Player Command
                    handlePlayerCommand(message);
                    break;
            /*case "07":
                index = message.split(":");
                int skill = Integer.parseInt(index[0]);
                float xp = Float.parseFloat(index[1]);
                findPlayer(index[2]).skills.xp[skill] += xp;
                break;
            case "08":
                index = message.split(":");
                int slot = Integer.parseInt(index[0]);
                int id = Integer.parseInt(index[1]);
                int amount = Integer.parseInt(index[2]);
                int data = Integer.parseInt(index[3]);
                connection = findPlayer(index[4]);
                connection.inventory.clientSetItem(id, amount);
                break;
            case "09":
                index = message.split(":");
                int aslot = Integer.parseInt(index[0]);
                int aid = Integer.parseInt(index[1]);
                int aamount = Integer.parseInt(index[2]);
                int adata = Integer.parseInt(index[3]);
                connection = findPlayer(index[4]);
                connection.accessoryItems[aslot].id = aid;
                connection.accessoryItems[aslot].amount = aamount;
                connection.accessoryItems[aslot].data = adata;
                break;
            case "57":
                index = message.split(":");
                int qid = Integer.parseInt(index[0]);
                int qdata = Integer.parseInt(index[1]);
                connection = findPlayer(index[2]);
                connection.questSaves[qid] = qdata;
                break;
            case "6e":
                index = message.split(":");
                int bindex = Integer.parseInt(index[0]);
                int bid = Integer.parseInt(index[1]);
                int bamount = Integer.parseInt(index[2]);
                int bdata = Integer.parseInt(index[3]);
                connection = findPlayer(index[4]);
                ItemMemory bankItem = connection.bankItems.get(bindex);
                bankItem.id = bid;
                bankItem.amount = bamount;
                bankItem.data = bdata;
                break;
            case "6c":
                index = message.split(":");
                int baid = Integer.parseInt(index[0]);
                int baamount = Integer.parseInt(index[1]);
                int badata = Integer.parseInt(index[2]);
                connection = findPlayer(index[3]);
                connection.bankItems.add(new ItemMemory(baid, baamount, badata));
                break;*/
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(new String(packet.getData()));

            for (Player player : connections) {
                System.out.println(player.username);
            }
        }
    }

    /**
     * Takes in a username and searches all connected players and returns
     * a PlayerConnection if the usernames match. Returns null if it fails.
     * @param username Username of Requested Player
     * @return The PlayerConnection file of the requested player.
     */
    public Player findPlayer(String username) {
        for (Player c : connections) {
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
            send("02" + "r" + ((isConnect) ? "c:" + username : "i:N/A"), packet.getAddress(), packet.getPort());
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
        for (Player c : connections) {
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
     * @return PlayerConnection Objects of the newly connected player to add to the ArrayList of players.
     */
    public Player getPlayerObject(DatagramPacket packet, String username, String password, int connectionCode) {
        Player connection = (connectionCode == 0) ?
                ManageSave.loadPlayerData(username, packet) : ManageSave.createPlayerData(username, password, packet);
        connections.add(connection);
        return connection;
    }

    public static void send(byte[] data, InetAddress address, int port) {
        assert(socket.isConnected());
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
    public static void send(String data, InetAddress address, int port) {
        send(data.getBytes(), address, port);
    }

    public static void send(Player player, String data) {
        send(data.getBytes(), player.getIpAddress(), player.getPort());
    }

    public static void send(Player player, String tag, Object... data) {
        StringBuilder packetContents = new StringBuilder(tag);

        for (Object part : data) {
            packetContents.append(part + ";");
        }

        // TAG;Data1;Data2;Data3...
        send(player, packetContents.substring(0, packetContents.length() - 1));
    }

    /**
     * Returns the delta time of each server frame.
     * If the player moves at some speed times delta time, the movement will be constant no matter the frame rate.
     * @return Delta Time
     */
    public static double dTime() {
        return deltaTime / 1000f;
    }
}
