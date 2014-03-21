/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hadeslee.webpage.util;

import java.security.MessageDigest;
import java.text.MessageFormat;
import java.util.ResourceBundle;

/**
 *
 * @author hadeslee
 */
public class Test2 {

    private static String md5_3(String input) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] tmp = md.digest((input).getBytes());
        tmp = md.digest(tmp);
        tmp = md.digest(tmp);
        return byteToHex(tmp);
    }

    private static String byteToHex(byte[] tmp) {
        StringBuilder dist = new StringBuilder();
        try {
            for (byte b : tmp) {
                dist.append(String.format("%02X", b));
            }
        } catch (Exception ex) {
            dist.append(String.format("%032d", System.currentTimeMillis()));
        }
        return dist.toString().toUpperCase();
    }

    private static String getString(String pwd, String code) throws Exception {
        return md5(md5_3(pwd) + code.toUpperCase()).toUpperCase();
    }

    public static String md5(String source) {
        if (source == null) {
            return null;
        } else if (source.trim().equals("")) {
            return "";
        }
        StringBuilder dist = new StringBuilder();
        try {
            byte[] tmp = MessageDigest.getInstance("MD5").digest((source).getBytes());
            for (byte b : tmp) {
                dist.append(String.format("%02X", b));
            }
        } catch (Exception ex) {
            dist.append(String.format("%032d", System.currentTimeMillis()));
        }
        return dist.toString().toLowerCase();
    }



    public static void main(String[] args) throws Exception {
        String s =MessageFormat.format(R.getSaveCheckURL(), "aaaaaa","bbbbb","cccccc");
        System.out.println(s);
    }
}
