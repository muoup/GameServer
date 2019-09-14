package com.Game.Init;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Server {
    private int port;
    private Thread listenThread;
    private boolean listening = false;
    private DatagramSocket socket;
    private Set<PlayerConnection> connections;
    private final int MAX_PACKET_SIZE = 1024;
    private byte[] dataBuffer = new byte[MAX_PACKET_SIZE * 10];

    public Server(int port) {
        this.port = port;
        connections = new HashSet<PlayerConnection>();
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

        System.out.println(code);
        System.out.println(contents);

        switch (code) {
            case "69": // connection code
                // Do connection stuff
                PlayerConnection connection = new PlayerConnection(packet.getAddress(), packet.getPort());
                connections.add(connection);
                break;
            case "13": // Chat box message code
                System.out.println(connections.size());
                for (PlayerConnection c : connections) {
                    System.out.println(c);
                    send(contents.getBytes(), c.getIpAddress(), c.getPort());
                }
                break;
        }

        // Use a prefix to indicate the packet type, reference data from there
    }

    public void send(byte[] data, InetAddress address, int port) {
        assert(socket.isConnected());
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
