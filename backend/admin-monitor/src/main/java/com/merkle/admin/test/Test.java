package com.merkle.admin.test;

import java.io.UnsupportedEncodingException;

import org.springframework.util.Base64Utils;

public class Test {

    public static void main(String[] args) throws UnsupportedEncodingException {
        String from = "中文-23❤4";
        String nickname = "Pz8=";
        String newFrom =new String(Base64Utils.encodeToString(from.getBytes("ISO-8859-1")));
        System.out.println(newFrom);
        System.out.println(new String(Base64Utils.decodeFromString(newFrom)));
        System.out.println(Test.class.getPackage().getName());
    }

}
