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

import java.util.Objects;

public class Password implements Comparable<Password> {
    private final String password;
    PasswordState state;

    public Password(String password, boolean registered, boolean hashed) {
        this.password = password;
        if (registered) {
            if (hashed) {
                state = PasswordState.HASHED;
            } else {
                state = PasswordState.UNHASHED;
            }
        } else {
            state = PasswordState.UNREGISTERED;
        }
    }

    public String getPassword(Object o) {
        if (state == PasswordState.HASHED) {
            return password;
        } else {
            if (o instanceof LoginHandler || o instanceof Obfuscator || o instanceof Password) {
                return password;
            } else {
                return null;
            }
        }
    }

    public void changeState(PasswordState s) {
        state = s;
    }


    @Override
    public int compareTo(Password o) {
        if (Objects.equals(this.getPassword(this), o.getPassword(this))) {
            return 0;
        } else {
            return -1;
        }
    }
}
