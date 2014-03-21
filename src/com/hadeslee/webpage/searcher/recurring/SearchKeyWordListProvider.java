/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hadeslee.webpage.searcher.recurring;

import com.hadeslee.webpage.util.Setting;
import com.hadeslee.webpage.util.Util;
import java.util.List;

/**
 *
 * @author Hadeslee
 */
public class SearchKeyWordListProvider implements KeywordListProvider {

    private String keyword;
    private Setting setting;
    private List<String> keywords;

    public SearchKeyWordListProvider(String keyword, Setting setting) {
        this.keyword = keyword;
        this.setting = setting;
        search();
    }

    private void search() {
        keywords = Util.searchKeyWords(setting,keyword);
    }

    public List<String> getKeywordList() {
        return keywords;
    }
}
