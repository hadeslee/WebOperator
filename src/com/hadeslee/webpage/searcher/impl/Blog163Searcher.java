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
 * blog.163.com的搜索封装对象
 * @author Hadeslee
 */
public class Blog163Searcher   {

    private static final Pattern allPattern = Pattern.compile("(?<=<div class=\"mcnt ztag\">)(.*?)(?=yodaoad)", Pattern.MULTILINE | Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
    private static final Pattern titlePattern = Pattern.compile("(?<=<span class=\"tcnt\">)(.*?)(?=</span>)", Pattern.MULTILINE | Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
    public static final String NAME = MyBundle.getString("blog.163");

    public Blog163Searcher(String keyword) {
    }

    protected Article readArticle(String url) {
        try {
            String s = Util.readUrl(url, "GBK");
            Matcher m = allPattern.matcher(s);
            if (m.find()) {
                String temp = m.group();
                m = titlePattern.matcher(temp);
                if (m.find()) {
                    Article a = new Article();
                    a.setTitle(m.group());
                    a.setContent(temp.replaceAll("<span class=\"tcnt\">(.*?)</span>", ""));
                    return a;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    protected String getSite() {
        return "blog.163.com";
    }
}
