/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hadeslee.webpage.searcher;

import java.util.EventListener;

/**
 * �������¼�����Ȥ�Ķ������ʵ�ִ˽ӿ�
 * @author Hadeslee
 */
public interface KeyWordSearchListener extends EventListener {

    /**
     * ��ʼ����ĳ���ؼ����ˣ����ʱ��UI������listbar����
     * ���һ����¼��
     * @param keyword
     */
    public void keywordStartSearch(String keyword);

    /**
     * Ϊĳ���ؼ����ѵ���ĳƪ���£����ʱ��UI������JTabbedPane����
     * ���һ��Tab��
     * @param keyword ĳ���ؼ���
     * @param article ���ҵ�����������
     */
    public void articleSearched(String keyword, Article article);

    /**
     * ���е������Ѿ����
     * @param keyWordSearcher �ĸ����������������
     */
    public void searchDone(KeyWordSearcher keyWordSearcher);
}
