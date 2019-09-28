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

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;


/**
 * Class Obfuscator and its instantiations will handle security functions such as password hashing.
 * This class <em>must</em> be instantiated in order to use the methods within, for security purposes.
 *
 * @author Connor McDermid
 *
 * @since 0.01
 */
public class Obfuscator {

    private String hashed;
    private static byte[] salt = getSalt();


    public Obfuscator() {
        //null
    }
    /**
     * This takes the unhashed password typically set by {@link #setPassword(String)} and securely hashes it with
     * a salted SHA-256 hash.
     * @param unhashed The unhashed password in String format from {@link #setPassword(String)}.
     * @return The hashed password. This also overrides the password field with the hashed password, which may cause errors in the future.
     */
    public String hashPassword(String unhashed)  {
        MessageDigest md;
        byte[] bytes = null;
        StringBuilder sb = new StringBuilder();
        try {
            md = MessageDigest.getInstance("SHA-256");
            md.update(salt);

            bytes = md.digest(unhashed.getBytes(StandardCharsets.UTF_8));

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            //Implement failure code here. The program shouldn't exit, but clearly shouldn't be silent.
        }
        for (int i = 0; i < bytes.length; i++) {
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));


        }
        String hashed = sb.toString();
        this.hashed = hashed;
        return hashed;
    }

    /**
     * This method uses a Secure Random pseudorandom generation method to generate a byte array to be used as the password
     * salt.
     * @return The password salt
     */
    private static byte[] getSalt() {
        String saltstring = "Zach, you should try commenting your code.";
        return saltstring.getBytes();
    }

    /**
     * {@code cleanupCrew} will scrub the instantiated object of any and all secure data,
     * preparing it for next use.
     */
    public void cleanupCrew() {
        this.hashed = null;
    }
}
