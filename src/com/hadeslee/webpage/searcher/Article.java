/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hadeslee.webpage.searcher;

import java.io.Serializable;

/**
 * һƪ���µĴ������
 * @author Hadeslee
 */
public class Article implements Serializable {

    private String title;//����
    private String content;//����

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
