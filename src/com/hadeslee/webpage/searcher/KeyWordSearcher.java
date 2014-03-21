/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hadeslee.webpage.searcher;

import com.hadeslee.webpage.util.ReplaceInfo;
import com.hadeslee.webpage.util.Setting;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

/**
 * TODO 这里应该考虑一下，Searcher要不要重用的问题，是设计成 immutable对象，还是设计成mutable对象
 *
 * @author Hadeslee
 */
public class KeyWordSearcher implements Serializable, Runnable {

    private static final long serialVersionUID = 20090922L;
    private String keyword;
    private List<Article> content = new ArrayList<Article>();//搜到的关键字列表
    private boolean done;//是否搜索完成
    private boolean canceled;//是否被取消了
    private List<MySearcher> searchers = new ArrayList<MySearcher>();
    private transient List<KeyWordSearchListener> keyWordSearchListeners = new ArrayList<KeyWordSearchListener>();
    private boolean started;//是否已经启动了
    private int maxSearchCount;//最多搜索页面数量
    private ReplaceInfo pro;

    public KeyWordSearcher(String keyword, int maxSearchCount, ReplaceInfo pro, Setting setting) {
        this.keyword = keyword;
        this.maxSearchCount = maxSearchCount;
        this.pro = pro;
        Vector<MySearcher> v = setting.getSearchers();
        for (int i = 0; i < v.size(); i++) {
            searchers.add(v.get(i));
        }
    }

    public void addKeyWordSearchListener(KeyWordSearchListener l) {
        keyWordSearchListeners.add(l);
    }

    public void removeKeyWordSearchListener(KeyWordSearchListener l) {
        keyWordSearchListeners.remove(l);
    }

    void keywordStartSearch(String keyword) {
        for (KeyWordSearchListener l : keyWordSearchListeners) {
            l.keywordStartSearch(keyword);
        }
    }

    synchronized void articleSearched(String keyword, Article article) {
        if (content.size() < maxSearchCount) {
            content.add(article);
            for (KeyWordSearchListener l : keyWordSearchListeners) {
                l.articleSearched(keyword, article);
            }
        }
    }

    void searchDone() {
        for (KeyWordSearchListener l : keyWordSearchListeners) {
            l.searchDone(this);
        }
    }

    public boolean isCanceled() {
        return canceled;
    }

    private void reset() {
        done = false;
        canceled = false;
        content.clear();
    }

    public void cancel() {
        canceled = true;
        for (MySearcher searcher : searchers) {
            searcher.cancel();
        }
    }

    public synchronized void startSearch() {
        if (!started) {
            started = true;
            new Thread(this).start();
        }
    }

    public void run() {
        reset();
        keywordStartSearch(keyword);
        out:
        for (MySearcher searcher : searchers) {
            searcher.searchKeyWord(keyword, this, pro, maxSearchCount - content.size());
            if (content.size() >= maxSearchCount) {
                break out;
            }
        }
        searchDone();
        System.out.println("searchDone,keyword＝" + keyword + ",content.size=" + content.size());
    }

    public List<Article> getContent() {
        return Collections.unmodifiableList(content);
    }

    public boolean isDone() {
        return done;
    }

    public String getKeyword() {
        return keyword;
    }
}
