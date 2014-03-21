/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hadeslee.webpage.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 查找替换的对象
 * @author Hadeslee
 */
public class ReplaceInfo implements Serializable {

    private static final long serialVersionUID = 20100102L;
    private int headTrimCount;//头部去掉的字数
    private int tailTrimCount;//尾部去掉的字数
    private List<ReplaceContent> replaceContents = new ArrayList<ReplaceContent>();

    public ReplaceInfo() {
    }

    public int getHeadTrimCount() {
        return headTrimCount;
    }

    public void setHeadTrimCount(int headTrimCount) {
        this.headTrimCount = headTrimCount;
    }

    public List<ReplaceContent> getReplaceContents() {
        return replaceContents;
    }

    public void setReplaceContents(List<ReplaceContent> replaceContents) {
        this.replaceContents = replaceContents;
    }

    public int getTailTrimCount() {
        return tailTrimCount;
    }

    public void setTailTrimCount(int tailTrimCount) {
        this.tailTrimCount = tailTrimCount;
    }

    /**
     * 替换内容的一条记录的对象
     */
    public static class ReplaceContent implements Serializable {

        private static final long serialVersionUID = 20100102L;
        private String key = "";//原来的文字
        private String value = "";//替换成的文字
        private String link = "";//链接的内容，如果为空，则表示没有链接

        public ReplaceContent() {
        }

        public ReplaceContent(String key, String value, String link) {
            this.key = key;
            this.value = value;
            this.link = link;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
