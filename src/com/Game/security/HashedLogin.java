/*
 * Copyright (c) 2019 Connor McDermid
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

package com.Game.security;

import com.Game.exceptions.InvalidPasswordException;

import java.io.*;
import java.util.Comparator;
import java.util.Scanner;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

public class HashedLogin implements LoginHandler {

    private Password pass;
    private String username;

    public void setPassword(Password p) {
        pass = p;
    }

    @Override
    public String takeUserInput() {
        Scanner input = new Scanner(System.in);
        String usr = input.nextLine();
        return usr;
    }

    @Override
    public boolean match(Password p) {
        return pass.compareTo(p) == 0;
    }

    @Override
    public Password readPassword(File save) throws IOException {
        Obfuscator obf = new Obfuscator();
        BufferedReader reader = new BufferedReader(new FileReader(save));
        String logins = reader.readLine();
        String[] usrpass = logins.split(" ");
        return new Password(usrpass[1], true, true);
    }

    @Override
    public File findSave(String username) throws IOException {
        return new File("src/com/Game/saves/" + username + ".psave");
    }

    public Password hashPass(Password p) {
        Obfuscator obs = new Obfuscator();
        String temp = obs.hashPassword(p.getPassword(this));
        return new Password(temp, true, true);
    }

    public HashedLogin(Password p) {
        this.pass = p;
    }
}
