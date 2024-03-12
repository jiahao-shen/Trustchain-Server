package com.trustchain.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUtil {
    public static String encrypt(String plain) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        return encoder.encode(plain);
    }

    public static boolean match(String plain, String cipher) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        return encoder.matches(plain, cipher);
    }
}
