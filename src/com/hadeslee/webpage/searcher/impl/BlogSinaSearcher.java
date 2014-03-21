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
public class BlogSinaSearcher {

    private static final Pattern allPattern = Pattern.compile("(?<=<div id=\"articlebody\")(.*?)(?=shareUp)", Pattern.DOTALL | Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
    private static final Pattern titlePattern = Pattern.compile("(?<=class=\"titName SG_txta\">)(.*?)(?=</h2>)", Pattern.DOTALL | Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
//    private static final Pattern timePattern = Pattern.compile("<span class=\"time\">(.*?)</span>", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
    public static final String NAME = MyBundle.getString("blog.sina");

    public BlogSinaSearcher(String keyword) {
    }

    protected Article readArticle(String url) {
        try {
            String s = Util.readUrl(url, "UTF-8");
            Matcher m = allPattern.matcher(s);
            if (m.find()) {
                String temp = m.group();
                m = titlePattern.matcher(temp);
                if (m.find()) {
                    String match = m.group();
                    Article a = new Article();
                    a.setTitle(match);
                    a.setContent(m.replaceAll("<div class=\"articleTag\">"));
                    return a;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

//    private String getTitle(String match) {
//        Matcher m = timePattern.matcher(match);
//        match = m.replaceAll("");
//        return Util.htmlTrim(match).replaceAll("\\p{Space}", "");
//
//    }
    protected String getSite() {
        return "blog.sina.com.cn";
    }
}
