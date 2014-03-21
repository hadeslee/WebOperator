/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hadeslee.webpage.template.holder;

import com.hadeslee.webpage.searcher.Article;
import com.hadeslee.webpage.searcher.KeyWordSearcher;
import com.hadeslee.webpage.template.KeywordListTemplate;
import com.hadeslee.webpage.template.Template;
import com.hadeslee.webpage.util.Util;
import java.io.File;
import java.util.List;

/**
 *
 * @author Hadeslee
 */
public class KeywordListValueHolder implements TemplateValueHolder {

    private File mainFile;
    private List<String> keywords;
    private KeyWordSearcher searcher;

    public KeywordListValueHolder(KeyWordSearcher searcher, File mainFile, List<String> keywords) {
        this.mainFile = mainFile;
        this.keywords = keywords;
        this.searcher = searcher;
    }

    public String getTemplateValue(Class<? extends Template> cls, String key) {
        if (cls == KeywordListTemplate.class) {
            if (KeywordListTemplate.KEYWORD.equals(key)) {
                return searcher.getKeyword();
            } else {
                return "??［未知变量:" + key + "］??";
            }
        } else {
            return "??［错误类别:" + cls + "］??";
        }
    }

    public String getTemplateValue(Class<? extends Template> cls, String key, int index) {
        if (cls == KeywordListTemplate.class) {
            if (KeywordListTemplate.KEYWORD_LIST.equals(key)) {
                List<String> list = keywords;
                if (index <= list.size()) {
                    return list.get(index - 1);
                } else {
                    return "";
                }
            } else if (KeywordListTemplate.TITLE_LIST.equals(key)) {
                List<Article> list = searcher.getContent();
                if (index <= list.size()) {
                    return list.get(index - 1).getTitle();
                } else {
                    return "";
                }
            } else if (KeywordListTemplate.ARTICLE_LINK_LIST.equals(key)) {
                List<Article> list = searcher.getContent();
                if (index <= list.size()) {
                    Article article = list.get(index - 1);
                    return Util.getPinyin(searcher.getKeyword()) + "/"
                            + Util.generateUniqueName(article.getTitle(), "htm",
                            new File(mainFile.getParent() + "/"
                            + Util.getPinyin(searcher.getKeyword())));
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
