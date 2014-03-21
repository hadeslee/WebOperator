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
 * ģ����ĸ���
 * @author Hadeslee
 */
public abstract class Template implements Serializable {

    private static final long serialVersionUID = 20090922L;
    protected String content;//ģ�������
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
     * һ��������ȥʵ�ֵķ���������һ�����Է��ر�ģ��
     * ��Ҫ��ֵ��ӵ���ߣ���ʵ���и������Ժ󷵻ظ���
     * ��template���ɵ��µ�����
     * @param holder ģ�����ֵ��ӵ����
     * @return Ӧ��ģ���Ժ������
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
