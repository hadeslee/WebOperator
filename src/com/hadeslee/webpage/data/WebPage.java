/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hadeslee.webpage.data;

import com.hadeslee.webpage.template.KeywordContentTemplate;
import com.hadeslee.webpage.template.KeywordListTemplate;
import com.hadeslee.webpage.util.Setting;
import com.hadeslee.webpage.util.Util;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 一个页面的代表对象，此对象都有默认值，改变一下对象
 * @author Hadeslee
 */
public class WebPage implements Serializable {

    private static final long serialVersionUID = 20090920L;
    private List<String> keywords = new ArrayList<String>();//一个关键字的列表
    private String originalContent = "";//原始的内容
    private KeywordContentTemplate keywordContentTemplate;//关键字内容的模版对象
    private KeywordListTemplate keywordListTemplate;//关键字内容的模版对象
    private Setting setting = Setting.getInstance();
    private File mainFile;//主要文件保存在哪里，其它的文件都是相对这个文件目录的

    public WebPage() {
    }

    public String getAllCodeContent(String hn) {
        StringBuilder sb = new StringBuilder();
        sb.append(getTitle()).append("\n");
        sb.append(getMetaKeywords()).append("\n");
        sb.append(getMetaDescription()).append("\n");
        sb.append(getNavigation()).append("\n");
        sb.append(getCenterH(hn)).append("\n");
        sb.append(Util.getCurrentFriendLinkContent());
        return sb.toString();
    }

    public String getNewContent(Setting setting) {
        return Util.insertContent(originalContent, keywords, setting);
    }

    public String getTitle() {
        return Util.getTitle(keywords);
    }

    public String getMetaDescription() {
        return Util.getDescription(keywords);
    }

    public String getMetaKeywords() {
        return Util.getKeywords(keywords);
    }

    public String getNavigation() {
        return Util.getNavigation(keywords);
    }

    public String getCenterH(String hn) {
        return Util.getCenterH(hn, keywords);
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public void setOriginalContent(String originalContent) {
        this.originalContent = originalContent;
    }

    public String getOriginalContent() {
        return originalContent;
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

    public Setting getSetting() {
        return setting;
    }

    public void setSetting(Setting setting) {
        this.setting = setting;
    }

    public File getMainFile() {
        return mainFile;
    }

    public void setMainFile(File mainFile) {
        this.mainFile = mainFile;
    }
}
