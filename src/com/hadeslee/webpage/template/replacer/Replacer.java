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
     * �滻ԭ��������
     * @param rawContent ԭ������
     * @param holder     ����template���ֵ�Ķ���
     * @return �µ�����
     */
    public abstract String replace(String rawContent, TemplateValueHolder holder, Class<? extends Template> cls);
}
