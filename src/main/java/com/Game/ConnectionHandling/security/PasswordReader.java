package com.Game.ConnectionHandling.security;

import java.io.*;

public class PasswordReader {
    public static LoginHandler readPass(File saveFile) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(saveFile));
        reader.readLine();
        String[] pass = reader.readLine().split(" ");
        if (Integer.parseInt(pass[2]) == 1) {
            LoginHandler handler = new HashedLogin(new Password(pass[1], true, true));
            return handler;
        } else {
            LoginHandler handler = new VulnerableLogin(new Password(pass[1], true, false));
            return handler;
        }
    }
}
