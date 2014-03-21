/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hadeslee.webpage.util;

import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author Hadeslee
 */
public class R {

    private static Properties p;

    static {
        loadProperties();
    }

    private static void loadProperties() {
        InputStream is = null;
        try {
            p = new Properties();
            is = R.class.getResourceAsStream("/resource.properties");
            p.load(is);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            Util.close(is);
        }
    }

    public static String getSaveCheckURL() {
        return p.getProperty("saveCheckURL", "http://www.baidu.com");
    }
    public static String getCheckRegURL() {
        return p.getProperty("checkRegURL", "http://www.baidu.com");
    }
    public static String getAuthorMeta() {
        return p.getProperty("authorMeta", "www.baidu.com,百度");
    }

    public static String getZhanQunURL() {
        return p.getProperty("zhanqunURL", "http://www.75pp.com/zhanqun.txt");
    }

    public static String getDateURL() {
        return p.getProperty("dateURL", "http://date.75pp.com/date.asp");
    }

    public static String getRegURL() {
        return p.getProperty("regURL", "http://www.75pp.com/reg.txt");
    }

    public static String getLinkURL() {
        return p.getProperty("linkURL", "http://www.75pp.com/link.txt");
    }

    public static String getDiZhiURL() {
        return p.getProperty("dizhiURL", "http://www.75pp.com/dizhi.txt");
    }

    public static String getSEURL() {
        return p.getProperty("seURL", "http://www.75pp.com/software/");
    }

    public static String getContactInfo() {
        return p.getProperty("contact", "QQ:195042644\nQQ:345736340\nQQ:313769402");
    }

    public static String getContactURL() {
        return p.getProperty("url", "http://www.75pp.com/dizhi.asp");
    }

    public static String getTitle() {
        return p.getProperty("title", "清伍seo优化软件 手工优化王 V 6.0 联系QQ：195042644");
    }
}
