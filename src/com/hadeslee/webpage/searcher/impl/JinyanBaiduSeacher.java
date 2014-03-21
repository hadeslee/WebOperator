/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hadeslee.webpage.searcher.impl;

import com.hadeslee.webpage.searcher.Article;
import com.hadeslee.webpage.util.MyBundle;
import com.hadeslee.webpage.util.Util;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Hadeslee
 */
public class JinyanBaiduSeacher  {

    //<h1>空调病</h1>
    private static final Pattern allPattern = Pattern.compile("(?<=<section id=\"exp-detail\" class=\"border-padding\">)(.*?)(?=<h2>参考资料</h2>)", Pattern.DOTALL | Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
    private static final Pattern titlePattern = Pattern.compile("(?<=<span class=\"exp-title\">)(.*?)(?=</span>)");
    public static final String NAME = MyBundle.getString("jingyan.baidu");

    public JinyanBaiduSeacher(String keyword) {
    }

    protected Article readArticle(String url) {
        try {
            String s = Util.readUrl(url, "utf-8");
            Matcher m = allPattern.matcher(s);
            if (m.find()) {
                String temp = m.group();
                m = titlePattern.matcher(temp);
                if (m.find()) {
                    Article a = new Article();
                    a.setTitle(m.group());
                    a.setContent(temp);
                    return a;
                }
            }
        } catch (Exception ex) {
        }
        return null;
    }

    protected String getSite() {
        return "jingyan.baidu.com";
    }
    
    public static void main(String[] args) {
        JinyanBaiduSeacher a = new JinyanBaiduSeacher("手机");
        Article at = a.readArticle("http://jingyan.baidu.com/article/a3761b2b69c31d1577f9aa6e.html");
        System.out.println(at.getTitle());
    }
}
