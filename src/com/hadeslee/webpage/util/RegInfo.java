/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hadeslee.webpage.util;

import java.io.Serializable;
import java.util.Map;

/**
 *
 * @author Hadeslee
 */
public class RegInfo implements Serializable {

    private int restTime = 0;
    private String password = "";

    public RegInfo() {
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRestTime() {
        return restTime;
    }

    public void setRestTime(int restTime) {
        this.restTime = restTime;
    }

    public static void main(String[] args) {
        Map<String, String> map = System.getenv();
        for (String s : map.keySet()) {
            System.out.println(s + ":" + map.get(s));
        }
    }
}
