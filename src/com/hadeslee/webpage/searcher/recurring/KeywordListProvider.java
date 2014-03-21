/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hadeslee.webpage.searcher.recurring;

import java.util.List;

/**
 * 关键字列表的提供者
 * 来源可能有两个
 * 1，手工输入
 * 2，根据输入的进行搜索
 * @author Hadeslee
 */
public interface KeywordListProvider {

    public List<String> getKeywordList();
}
