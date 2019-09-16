package com.Game.Init;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Server {
    private int port;
    private Thread listenThread;
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

    private void process(DatagramPacket packet) {
        //dumpPacket(packet);

        dataBuffer = new byte[MAX_PACKET_SIZE * 10];
        String contents = new String(packet.getData());

        String code = contents.substring(0, 2);
        String message = contents.substring(2);

        switch (code) {
            case "69": // connection code
                String[] index = message.split(",");
                PlayerConnection newUser = handleLogin(packet, index[0].trim(), index[1].trim(), Integer.parseInt(index[2].trim()), Integer.parseInt(index[3].trim()), Integer.parseInt(index[4].trim()));
                System.out.println("POSITION RECEIVED: " + newUser.getX() + " " + newUser.getY());
                for (PlayerConnection c : connections) {
                    if (c.getUsername() != newUser.getUsername()) {
                        send((12 + "" + c.getX() + ":" + c.getY() + ":" + c.getUsername()).getBytes(), packet.getAddress(), packet.getPort());
                        send((12 + "" + newUser.getX() + ":" + newUser.getY() + ":" + newUser.getUsername()).getBytes(), c.getIpAddress(), c.getPort());
                    }
                }
                System.out.println(connections.get(0));
                break;
            case "13": // Chat box message code
                chatMessage(contents);
                break;
            case "55": // Disconnect code
                chatMessage(contents + " has left the game...");
                for (PlayerConnection c : connections) {
                    if (c.getUsername() == contents) {
                        System.out.println(c.getIpAddress() + " disconnect");
                        connections.remove(c);
                        return;
                    }
                }
                break;
            case "15":
                String[] parts = message.split(":");
                int i = Integer.parseInt(parts[3].trim());
                if (i == -1)
                    return;
                PlayerConnection movement = connections.get(i);
                System.out.println("Y: " + parts[2]);
                movement.setPos(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
                if (movement == null)
                    return;
                for (PlayerConnection c : connections) {
                    if (c.getUsername() != movement.getUsername()) {
                        send(("15" + movement.getUsername() + ":" + movement.getX() + ":" + movement.getY()).getBytes(), c.getIpAddress(), c.getPort());
                    }
                }
                break;
        }
    }

    public void chatMessage(String message) {
        for (PlayerConnection c : connections) {
            send(message.getBytes(), c.getIpAddress(), c.getPort());
        }
    }

    public PlayerConnection handleLogin(DatagramPacket packet, String username, String password, int connectionCode, int x, int y) {
        // This is wear loading and saving would go, nothing for now
        // TODO: implement saving and loading
        // TODO: implement checking password
        PlayerConnection connection = new PlayerConnection(packet.getAddress(), packet.getPort());
        connection.setUsername(username);
        connection.setPos(x, y);
        connections.add(connection);
        send(("01" + (connections.size() - 1)).getBytes(), packet.getAddress(), packet.getPort());
        return connection;
    }

    public void send(byte[] data, InetAddress address, int port) {
        assert(socket.isConnected());
        System.out.println("Packet Sent: " + new String(data) + " " + address.getHostAddress() + ":" + port + " Length: " + data.length);
        DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void dumpPacket(DatagramPacket packet) {
        byte[] data = packet.getData();
        InetAddress address = packet.getAddress();
        int port = packet.getPort();

        System.out.println(".......................");
        System.out.println("PACKET: ");
        System.out.println("\t" + address.getHostAddress() + ":" + port);
        System.out.println();
        System.out.println("\tContents: " +new String(data).trim());
        System.out.println(".......................");
    }
}
