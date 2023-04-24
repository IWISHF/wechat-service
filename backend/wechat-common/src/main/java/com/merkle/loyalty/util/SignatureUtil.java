package com.merkle.loyalty.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.TreeMap;

public class SignatureUtil {
    public static String getSignature(String url) {
        byte[] three = url.getBytes();
        String signature = null;
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
            byte[] thedigest = md.digest(three);
            StringBuilder buff = new StringBuilder();
            for (byte b : thedigest) {
                String conversion = Integer.toString(b & 0xFF, 16);
                while (conversion.length() < 2) {
                    conversion = "0" + conversion;
                }
                buff.append(conversion);
            }
            signature = buff.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return signature;
    }

    public static String getSignature(String secret, Map<String, String> vars) {
        Map<String, String> params = getSortedParameters(vars);
        String signature = null;

        StringBuilder sb = new StringBuilder(secret);
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue().toString();
            sb.append(key).append(value);
        }

        byte[] three = sb.toString().getBytes();
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
            byte[] thedigest = md.digest(three);
            StringBuilder buff = new StringBuilder();
            for (byte b : thedigest) {
                String conversion = Integer.toString(b & 0xFF, 16);
                while (conversion.length() < 2) {
                    conversion = "0" + conversion;
                }
                buff.append(conversion);
            }
            signature = buff.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return signature;
    }

    private static Map<String, String> getSortedParameters(Map<String, String> vars) {
        Map<String, String> parameters = new TreeMap<String, String>();
        for (Map.Entry<String, String> var : vars.entrySet()) {
            if (!var.getKey().toLowerCase().equals("sig")) {
                parameters.put(var.getKey(), var.getValue());
            }
        }
        return parameters;
    }

    // public static void main(String[] args) {
    // Map<String, String> vars = new HashedMap();
    // vars.put("uuid", "fgDkfrTura");
    // vars.put("external_customer_id","11111209091");
    //// vars.put("status", "active");
    //// vars.put("auto_enroll", "true");
    // System.out.println(getSignature("IfogMX1aTpIsDAeeedQLk3DoSiLRyfTM",
    // vars));
    // }
}
