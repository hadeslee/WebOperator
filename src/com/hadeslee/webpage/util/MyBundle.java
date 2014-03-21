/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hadeslee.webpage.util;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 *
 * @author Hadeslee
 */
public class MyBundle {

    private static final ResourceBundle rb = ResourceBundle.getBundle("com/hadeslee/webpage/resource/Message", Locale.CHINA);
//    private static final ResourceBundle rb = ResourceBundle.getBundle("com/hadeslee/webpage/resource/Message", Locale.TAIWAN);

    public static String format(String key, Object... params) {
        return MessageFormat.format(getString(key), params);
    }

    public static String getString(String key) {
        try {
            return rb.getString(key);
        } catch (Exception ex) {
            ex.printStackTrace();
            return key;
        }
    }
}
