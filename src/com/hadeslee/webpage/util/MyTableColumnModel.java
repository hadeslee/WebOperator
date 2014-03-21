/*
 * MyTableColumnModel.java
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.hadeslee.webpage.util;

import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

/**
 * ͨ�õı���ģ��������
 * @author hadeslee
 */
public class MyTableColumnModel extends DefaultTableColumnModel {

    private TableCellRenderer renderer;
    private String[] heads;
    private int[] sizes;
    private TableColumn[] tcs;

    /** Creates a new instance of MyTableColumnModel */
    public MyTableColumnModel(TableCellRenderer renderer) {
        this.renderer = renderer;
        sizes = new int[0];
        heads = new String[0];
    }

    /**
     * ���ñ�ͷ������,�����ɱ�
     * @param heads ��ͷ������
     */
    public void setHeads(String... heads) {
        this.heads = heads;
    }

    /**
     * ���ñ�ͷ�Ĵ�С,�����ɱ�
     * @param sizes ��ͷ�Ĵ�С
     */
    public void setSizes(int... sizes) {
        this.sizes = sizes;
    }

    /**
     * ��ʼ�����ű�,���û�����ñ�ͷ�ʹ�С��
     * ��ʼ�����п��ܻ��׳��쳣
     */
    public void init() {
        if (heads.length == sizes.length) {
            tcs = new TableColumn[heads.length];
            for (int i = 0; i < heads.length; i++) {
                tcs[i] = new TableColumn(i, sizes[i], renderer, null);
                tcs[i].setHeaderValue(heads[i]);
                tcs[i].setMinWidth(sizes[i]);
                this.addColumn(tcs[i]);
            }
        } else {
            throw new RuntimeException("�������д�С���ò�һ��!!");
        }
    }
}
