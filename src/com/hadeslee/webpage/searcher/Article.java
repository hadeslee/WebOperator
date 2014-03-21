/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hadeslee.webpage.searcher;

import java.io.Serializable;

/**
 * 一篇文章的代表对象
 * @author Hadeslee
 */
public class Article implements Serializable {

    private String title;//标题
    private String content;//内容

    public Article() {
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String toString() {
        return "title = " + title + ",content = " + content;
    }
}
