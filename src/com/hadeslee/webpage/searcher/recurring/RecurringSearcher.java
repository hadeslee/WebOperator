/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hadeslee.webpage.searcher.recurring;

import com.hadeslee.webpage.searcher.Article;
import com.hadeslee.webpage.searcher.KeyWordSearchListener;
import com.hadeslee.webpage.searcher.KeyWordSearcher;
import com.hadeslee.webpage.template.KeywordContentTemplate;
import com.hadeslee.webpage.template.KeywordListTemplate;
import com.hadeslee.webpage.template.holder.KeywordContentValueHolder;
import com.hadeslee.webpage.template.holder.KeywordListValueHolder;
import com.hadeslee.webpage.ui.panel.RecurringSearchListener;
import com.hadeslee.webpage.util.MyBundle;
import com.hadeslee.webpage.util.ReplaceInfo;
import com.hadeslee.webpage.util.Setting;
import com.hadeslee.webpage.util.Util;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 * 循环搜索器里面的一环
 * @author Hadeslee
 */
public class RecurringSearcher implements Runnable, KeyWordSearchListener {

    private static final Logger logger = Logger.getLogger(RecurringSearcher.class.getName());
    private final SearchContent searchContent;//要搜索的内容，包括文件，等等
    private final Setting setting;
    private final RecurringSearchListener panel;
    private List<SearchContent> nextContents;//由此环产生的可被继续搜索的列表
    //用于关键字搜索的搜索器，一个关键字对应一个
    private List<KeyWordSearcher> keywordSearchers = new ArrayList<KeyWordSearcher>();
    private int currentIndex;//当前正在搜索的下标
    private Thread worker = null;//工作线程
    private volatile boolean go = true;
    private volatile boolean finished = false;

    public RecurringSearcher(SearchContent searchContent, Setting setting, RecurringSearchListener panel) {
        this.searchContent = searchContent;
        this.setting = setting;
        this.panel = panel;
        nextContents = new ArrayList<SearchContent>();
    }

    public List<SearchContent> getNextContents() {
        return nextContents;
    }

    public boolean isFinished() {
        return finished;
    }

    private void startFetchKeywordContent() {
        currentIndex = -1;
        for (KeyWordSearcher keyWordSearcher : keywordSearchers) {
            keyWordSearcher.removeKeyWordSearchListener(this);
            keyWordSearcher.cancel();
        }
        keywordSearchers.clear();
        System.gc();
        System.gc();
        System.gc();
        List<String> keywords = searchContent.getKeywordListProvider().getKeywordList();
        logger.info(MyBundle.getString("searched.keyword.list") + keywords);
        ReplaceInfo pro = null;
        if (setting.isOriginalContent()) {
            pro = Util.readReplaceInfo();
        }
        logger.info("ReplaceInfo = " + pro);
        for (String s : keywords) {
            KeyWordSearcher keyWordSearcher = new KeyWordSearcher(s, setting.getKeywordPageCount(), pro, setting);
            keyWordSearcher.addKeyWordSearchListener(this);
            keywordSearchers.add(keyWordSearcher);
        }
        if (keywordSearchers.size() > 0) {
            currentIndex = 0;
            keywordSearchers.get(0).startSearch();
        } else {
            JOptionPane.showMessageDialog(null, MyBundle.getString("has.no.keyword.to.search"));
        }
    }

    public void start() {
        if (worker == null) {
            worker = new Thread(this);
            worker.start();
        }
    }

    public void run() {
        startFetchKeywordContent();
    }

    public void keywordStartSearch(String keyword) {
        panel.keywordStartSearch(keyword);
    }

    public void articleSearched(String keyword, Article article) {
        panel.articleSearched(keyword, article);
    }

    private void saveMainFile() {
        String content = Util.insertContent(Util.readFile(searchContent.getMainFile(),
                setting.getReadEncoding()),
                searchContent.getKeywordListProvider().getKeywordList(),
                setting);
        Util.saveFile(content, setting.getWriteEncoding(), searchContent.getMainFile());
    }

    private File saveKeywordList(KeyWordSearcher searcher) {
        KeywordListTemplate template = searchContent.getKeywordListTemplate();
        KeywordListValueHolder listHolder = new KeywordListValueHolder(searcher, searchContent.getMainFile(), searchContent.getKeywordListProvider().getKeywordList());
        String temp = template.applyTemplateValue(listHolder);
        File file = getKeywordListPageFile(searcher.getKeyword());
        panel.appendInfo(MyBundle.format("saving.file.to", file.getPath()));
        Util.saveFile(temp, setting.getWriteEncoding(), file);
        return file;
    }

    private void saveKeyWordContents(KeyWordSearcher searcher) {
        KeywordContentTemplate template = searchContent.getKeywordContentTemplate();
        for (Article article : searcher.getContent()) {
            KeywordContentValueHolder holder = new KeywordContentValueHolder(article, searchContent.getKeywordListProvider().getKeywordList());
            String temp = template.applyTemplateValue(holder);
            File file = getKeywordContentPageFile(searcher.getKeyword(), article);
            Util.saveFile(temp, setting.getWriteEncoding(), file);
        }
    }

    private File getKeywordContentPageFile(String keyword, Article article) {
        File mainFile = searchContent.getMainFile();
        File file = new File(mainFile.getParent() + "/" + Util.getPinyin(keyword), Util.generateUniqueName(article.getTitle(), "htm", mainFile.getParentFile()));
        file.getParentFile().mkdirs();
        return file;
    }

    private File getKeywordListPageFile(String keyword) {
        File mainFile = searchContent.getMainFile();
        File file = new File(mainFile.getParent(), Util.getPinyin(keyword) + ".htm");
        file.getParentFile().mkdirs();
        return file;
    }

    public void searchDone(KeyWordSearcher keyWordSearcher) {
        panel.searchDone(keyWordSearcher);
        File mainFile = saveKeywordList(keyWordSearcher);
        saveKeyWordContents(keyWordSearcher);
        SearchContent sc = new SearchContent(setting);
        sc.setKeywordContentTemplate(searchContent.getKeywordContentTemplate());
        sc.setKeywordListTemplate(searchContent.getKeywordListTemplate());
        sc.setKeywordListProvider(new SearchKeyWordListProvider(keyWordSearcher.getKeyword(), setting));
        sc.setMainFile(mainFile);
        nextContents.add(sc);

        if (currentIndex + 1 < keywordSearchers.size()) {
            currentIndex += 1;
            keywordSearchers.get(currentIndex).startSearch();
        } else {
            saveMainFile();
            panel.allSearched();
            finished = true;
        }
    }
}
