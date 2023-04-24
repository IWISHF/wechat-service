package com.merkle.wechat.util;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

public class PasswordUtil {
    private static String password = "ELafsddfsiticSSSearchIsXS";

    public static String encryptString(String content) {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword(password);
        return encryptor.encrypt(content);
    }

    public static String decryptString(String content) {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword(password);
        return encryptor.decrypt(content);
    }

    public static void main(String[] args) {
        System.out.println(encryptString("123456"));
    }
}
