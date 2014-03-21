/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hadeslee.webpage.ui.panel;

import com.hadeslee.webpage.searcher.MySearcher;
import com.hadeslee.webpage.util.Setting;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractListModel;

/**
 *
 * @author Hadeslee
 */
public class SearcherListModel extends AbstractListModel {

    private List<MySearcher> list = new ArrayList<MySearcher>();

    public SearcherListModel() {
        Setting setting = Setting.getInstance();
        for (MySearcher s : setting.getSearchers()) {
            list.add(s);
        }
    }

    public int getSize() {
        return list.size();
    }

    public Object getElementAt(int index) {
        return list.get(index);
    }
}
