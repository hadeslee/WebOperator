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
public class BlogAlibabaSearcher  {

    private static final Pattern allPattern = Pattern.compile("(?<=<div class=\"wz_06\">)(.*?)(?=<div align=\"left\" class=\"link_c\">	)", Pattern.DOTALL | Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
    private static final Pattern titlePattern = Pattern.compile("(?<=<h1>)(.*?)(?=<span class)");
    public static final String NAME = MyBundle.getString("blog.alibaba");

    public BlogAlibabaSearcher(String keyword) {
    }

    protected Article readArticle(String url) {
        try {
            String s = Util.readUrl(url, "gbk");
            Matcher m = allPattern.matcher(s);
            if (m.find()) {
                String temp = m.group();
                m = titlePattern.matcher(temp);
                if (m.find()) {
                    Article a = new Article();
                    a.setTitle(m.group());
                    a.setContent(temp.replaceAll("<h1>(.*?)</h1>", ""));
                    return a;
                }
            }
        } catch (Exception ex) {
        }
        return null;
    }

    protected String getSite() {
        return "blog.china.alibaba.com";
    }
    
    public static void main(String[] args) {
        BlogAlibabaSearcher a = new BlogAlibabaSearcher("ÊÖ»ú");
        Article at = a.readArticle("http://blog.china.alibaba.com/blog/bolin0797/article/b0-i25800598.html");
        System.out.println(at.getTitle());
        System.out.println("********************************");
        System.out.println(at.getContent());
    }
}
