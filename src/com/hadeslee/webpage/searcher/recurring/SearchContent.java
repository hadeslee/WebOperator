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
 * 搜索内容，用于构造搜索任务的
 * @author Hadeslee
 */
public class SearchContent {

    private KeywordListProvider keywordListProvider;//列表提供器，可能是手动输入也可能是搜索输入
    private File mainFile;//主要文件存的地方，然后搜到的关键字再以此为相对
    private KeywordContentTemplate keywordContentTemplate;//关键字内容模版的文件
    private KeywordListTemplate keywordListTemplate;//关键字列表模版的文件
    private final Setting setting;//设置

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
