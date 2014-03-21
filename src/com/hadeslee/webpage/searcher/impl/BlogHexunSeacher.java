/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hadeslee.webpage.searcher.impl;

import com.hadeslee.webpage.searcher.MySearcher;
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
public class BlogHexunSeacher {

    //<h1>¿Õµ÷²¡</h1>
    private static final Pattern allPattern = Pattern.compile("(?<=<div class=\"Article\" id=\"ArticeTextID\">)(.*?)(?=<div class=\"moreArticles\">)", Pattern.DOTALL | Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
    private static final Pattern titlePattern = Pattern.compile("(?<=<span class=\"ArticleTitleText\">)(.*?)(?=</span>)");
    public static final String NAME = MyBundle.getString("blog.hexun");

    public BlogHexunSeacher(String keyword) {
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
        return "blog.hexun.com";
    }
    
    public static void main(String[] args) {
        BlogHexunSeacher a = new BlogHexunSeacher("ÊÖ»ú");
        Article at = a.readArticle("http://uir.blog.hexun.com/11780241_d.html");
        System.out.println(at.getContent());
    }
}
