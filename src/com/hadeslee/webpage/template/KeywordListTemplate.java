/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hadeslee.webpage.template;

import com.hadeslee.webpage.template.replacer.WordListReplacer;
import com.hadeslee.webpage.template.replacer.WordReplacer;

/**
 *
 * @author Hadeslee
 */
public class KeywordListTemplate extends Template {

    public static final String KEYWORD_LIST = "关键词";
    public static final String TITLE_LIST = "文章标题";
    public static final String ARTICLE_LINK_LIST = "文章链接";
    public static final String KEYWORD = "关键词";

    public KeywordListTemplate(String content) {
        super(content);
        replacers.add(new WordListReplacer(KEYWORD_LIST, 1, 3));
        replacers.add(new WordListReplacer(TITLE_LIST, 1, 2));
        replacers.add(new WordListReplacer(ARTICLE_LINK_LIST, 1, 2));
        replacers.add(new WordReplacer(KEYWORD));
    }
}
