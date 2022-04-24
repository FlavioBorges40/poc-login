package com.poc.util;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class PasswordUtil {

    public static String convertToBcryptPass(String password) {
        return BCrypt.withDefaults().hashToString(12, password.toCharArray());
    }

    public static Boolean verifyPassword(String password, String bcryptPassword) {
        return BCrypt.verifyer().verify(password.toCharArray(), bcryptPassword).verified;
    }

}
