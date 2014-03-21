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
 * �ؼ������ݵ�ģ����󣬴˶����װ��һЩ����ģ���
 * ���������紫����Ӧ��ֵ�����Զ�Ӧ��ģ������һ���µ�
 * ���ݳ���
 * @author Hadeslee
 */
public class KeywordContentTemplate extends Template {

    public static final String KEYWORD_LIST = "�ؼ���";
    public static final String TITLE = "���±���";
    public static final String CONTENT = "��������";

    public KeywordContentTemplate(String content) {
        super(content);
        replacers.add(new WordListReplacer(KEYWORD_LIST, 1, 3));
        replacers.add(new WordReplacer(TITLE));
        replacers.add(new WordReplacer(CONTENT));
    }

    public static void main(String[] args) {
        String s = "�л����񹲺͹�{�ؼ���12}����\n{�ؼ���3}�ձ�";
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
