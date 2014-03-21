/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hadeslee.webpage.searcher.impl;

import com.hadeslee.webpage.searcher.Article;
import com.hadeslee.webpage.util.MyBundle;
import com.hadeslee.webpage.util.Util;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Hadeslee
 */
public class HiBaiduSearcher {

    private static final Pattern allPattern = Pattern.compile("(?<=<div id=\"m_blog\" class=\"modbox\" style=\"overflow-x:hidden;\">)(.*?)(?=<div class=\"opt\">)", Pattern.DOTALL | Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
    private static final Pattern titlePattern = Pattern.compile("(?<=<div class=\"tit\">)(.*?)(?=</div>)", Pattern.DOTALL | Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
    public static final String NAME = MyBundle.getString("hi.baidu");

    public HiBaiduSearcher(String keyword) {
    }

    protected Article readArticle(String url) {
        try {
            String s = Util.readUrl(url, "gb2312");
            Matcher m = allPattern.matcher(s);
            if (m.find()) {
                String temp = m.group();
                m = titlePattern.matcher(temp);
                if (m.find()) {
                    Article a = new Article();
                    a.setTitle(m.group().trim());
                    a.setContent(temp.replaceAll("<div class=\"tit\">(.*?)</div>", ""));
                    return a;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    protected String getSite() {
        return "hi.baidu.com";
    }
}
