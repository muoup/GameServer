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
