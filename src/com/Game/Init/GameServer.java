package com.Game.Init;

import java.net.InetAddress;
import java.net.UnknownHostException;


public class GameServer {
    public static void main(String[] args) {
        PlayerConnection.init();
        Server server = new Server(3112);
        server.start();

//        InetAddress address = null;
//        try {
//            address = InetAddress.getByName("192.168.1.13");
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//        }
//
//        int port = 3112;
//        server.send(new byte[] {0, 1, 2}, address, port);
    }
}
