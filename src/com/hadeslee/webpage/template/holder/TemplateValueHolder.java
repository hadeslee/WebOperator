/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hadeslee.webpage.template.holder;

import com.hadeslee.webpage.template.*;
import java.io.Serializable;

/**
 * ������һ���ӿڣ�ͳһ�淶��������ģ�����ݵ�
 * �������Ҫʵ�ֵĽӿ�
 * @author Hadeslee
 */
public interface TemplateValueHolder extends Serializable {

    /**
     * ���ݴ����KEY���õ����KEY����Ӧ��ģ���ֵ
     * @param cls ģ����
     * @param key ������
     * @return ��Ӧ��ֵ
     */
    public String getTemplateValue(Class<? extends Template> cls, String key);

    /**
     * ���ݴ����KEY���Լ���ͬKEY���������õ�����Ҫֵ
     * ��Ϊ���ܻ���ͬ�������ֶ�Ӧ��ͬ������������
     * {�ؼ���1}��{�ؼ���2}��{�ؼ���3}
     * @param cls ģ����
     * @param key ������
     * @param index ��Ӧ������
     * @return
     */
    public String getTemplateValue(Class<? extends Template> cls, String key, int index);
}
