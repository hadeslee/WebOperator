/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hadeslee.webpage.searcher.impl;

import com.hadeslee.webpage.searcher.Article;
import com.hadeslee.webpage.searcher.RealSearcher;
import com.hadeslee.webpage.util.MyBundle;
import com.hadeslee.webpage.util.Util;
import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

/**
 *
 * @author Hadeslee
 */
public class WenwenSosoSearcher {

    private static final Pattern contentPattern_1 = Pattern.compile("(?<=<div class=\"sloved_answer\">)(.*?)(?=<div class=\"evaluation_wrap\">)", Pattern.DOTALL | Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
    private static final Pattern contentPattern_2 = Pattern.compile("(?<=<pre>)(.*?)(?=</pre>)", Pattern.DOTALL | Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
    private static final Pattern titlePattern = Pattern.compile("(?<=<h4 id=\"questionTitle\">)(.*?)(?=</h4>)", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
    public static final String NAME = MyBundle.getString("wenwen.soso");

    public WenwenSosoSearcher(String keyword) {
    }

    public static void main(String[] args) throws Exception {
//        WenwenSosoSearcher s = new WenwenSosoSearcher("ÐÇÐÇ");
//        Article a = s.readArticle("http://wenwen.soso.com/z/q96930838.htm");
//        System.out.println(a.getTitle());
//        System.out.println("********************************");
//        System.out.println(a.getContent());
        String content = Util.readFile(new File("js/wenwen.soso.com.js"), "gbk");
        ScriptEngineManager sem = new ScriptEngineManager();
        ScriptEngine se = sem.getEngineByExtension("js");
        if (se instanceof Invocable) {
            Invocable invoke = (Invocable) se;
            se.eval(content);
            RealSearcher ie = invoke.getInterface(RealSearcher.class);
            Article a = ie.readArticle("http://wenwen.soso.com/z/q96930838.htm");
            System.out.println(a.getTitle());
            System.out.println("********************************");
            System.out.println(a.getContent());
        }
    }

    protected Article readArticle(String url) {
        try {
            String s = Util.readUrl(url, "UTF-8");
            String sTitle = getTitle(s);
            String sContent = getContent(s);
            if (Util.isEmpty(sTitle) || Util.isEmpty(sContent)) {
                return null;
            } else {
                Article a = new Article();
                a.setTitle(sTitle);
                a.setContent(sContent);
                return a;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private String getTitle(String s) {
        Matcher m = titlePattern.matcher(s);
        if (m.find()) {
            String temp = m.group();
            temp = temp.replace("<h3>", "");
            return temp.trim();
        }
        return null;
    }

    private String getContent(String s) {
        Matcher m = contentPattern_1.matcher(s);
        if (m.find()) {
            String temp = m.group();
            m = contentPattern_2.matcher(temp);
            if (m.find()) {
                return m.group();
            }
        }
        return null;
    }

    protected String getSite() {
        return "wenwen.soso.com";
    }
}
