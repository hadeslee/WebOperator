/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hadeslee.webpage.template.replacer;

import com.hadeslee.webpage.template.Template;
import com.hadeslee.webpage.template.holder.TemplateValueHolder;

/**
 *
 * @author Hadeslee
 */
public class WordReplacer implements Replacer {

    private static final long serialVersionUID = 20090922L;
    private String word;

    public WordReplacer(String word) {
        this.word = word;
    }

    @Override
    public String replace(String rawContent, TemplateValueHolder holder, Class<? extends Template> cls) {
        String value = holder.getTemplateValue(cls, word);
        rawContent = rawContent.replace("{" + word + "}", value);
        return rawContent;
    }
}
