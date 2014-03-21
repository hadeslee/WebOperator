/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hadeslee.webpage.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * �����滻�Ķ���
 * @author Hadeslee
 */
public class ReplaceInfo implements Serializable {

    private static final long serialVersionUID = 20100102L;
    private int headTrimCount;//ͷ��ȥ��������
    private int tailTrimCount;//β��ȥ��������
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
     * �滻���ݵ�һ����¼�Ķ���
     */
    public static class ReplaceContent implements Serializable {

        private static final long serialVersionUID = 20100102L;
        private String key = "";//ԭ��������
        private String value = "";//�滻�ɵ�����
        private String link = "";//���ӵ����ݣ����Ϊ�գ����ʾû������

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
