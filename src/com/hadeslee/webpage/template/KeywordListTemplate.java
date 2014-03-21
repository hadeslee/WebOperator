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

    public static final String KEYWORD_LIST = "�ؼ���";
    public static final String TITLE_LIST = "���±���";
    public static final String ARTICLE_LINK_LIST = "��������";
    public static final String KEYWORD = "�ؼ���";

    public KeywordListTemplate(String content) {
        super(content);
        replacers.add(new WordListReplacer(KEYWORD_LIST, 1, 3));
        replacers.add(new WordListReplacer(TITLE_LIST, 1, 2));
        replacers.add(new WordListReplacer(ARTICLE_LINK_LIST, 1, 2));
        replacers.add(new WordReplacer(KEYWORD));
    }
}
