package com.Game.Init;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws IOException {
        Server server = new Server();
        ServerSocket ss = new ServerSocket(3000);

        while (true) {
            server.detectPackets(ss);
        }
    }

    public void detectPackets(ServerSocket ss) throws IOException {
        Socket socket = ss.accept();
        System.out.println(socket);

        InputStreamReader in = new InputStreamReader(socket.getInputStream());
        BufferedReader bf = new BufferedReader(in);

        String str = bf.readLine();

        System.out.println(str);
    }
}
