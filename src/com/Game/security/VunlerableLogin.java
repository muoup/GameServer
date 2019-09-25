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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class VunlerableLogin implements LoginHandler {

    private Password pass;
    private String username;

    @Override
    public void setPassword(Password p) {
        if (p.state == PasswordState.UNHASHED) {
            this.pass = p;
        }
    }

    @Override
    public String takeUserInput() {
        Scanner input = new Scanner(System.in);
        return input.nextLine();
    }

    @Override
    public boolean match(Password p) {
        return this.pass.compareTo(p) == 0;
    }

    @Override
    public Password readPassword(File save) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(save));
        String[] logins = reader.readLine().split(" ");
        return new Password(logins[1], true, false);
    }

    @Override
    public File findSave() {
        return new File("com/Game/saves/" + username + ".psave");
    }
}
