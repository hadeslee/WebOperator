/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hadeslee.webpage.template.replacer;

import com.hadeslee.webpage.template.Template;
import com.hadeslee.webpage.template.holder.TemplateValueHolder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Hadeslee
 */
public class WordListReplacer implements Replacer {

    private static final long serialVersionUID = 20090922L;
    private String word;
    private int minLength, maxLength;

    public WordListReplacer(String word, int minLength, int maxLength) {
        this.word = word;
        this.minLength = minLength;
        this.maxLength = maxLength;
    }

    @Override
    public String replace(String rawContent, TemplateValueHolder holder, Class<? extends Template> cls) {
        Matcher m = Pattern.compile("\\{" + word + "(\\d{" + minLength + "," + maxLength + "})\\}").matcher(rawContent);
        while (m.find()) {
            int index = Integer.parseInt(m.group(1));
            String value = holder.getTemplateValue(cls, word, index);
            rawContent = rawContent.replace("{" + word + index + "}", value);
        }
        System.gc();
        return rawContent;
    }
}
