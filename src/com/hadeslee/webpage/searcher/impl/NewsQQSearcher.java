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
public class NewsQQSearcher {

    public static final String NAME = MyBundle.getString("news.qq");

    public NewsQQSearcher(String keyword) {
    }

    public static void main(String[] args) {
        NewsQQSearcher s = new NewsQQSearcher("ÐÇÐÇ");
        Article a = s.readArticle("http://news.qq.com/a/20071214/001129.htm");
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
        String pattern = "(?<=<div id=\"ArticleTit\">)(.*?)(?=</div>)";
        return Util.matcher(input, pattern);
    }

    private String parseContent(String input) {
        String pattern = "(?<=<div id=\"ArticleCnt\">)(.*?)(?=<div id=\"ArtPLink\">)";
        String temp=  Util.matcher(input, pattern);
        if(temp!=null){
            return temp.replaceAll("(?sm)(?<=<div id=\"Reading\">)(.*?)(?=</table>\\s*</div>)", "");
        }
        return null;
    }

    protected String getSite() {
        return "news.qq.com";
    }
}
