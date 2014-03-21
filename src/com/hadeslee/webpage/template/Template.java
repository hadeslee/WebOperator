/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hadeslee.webpage.template;

import com.hadeslee.webpage.template.holder.TemplateValueHolder;
import com.hadeslee.webpage.template.replacer.Replacer;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 模版类的父类
 * @author Hadeslee
 */
public abstract class Template implements Serializable {

    private static final long serialVersionUID = 20090922L;
    protected String content;//模版的内容
    protected final List<Replacer> replacers = new ArrayList<Replacer>();

    public Template(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    /**
     * 一个由子类去实现的方法，传入一个可以返回本模版
     * 需要的值的拥有者，充实好行个参数以后返回根据
     * 此template生成的新的内容
     * @param holder 模版相关值的拥有者
     * @return 应用模版以后的内容
     */
    public String applyTemplateValue(TemplateValueHolder holder) {
        List<Replacer> list = getReplacerList();
        String temp = content;
        for (Replacer replacer : list) {
            temp = replacer.replace(temp, holder, this.getClass());
        }
        return temp;
    }

    public List<Replacer> getReplacerList() {
        return replacers;
    }
}
