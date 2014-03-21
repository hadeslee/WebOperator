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
public class BaiKeBaiduSearcher{

    //<h1>¿Õµ÷²¡</h1>
    private static final Pattern allPattern = Pattern.compile("(?<=<div class=\"text\">)(.*?)(?=<div class=\"end\">)", Pattern.DOTALL | Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
    private static final Pattern titlePattern = Pattern.compile("(?<=<h1 class=\"title\">)(.*?)(?=</h1>)");
    public static final String NAME = MyBundle.getString("baidu.baike");

    public BaiKeBaiduSearcher(String keyword) {
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
                    a.setContent(temp.replaceAll("<h1 class=\"title\">(.*?)</h1>", ""));
                    return a;
                }
            }
        } catch (Exception ex) {
        }
        return null;
    }

    protected String getSite() {
        return "baike.baidu.com";
    }
}
