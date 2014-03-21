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
 * 通用的表列模型生成器
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
     * 设置表头的内容,参数可变
     * @param heads 表头的内容
     */
    public void setHeads(String... heads) {
        this.heads = heads;
    }

    /**
     * 设置表头的大小,参数可变
     * @param sizes 表头的大小
     */
    public void setSizes(int... sizes) {
        this.sizes = sizes;
    }

    /**
     * 初始化这张表,如果没有设置表头和大小就
     * 初始化则有可能会抛出异常
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
            throw new RuntimeException("列名和列大小设置不一致!!");
        }
    }
}
