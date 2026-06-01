package com.okul.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

    private PasswordUtil() {
    }

    public static String hash(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    public static boolean check(String plainPassword, String hash) {
        if (hash == null || hash.isEmpty()) {
            return false;
        }
        return BCrypt.checkpw(plainPassword, hash);
    }
}
