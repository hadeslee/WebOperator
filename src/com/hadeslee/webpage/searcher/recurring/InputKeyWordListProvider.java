/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hadeslee.webpage.searcher.recurring;

import java.util.List;

/**
 *
 * @author Hadeslee
 */
public class InputKeyWordListProvider implements KeywordListProvider {

    private List<String> keywords;

    public InputKeyWordListProvider(List<String> keywords) {
        this.keywords = keywords;
    }

    public List<String> getKeywordList() {
        return keywords;
    }
}
