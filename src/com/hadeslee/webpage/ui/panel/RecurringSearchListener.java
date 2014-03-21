/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hadeslee.webpage.ui.panel;

import com.hadeslee.webpage.searcher.Article;
import com.hadeslee.webpage.searcher.KeyWordSearcher;

/**
 *
 * @author hadeslee
 */
public interface RecurringSearchListener {

    public void keywordStartSearch(String keyword);

    public void articleSearched(String keyword, Article article);

    public void appendInfo(String info);

    public void searchDone(KeyWordSearcher keyWordSearcher);

    public void allSearched();
}
