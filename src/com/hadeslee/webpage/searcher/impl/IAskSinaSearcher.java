/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hadeslee.webpage.searcher.impl;

import com.hadeslee.webpage.searcher.Article;
import com.hadeslee.webpage.util.MyBundle;
import com.hadeslee.webpage.util.Util;

/**
 *
 * @author hadeslee
 */
public class IAskSinaSearcher{

    public static final String NAME = MyBundle.getString("iask.sina");

    public IAskSinaSearcher(String keyword) {
    }

    public static void main(String[] args) {
        IAskSinaSearcher s = new IAskSinaSearcher("ÐÇÐÇ");
        Article a = s.readArticle("http://iask.sina.com.cn/b/17966850.html");
        System.out.println(a.getTitle());
        System.out.println("********************************");
        System.out.println(a.getContent());
    }

    protected Article readArticle(String url) {
        try {
            String s = Util.readUrl(url, "gb2312");
            String title = parseTitle(s);
            if (title != null) {
                String c = parseContent(s);
                if (c != null) {
                    Article a = new Article();
                    a.setTitle(title);
                    a.setContent(c);
                    return a;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private String parseTitle(String input) {
        String pattern = "(?<=<img src=\"http://i1.sinaimg.cn/pfp/ask/images/zhishi/ssr52a.gif\" alt=\"ÒÑ½â¾ö\" align=\"top\">)(.*?)(?=<span id='zsqprize'>)";
        String temp = Util.matcher(input, pattern);
        if (temp != null) {
            return Util.matcher(temp, "(?<=<h3>)(.*?)(?=</h3>)");
        }
        return null;
    }

    private String parseContent(String input) {
        String pattern = "(?<=<div class=\"cl_ans2\">)(.*?)(?=<div class=\"cl_ans\" id=\"iaskrfun\" style=\"display:none;\"></div>)";
        String temp = Util.matcher(input, pattern);
        if (temp != null) {
            return Util.matcher(temp, "(?<=<div class=\"usr_qus\">)(.*?)(?=<div id=)");
        }
        return null;
    }

    protected String getSite() {
        return "iask.sina.com.cn";
    }
}
