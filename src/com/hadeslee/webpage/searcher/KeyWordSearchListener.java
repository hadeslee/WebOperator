/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hadeslee.webpage.searcher;

import java.util.EventListener;

/**
 * 对搜索事件感兴趣的对象可以实现此接口
 * @author Hadeslee
 */
public interface KeyWordSearchListener extends EventListener {

    /**
     * 开始搜索某个关键字了，这个时候UI可以在listbar上面
     * 添加一条记录了
     * @param keyword
     */
    public void keywordStartSearch(String keyword);

    /**
     * 为某个关键字搜到了某篇文章，这个时候UI可以在JTabbedPane上面
     * 添加一个Tab了
     * @param keyword 某个关键字
     * @param article 新找到的文章内容
     */
    public void articleSearched(String keyword, Article article);

    /**
     * 所有的搜索已经完成
     * @param keyWordSearcher 哪个搜索器搜索完成了
     */
    public void searchDone(KeyWordSearcher keyWordSearcher);
}
