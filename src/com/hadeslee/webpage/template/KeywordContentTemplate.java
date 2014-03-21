/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hadeslee.webpage.template;

import com.hadeslee.webpage.template.holder.TemplateValueHolder;
import com.hadeslee.webpage.template.replacer.WordListReplacer;
import com.hadeslee.webpage.template.replacer.WordReplacer;
import java.io.File;
import java.net.URLClassLoader;
import java.util.concurrent.ForkJoinPool;

/**
 * 关键字内容的模版对象，此对象封装了一些对于模版的
 * 操作，比如传入相应的值，会自动应用模版生成一个新的
 * 内容出来
 * @author Hadeslee
 */
public class KeywordContentTemplate extends Template {

    public static final String KEYWORD_LIST = "关键词";
    public static final String TITLE = "文章标题";
    public static final String CONTENT = "文章内容";

    public KeywordContentTemplate(String content) {
        super(content);
        replacers.add(new WordListReplacer(KEYWORD_LIST, 1, 3));
        replacers.add(new WordReplacer(TITLE));
        replacers.add(new WordReplacer(CONTENT));
    }

    public static void main(String[] args) {
        String s = "中华人民共和国{关键词12}美国\n{关键词3}日本";
        KeywordContentTemplate k = new KeywordContentTemplate(s);
        String temp = k.applyTemplateValue(new TemplateValueHolder() {

            public String getTemplateValue(Class<? extends Template> cls, String key) {
                return "FUCK(Key=" + key + ")";
            }

            public String getTemplateValue(Class<? extends Template> cls, String key, int index) {
                return "FUCK(index=" + index + ")";
            }
        });
        System.out.println(temp);
    }
}
