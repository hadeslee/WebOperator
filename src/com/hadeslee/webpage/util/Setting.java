/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hadeslee.webpage.util;

import com.hadeslee.webpage.searcher.MySearcher;
import com.hadeslee.webpage.searcher.RealSearcher;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.FileFilter;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Vector;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

/**
 * һ��ȫ�ֵ����ö���
 *
 * @author Hadeslee
 */
public class Setting implements Serializable {

    private static final long serialVersionUID = 20090920L;
    private String readEncoding = "GBK";
    private String writeEncoding = "GBK";
    private String hn = "h1";//ʹ��H��
    private boolean textareaWrap = true;//�ı����Ƿ��Զ�����
    private int keywordPageCount = 10;//�ؼ���ץȡҳ���Ĵ�С
    private int keywordCount = 8;//ץȡ�ؼ��ֵ�����
    private int autoBuildRepeatCount = 2;//�Զ���վ��ѭ������
    private int zhanqunCount = 0;//վȺ����
    private boolean translateToEnglish;//�Ƿ����Ӣ��
    private String propertiesEncoding = "GBK";//�����ļ��ı���
    private boolean originalContent = false;//�Ƿ�ԭ��������
    private int sortType = DUO_SHAO;//�ؼ�������ʽ
    public static final int DUO_SHAO = 1;//���ֶൽ����
    public static final int SHAO_DUO = 2;//�����ٵ��ֶ�
    public static final int MO_REN = 3;//Ĭ��
    private boolean insertFriendLink;//�Ƿ������������
    private Vector<MySearcher> searchers;//��������˳��
    private int keywordPlace;//�ؼ��ֵ�λ�ã����滹������
    private boolean hideKeyWord;//�Ƿ����عؼ���
    private transient PropertyChangeSupport support = new PropertyChangeSupport(this);
    public static final String TEXTAREA_WRAP = "textareaWrap";
    public static final String HN_CHANGE = "hnChangedFuck";
    public static final int KEYWORD_TOP = 0;
    public static final int KEYWORD_BOTTOM = 1;
    private static Setting me = new Setting();

    public static Setting getInstance() {
        return me;
    }

    private RealSearcher getRealSearcher(File jsFile) {
        try {
            String content = Util.readFile(jsFile, getReadEncoding());
            if (Util.isEmpty(content) == false) {
                ScriptEngineManager sem = new ScriptEngineManager();
                ScriptEngine se = sem.getEngineByExtension("js");
                if (se instanceof Invocable) {
                    Invocable invoke = (Invocable) se;
                    se.eval(content);
                    RealSearcher ie = invoke.getInterface(RealSearcher.class);
                    return ie;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;

    }

    private Setting() {
        searchers = new Vector<MySearcher>();
        //TODO �������ö�ȡ�ļ����õķ�ʽ
        File dir = new File("js");
        File[] fs = dir.listFiles(new FileFilter() {

            public boolean accept(File pathname) {
                if (pathname.isFile() && pathname.getName().endsWith(".js")) {
                    return true;
                }
                return false;
            }
        });
        for (File file : fs) {
            RealSearcher searcher = getRealSearcher(file);
            if (searcher != null) {
                searchers.add(new MySearcher(searcher));
            }
        }
    }

    private void readObject(ObjectInputStream ois) {
        try {
            ois.defaultReadObject();
            support = new PropertyChangeSupport(this);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public boolean isHideKeyWord() {
        return hideKeyWord;
    }

    public void setHideKeyWord(boolean hideKeyWord) {
        this.hideKeyWord = hideKeyWord;
    }

    public int getKeywordPlace() {
        return keywordPlace;
    }

    public void setKeywordPlace(int keywordPlace) {
        this.keywordPlace = keywordPlace;
    }

    public boolean isTranslateToEnglish() {
        return translateToEnglish;
    }

    public void setTranslateToEnglish(boolean translateToEnglish) {
        this.translateToEnglish = translateToEnglish;
    }

    public int getZhanqunCount() {
        return zhanqunCount;
    }

    public void setZhanqunCount(int zhanqunCount) {
        this.zhanqunCount = zhanqunCount;
    }

    public Vector<MySearcher> getSearchers() {
        return searchers;
    }

    public void setSearchers(Vector<MySearcher> searchers) {
        this.searchers = searchers;
    }

    public boolean isInsertFriendLink() {
        return insertFriendLink;
    }

    public void setInsertFriendLink(boolean insertFriendLink) {
        this.insertFriendLink = insertFriendLink;
    }

    public boolean isOriginalContent() {
        return originalContent;
    }

    public void setOriginalContent(boolean originalContent) {
        this.originalContent = originalContent;
    }

    public int getAutoBuildRepeatCount() {
        return autoBuildRepeatCount;
    }

    public void setAutoBuildRepeatCount(int autoBuildRepeatCount) {
        this.autoBuildRepeatCount = autoBuildRepeatCount;
    }

    public String getPropertiesEncoding() {
        return propertiesEncoding;
    }

    public void setPropertiesEncoding(String propertiesEncoding) {
        this.propertiesEncoding = propertiesEncoding;
    }

    public int getKeywordCount() {
        return keywordCount;
    }

    public void setKeywordCount(int keywordCount) {
        this.keywordCount = keywordCount;
    }

    public int getKeywordPageCount() {
        return keywordPageCount;
    }

    public void setKeywordPageCount(int keywordPageCount) {
        this.keywordPageCount = keywordPageCount;
    }

    public synchronized void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        support.removePropertyChangeListener(propertyName, listener);
    }

    public synchronized void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }

    public synchronized void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        support.addPropertyChangeListener(propertyName, listener);
    }

    public synchronized void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public String getReadEncoding() {
        return readEncoding;
    }

    public void setReadEncoding(String readEncoding) {
        this.readEncoding = readEncoding;
    }

    public String getHn() {
        return hn;
    }

    public void setHn(String hn) {
        support.firePropertyChange(Setting.HN_CHANGE, this.hn, hn);
        this.hn = hn;
    }

    public boolean isTextareaWrap() {
        return textareaWrap;
    }

    public void setTextareaWrap(boolean textareaWrap) {
        support.firePropertyChange(Setting.TEXTAREA_WRAP, this.textareaWrap, textareaWrap);
        this.textareaWrap = textareaWrap;
    }

    public Comparator<String> getSorter() {
        if (sortType == DUO_SHAO) {
            return new Comparator<String>() {

                public int compare(String o1, String o2) {
                    return o2.length() - o1.length();
                }
            };
        } else if (sortType == SHAO_DUO) {
            return new Comparator<String>() {

                public int compare(String o1, String o2) {
                    return o1.length() - o2.length();
                }
            };
        } else {
            return null;
        }
    }

    public int getSortType() {
        return sortType;
    }

    public void setSortType(int sortType) {
        this.sortType = sortType;
    }

    public String getWriteEncoding() {
        return writeEncoding;
    }

    public void setWriteEncoding(String writeEncoding) {
        this.writeEncoding = writeEncoding;
    }
}
