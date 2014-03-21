/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hadeslee.webpage.searcher;

/**
 *
 * @author Hadeslee
 */
public interface RealSearcher {

    /**
     *
     * @param url
     * @return
     */
    public abstract Article readArticle(String url);

    /**
     *
     * @return
     */
    public abstract String getSite();

    public abstract String getName();
}
