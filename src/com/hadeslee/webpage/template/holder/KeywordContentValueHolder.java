/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hadeslee.webpage.template.holder;

import com.hadeslee.webpage.searcher.Article;
import com.hadeslee.webpage.template.KeywordContentTemplate;
import com.hadeslee.webpage.template.Template;
import java.util.List;

/**
 *
 * @author Hadeslee
 */
public class KeywordContentValueHolder implements TemplateValueHolder {

    private Article article;
    private List<String> keywords;

    public KeywordContentValueHolder(Article article, List<String> keywords) {
        this.article = article;
        this.keywords = keywords;
    }

    public String getTemplateValue(Class<? extends Template> cls, String key) {
        if (cls == KeywordContentTemplate.class) {
            if (KeywordContentTemplate.CONTENT.equals(key)) {
                return article.getContent();
            } else if (KeywordContentTemplate.TITLE.equals(key)) {
                return article.getTitle();
            } else {
                return "??［未知变量:" + key + "］??";
            }
        } else {
            return "??［错误类别:" + cls + "］??";
        }
    }

    public String getTemplateValue(Class<? extends Template> cls, String key, int index) {
        if (cls == KeywordContentTemplate.class) {
            if (KeywordContentTemplate.KEYWORD_LIST.equals(key)) {
                List<String> list = keywords;
                if (index <= list.size()) {
                    return list.get(index - 1);
                } else {
                    return "";
                }
            } else {
                return "??［未知变量:" + key + "］??";
            }
        } else {
            return "??［错误类别:" + cls + "］??";
        }
    }
}
