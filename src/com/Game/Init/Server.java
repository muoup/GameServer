package com.Game.Init;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class Server {
    private static ObjectInputStream input;
    private static ObjectOutputStream output;
    private static ServerSocket ss;
    private static Socket connection;

    public static void main(String[] args) throws IOException {
        Server server = new Server();

        server.startRunning();
    }

    public void startRunning() {
        try {
            ss = new ServerSocket(3000, 5);
            while (true) {
                try {
                    connect();
                    setupConnection();
                    detectPackets();
                } catch (EOFException e) {
                    System.out.println("Player has connected...");
                } catch (SocketException e) {
                    System.out.println("Player has disconnected...");
                } finally {
                    input.close();
                    output.close();
                    connection.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void connect() throws IOException {
        connection = ss.accept();
    }

    public void setupConnection() throws IOException {
        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();

        input = new ObjectInputStream(connection.getInputStream());
    }

    public void detectPackets() throws IOException {
        while (true) {
            try {
                String message = (String) input.readObject();
                output.writeObject(message);
                output.flush();
                System.out.println(message);
            } catch (ClassNotFoundException e) {
                System.err.println("This user has sent an unknown object!");
            }
        }
    }
}
