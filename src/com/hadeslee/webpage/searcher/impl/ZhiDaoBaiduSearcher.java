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
public class ZhiDaoBaiduSearcher {

    private static final Pattern contentPattern = Pattern.compile("(?<=<pre id=\"best-answer-content\" class=\"reply-text mb10\">)(.*?)(?=</pre>)", Pattern.DOTALL | Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
    private static final Pattern titlePattern = Pattern.compile("(?<=<span class=\"question-title\">)(.*?)(?=</span>)");
    public static final String NAME = MyBundle.getString("baidu.zhidao");

    public ZhiDaoBaiduSearcher(String keyword) {
    }

    public static void main(String[] args) {
        ZhiDaoBaiduSearcher s = new ZhiDaoBaiduSearcher("ÐÇÐÇ");
        Article a = s.readArticle("http://zhidao.baidu.com/question/95443841.html");
        System.out.println(a.getTitle());
        System.out.println("********************************");
        System.out.println(a.getContent());
    }

    protected Article readArticle(String url) {
        try {
            String s = Util.readUrl(url, "gb2312");
            Matcher m = titlePattern.matcher(s);
            if (m.find()) {
                String sTitle = m.group();
                m = contentPattern.matcher(s);
                if (m.find()) {
                    String sContent = m.group();
                    Article a = new Article();
                    a.setTitle(sTitle);
                    a.setContent(sContent);
                    return a;
                }
            }
        } catch (Exception ex) {
        }
        return null;
    }

    protected String getSite() {
        return "zhidao.baidu.com";
    }
}
