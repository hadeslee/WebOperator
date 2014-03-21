/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hadeslee.webpage.searcher.recurring;

import com.hadeslee.webpage.template.KeywordContentTemplate;
import com.hadeslee.webpage.template.KeywordListTemplate;
import com.hadeslee.webpage.util.Setting;
import java.io.File;

/**
 * �������ݣ����ڹ������������
 * @author Hadeslee
 */
public class SearchContent {

    private KeywordListProvider keywordListProvider;//�б��ṩ�����������ֶ�����Ҳ��������������
    private File mainFile;//��Ҫ�ļ���ĵط���Ȼ���ѵ��Ĺؼ������Դ�Ϊ���
    private KeywordContentTemplate keywordContentTemplate;//�ؼ�������ģ����ļ�
    private KeywordListTemplate keywordListTemplate;//�ؼ����б�ģ����ļ�
    private final Setting setting;//����

    public SearchContent(Setting setting) {
        this.setting = setting;
    }

    public File getMainFile() {
        return mainFile;
    }

    public void setMainFile(File mainFile) {
        this.mainFile = mainFile;
    }

    public KeywordListProvider getKeywordListProvider() {
        return keywordListProvider;
    }

    public void setKeywordListProvider(KeywordListProvider keywordListProvider) {
        this.keywordListProvider = keywordListProvider;
    }

    public KeywordContentTemplate getKeywordContentTemplate() {
        return keywordContentTemplate;
    }

    public void setKeywordContentTemplate(KeywordContentTemplate keywordContentTemplate) {
        this.keywordContentTemplate = keywordContentTemplate;
    }

    public KeywordListTemplate getKeywordListTemplate() {
        return keywordListTemplate;
    }

    public void setKeywordListTemplate(KeywordListTemplate keywordListTemplate) {
        this.keywordListTemplate = keywordListTemplate;
    }
}
