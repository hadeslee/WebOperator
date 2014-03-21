/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hadeslee.webpage.data;

import java.io.File;
import java.io.Serializable;

/**
 * һ����������Ķ��󣬴˶�����Ը��õػ�ԭ����
 * @author Hadeslee
 */
public class WorkSpace implements Serializable {

    private static final long serialVersionUID = 20090920L;
    private WebPage webPage = new WebPage();
    private boolean searchKeyWord = true;
    private String keywordInput = "";
    private String jTextArea1;
    private String webContentJTA;
    private File openFile;//�򿪵�ԭʼ��ҳ�ļ�
    private File keywordListTemplate;//�򿪵Ĺؼ����б�ģ��
    private File keywordContentTemplate;//�򿪵Ĺؼ�������ģ��
    private transient File fromFile;//�������Ǵ��������

    public WorkSpace() {
    }

    public File getKeywordContentTemplate() {
        return keywordContentTemplate;
    }

    public void setKeywordContentTemplate(File keywordContentTemplate) {
        this.keywordContentTemplate = keywordContentTemplate;
    }

    public File getKeywordListTemplate() {
        return keywordListTemplate;
    }

    public void setKeywordListTemplate(File keywordListTemplate) {
        this.keywordListTemplate = keywordListTemplate;
    }

    public File getOpenFile() {
        return openFile;
    }

    public void setOpenFile(File openFile) {
        this.openFile = openFile;
    }

    public String getKeywordInput() {
        return keywordInput;
    }

    public void setKeywordInput(String keywordInput) {
        this.keywordInput = keywordInput;
    }

    public boolean isSearchKeyWord() {
        return searchKeyWord;
    }

    public void setSearchKeyWord(boolean searchKeyWord) {
        this.searchKeyWord = searchKeyWord;
    }

    public WebPage getWebPage() {
        return webPage;
    }

    public void setWebPage(WebPage webPage) {
        this.webPage = webPage;
    }

    public File getFromFile() {
        return fromFile;
    }

    public void setFromFile(File fromFile) {
        this.fromFile = fromFile;
    }

    public String getjTextArea1() {
        return jTextArea1;
    }

    public void setjTextArea1(String jTextArea1) {
        this.jTextArea1 = jTextArea1;
    }

    public String getWebContentJTA() {
        return webContentJTA;
    }

    public void setWebContentJTA(String webContentJTA) {
        this.webContentJTA = webContentJTA;
    }
}
