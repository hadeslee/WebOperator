/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hadeslee.webpage.template.replacer;

import com.hadeslee.webpage.template.Template;
import com.hadeslee.webpage.template.holder.TemplateValueHolder;
import java.io.Serializable;

/**
 *
 * @author Hadeslee
 */
public interface Replacer extends Serializable {

    /**
     * 替换原来的内容
     * @param rawContent 原来内容
     * @param holder     持有template相关值的对象
     * @return 新的内容
     */
    public abstract String replace(String rawContent, TemplateValueHolder holder, Class<? extends Template> cls);
}
