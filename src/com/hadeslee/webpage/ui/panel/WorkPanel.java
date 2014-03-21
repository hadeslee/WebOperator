/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * WorkPanel.java
 *
 * Created on 2009-9-19, 16:08:23
 */
package com.hadeslee.webpage.ui.panel;

import com.hadeslee.webpage.data.WorkSpace;
import com.hadeslee.webpage.searcher.Article;
import com.hadeslee.webpage.searcher.KeyWordSearchListener;
import com.hadeslee.webpage.searcher.KeyWordSearcher;
import com.hadeslee.webpage.searcher.MySearcher;
import com.hadeslee.webpage.template.KeywordContentTemplate;
import com.hadeslee.webpage.template.KeywordListTemplate;
import com.hadeslee.webpage.template.holder.KeywordContentValueHolder;
import com.hadeslee.webpage.template.holder.KeywordListValueHolder;
import com.hadeslee.webpage.ui.Main;
import com.hadeslee.webpage.util.MyBundle;
import com.hadeslee.webpage.util.MyTableColumnModel;
import com.hadeslee.webpage.util.ReplaceInfo;
import com.hadeslee.webpage.util.ReplaceInfo.ReplaceContent;
import com.hadeslee.webpage.util.Setting;
import com.hadeslee.webpage.util.Util;
import java.awt.Color;
import java.awt.Component;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author Hadeslee
 */
public class WorkPanel extends javax.swing.JPanel implements PropertyChangeListener, KeyWordSearchListener {

    private WorkSpace workSpace = new WorkSpace();//本面板对应的工作空间
    private JFileChooser jfc;//文件选择器
    private Main main;//父组件
    private JRadioButtonMenuItem menuItem;//与此面板相关联的菜单，当此面板除去的时候，此菜单也要除去了
    private List<KeyWordSearcher> keywordSearchers = new ArrayList<KeyWordSearcher>();
    private int currentIndex;//当前正在搜索的下标
    private Thread changeTextThread;
    //＝＝＝＝＝＝＝＝＝＝＝list模版
    private List<String> replaceLinks;
    //===============Content模版
    private String articleTitle;
    private String articleHead;
    private String articleTail;
    private TableRowSorter<TableModel> ts;
    private ReplaceInfo info = Util.readReplaceInfo();

    /** Creates new form WorkPanel */
    public WorkPanel() {
        initComponents();
        initOther();
    }

    public WorkPanel(Main main, String name, JRadioButtonMenuItem menuItem) {
        this.main = main;
        this.menuItem = menuItem;
        initComponents();
        jLabel8.setText(name);
        initOther();
    }

    private JTable initTable() {
        MyTableColumnModel mcm = new MyTableColumnModel(new DefaultTableCellRenderer());
        mcm.setHeads(MyBundle.getString("content.to.be.replaced"), MyBundle.getString("replace.content"), MyBundle.getString("add.link.content"));
        mcm.setSizes(130, 130, 220);
        mcm.init();
        MyTableModel mt = new MyTableModel();
        JTable jt = new JTable(mt, mcm);
        ts = new TableRowSorter<TableModel>(mt);
        ts.setSortsOnUpdates(true);
        jt.setRowSorter(ts);
        jt.setRowHeight(18);
        jt.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jt.getTableHeader().setReorderingAllowed(false);
        jt.setGridColor(Color.BLACK);
        jt.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);
        jt.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting() == false) {
                    int index = jTable1.getSelectedRow();
                    if (index > -1) {
                        index = ts.convertRowIndexToModel(index);
                        ReplaceContent rc = info.getReplaceContents().get(index);
                        jTextField7.setText(rc.getKey());
                        jTextField8.setText(rc.getValue());
                        jTextField24.setText(rc.getLink());
                    }
                }
            }
        });
        return jt;
    }

    /**
     * 自定义的表的数据模型
     */
    private class MyTableModel extends AbstractTableModel {

        public int getRowCount() {
            return info.getReplaceContents().size();
        }

        public int getColumnCount() {
            return 3;
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            return getValue(info.getReplaceContents().get(rowIndex), columnIndex);
        }

        public boolean isCellEditable(int row, int col) {
            return false;
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return String.class;
        }
    }

    /**
     * 自定义的表渲染器
     */
    private class MyTableCellRenderer extends JLabel implements TableCellRenderer {

        public MyTableCellRenderer() {
            this.setOpaque(true);
            this.setHorizontalAlignment(JLabel.CENTER);
        }

        public void repaint() {
        }

        public void repaint(Rectangle rec) {
        }

        public void repaint(long l, int x, int y, int width, int height) {
        }

        public void validate() {
        }

        public void invalidate() {
        }

        public void revalidate() {
        }

        public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText(String.valueOf(value));
            if (isSelected) {
//                this.setBackground(new Color(255, 255, 120));
                this.setForeground(UIManager.getColor("Table.selectionForeground"));
                this.setForeground(UIManager.getColor("Table.selectionBackground"));
            } else {
                this.setForeground(UIManager.getColor("Table.foreground"));
                this.setBackground(UIManager.getColor("Table.background"));
            }
            return this;
        }
    }

    private Object getValue(ReplaceContent rc, int column) {
        switch (column) {
            case 0:
                return rc.getKey();
            case 1:
                return rc.getValue();
            case 2:
                return rc.getLink();
            default:
                return "";
        }
    }

    public void setEnabled(boolean b) {
        super.setEnabled(b);
        jTabbedPane1.setEnabled(b);
    }

    public void loadFromWorkSpace(WorkSpace workSpace) {
        this.workSpace = workSpace;
        initOther();
        //TODO 还有一些界面的初始化工作
        jButton1.setEnabled(false);
        jTextField1.setText(workSpace.getOpenFile().getPath());
        jRadioButton1.setEnabled(true);
        jRadioButton2.setEnabled(true);
        if (!workSpace.isSearchKeyWord()) {
            jRadioButton1.setSelected(true);
            jLabel2.setText(MyBundle.getString("keyword.to.page"));
            jButton2.setText(MyBundle.getString("ok"));
        }
        jTextField2.setText(workSpace.getKeywordInput());
        jTextArea1.setText(workSpace.getjTextArea1());
        webContentJTA.setText(workSpace.getWebContentJTA());
        jButton8.setEnabled(true);
        if (workSpace.getWebPage().getKeywords().size() > 0) {
            searchKeywordDone();
        }
        if (workSpace.getKeywordListTemplate() != null) {
            File file = workSpace.getKeywordListTemplate();
            if (file.exists()) {
                jTextField3.setText(file.getPath());
                String pageContent = Util.readFile(file, workSpace.getWebPage().getSetting().getReadEncoding());
                workSpace.getWebPage().setKeywordListTemplate(new KeywordListTemplate(pageContent));
            } else {
                workSpace.setKeywordListTemplate(null);
            }
        }
        if (workSpace.getKeywordContentTemplate() != null) {
            File file = workSpace.getKeywordContentTemplate();
            if (file.exists()) {
                jTextField4.setText(file.getPath());
                String pageContent = Util.readFile(file, workSpace.getWebPage().getSetting().getReadEncoding());
                workSpace.getWebPage().setKeywordContentTemplate(new KeywordContentTemplate(pageContent));
            } else {
                workSpace.setKeywordContentTemplate(null);
            }

        }
    }

    /**
     * 初始化其它的一些组件
     */
    private void initOther() {
        replaceLinks = new ArrayList<String>();
        for (int i = 0; i < 10; i++) {
            replaceLinks.add("");
        }
        jTextField5.setTransferHandler(null);
        jTextField5.setTransferHandler(null);
        jTextField6.setText(String.valueOf(info.getHeadTrimCount()));
        jTextField5.setText(String.valueOf(info.getTailTrimCount()));
        myLabel.setVisible(false);
        myProgressBar.setVisible(false);
        jProgressBar2.setVisible(false);
        jLabel15.setVisible(false);
        jProgressBar1.setVisible(false);
        jLabel29.setVisible(false);
        jProgressBar3.setVisible(false);
        jTextArea3.setText(null);
        jButton18.setEnabled(false);
        jButton25.setEnabled(false);
        settingPanel1.setSetting(workSpace.getWebPage().getSetting());
        workSpace.getWebPage().getSetting().addPropertyChangeListener(this);
        friendJTA.setText(Util.getCurrentFriendLinkContent());
        friendJTA.getDocument().addDocumentListener(new DocumentListener() {

            public void insertUpdate(DocumentEvent e) {
                Util.setCurrentFriendLinkContent(friendJTA.getText());
            }

            public void removeUpdate(DocumentEvent e) {
                Util.setCurrentFriendLinkContent(friendJTA.getText());
            }

            public void changedUpdate(DocumentEvent e) {
                Util.setCurrentFriendLinkContent(friendJTA.getText());
            }
        });
        jTabbedPane1.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                int index = jTabbedPane1.getSelectedIndex();
                //只有预览的标签启用了，并且选中了它，才重新生成一遍
                if (jTabbedPane2.isEnabled() && index == 1) {
                    generatePreviewTabContents();
                }
            }
        });
        Util.addClipboardListener(this);

        for (int i = 0; i < 10; i++) {
            jTabbedPane1.setTitleAt(i, MyBundle.getString("main.tab." + (i + 1)));
        }
        for (int i = 0; i < 8; i++) {
            jTabbedPane2.setTitleAt(i, MyBundle.getString("tab2." + (i + 1)));
        }
    }

    public JRadioButtonMenuItem getMenuItem() {
        return menuItem;
    }

    /**
     * 获得文件选择器
     * @param desc 说明
     * @param exts 扩展名
     * @return 文件选择器
     */
    private JFileChooser getJFileChooser(String desc, String... exts) {
        if (jfc == null) {
            jfc = new JFileChooser(".");
        }
        jfc.resetChoosableFileFilters();
        if (exts != null && exts.length > 0) {
            jfc.setFileFilter(new FileNameExtensionFilter(desc, exts));
        }
        return jfc;
    }

    /**
     * 得到选中的文件，如果没有选中文件，则返回null
     * @param desc 说明
     * @param exts 扩展名
     * @return 选中的文件
     */
    public File getOpenSelectedFile(String desc, String... exts) {
        JFileChooser temp = getJFileChooser(desc, exts);
        int i = temp.showOpenDialog(this);
        if (i == JFileChooser.APPROVE_OPTION) {
            return temp.getSelectedFile();
        }
        return null;
    }

    private File getSaveSelectedFile(String desc, String... exts) {
        JFileChooser temp = getJFileChooser(desc, exts);
        int i = temp.showSaveDialog(this);
        if (i == JFileChooser.APPROVE_OPTION) {
            return temp.getSelectedFile();
        }
        return null;
    }

    /**
     * 处理搜索关键字
     */
    private void doSearchKeyWord() {
        final String content = jTextField2.getText();
        if (Util.isEmpty(content)) {
            JOptionPane.showMessageDialog(this, MyBundle.getString("keyword.to.search.can.not.empty"));
        } else {
            new Thread() {

                public void run() {
                    showProgress(MyBundle.getString("searching.keyword"));
                    doSearchKeyWord0(content);
                    hideProgress();
                }
            }.start();
        }
    }

    private void showProgress(String text) {
        myLabel.setText(text);
        myLabel.setVisible(true);
        myProgressBar.setVisible(true);
    }

    private void hideProgress() {
        myLabel.setVisible(false);
        myProgressBar.setVisible(false);
    }

    /**
     * 重新生成TAB那里预览的内容
     */
    private void generatePreviewTabContents() {
        titleJTA.setText(workSpace.getWebPage().getTitle());
        descriptionJTA.setText(workSpace.getWebPage().getMetaDescription());
        keywordJTA.setText(workSpace.getWebPage().getMetaKeywords());
        navJTA.setText(workSpace.getWebPage().getNavigation());
        centerHJTA.setText(workSpace.getWebPage().getCenterH(workSpace.getWebPage().getSetting().getHn()));
        allJTA.setText(workSpace.getWebPage().getNewContent(workSpace.getWebPage().getSetting()));
        allCodeJTA.setText(workSpace.getWebPage().getAllCodeContent(workSpace.getWebPage().getSetting().getHn()));
    }

    private void searchKeywordDone() {
        jButton3.setEnabled(true);
        jButton4.setEnabled(true);
        jTabbedPane2.setEnabled(true);
        generatePreviewTabContents();
    }

    /**
     * 处理搜索关键字的实际执行方法
     */
    private void doSearchKeyWord0(String content) {
        List<String> list = Util.searchKeyWords(content);
        Setting setting = workSpace.getWebPage().getSetting();
        if (setting.getSortType() != Setting.MO_REN) {
            Collections.sort(list, setting.getSorter());
        }
        int count = 0;
        workSpace.getWebPage().getKeywords().clear();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            String temp = list.get(i);
            workSpace.getWebPage().getKeywords().add(temp);
            sb.append(temp).append("|");
            count++;
            if (count >= workSpace.getWebPage().getSetting().getKeywordCount()) {
                break;
            }
        }
        if (sb.length() > 0) {
            searchKeywordDone();
            sb.deleteCharAt(sb.length() - 1);
        }
        jTextArea1.setText(sb.toString());
    }

    /**
     * 处理输入关键字
     */
    private void doInputKeyWord() {
        String text = jTextField2.getText();
        if (Util.isEmpty(text)) {
            JOptionPane.showMessageDialog(this, MyBundle.getString("keyword.input.can.not.empty"));
        } else {
            String[] ss = text.split("\\||,|，|｜");
            StringBuilder sb = new StringBuilder();
            workSpace.getWebPage().getKeywords().clear();
            for (String s : ss) {
                workSpace.getWebPage().getKeywords().add(s);
                sb.append(s).append("|");
            }
            if (sb.length() > 0) {
                searchKeywordDone();
                sb.deleteCharAt(sb.length() - 1);
            }
            jTextArea1.setText(sb.toString());
        }
    }

    private void doSearchStart() {
        jButton3.setEnabled(false);
        jButton4.setEnabled(false);
        jTabbedPane2.setEnabled(false);
        if (jRadioButton2.isSelected()) {
            doSearchKeyWord();
        } else {
            doInputKeyWord();
        }
    }

    private void saveToFile(File file, String content, String... exts) {
        if (exts.length != 0) {
            boolean find = false;
            for (String ext : exts) {
                if (file.toString().toLowerCase().endsWith(ext.toLowerCase())) {
                    find = true;
                }
            }
            if (find == false) {
                file = new File(file.getPath() + exts[0]);
            }
        }
        boolean b = Util.saveFile(content, workSpace.getWebPage().getSetting().getWriteEncoding(), file);
        String text = MyBundle.getString("save.succ");
        if (!b) {
            text = MyBundle.getString("save.failure");
        }
        JOptionPane.showMessageDialog(this, text);
    }

    private void doSave() {
        jButton3.setEnabled(false);
        try {
            saveToFile(workSpace.getOpenFile(),
                    Util.insertContent(webContentJTA.getText(), workSpace.getWebPage().getKeywords(), workSpace.getWebPage().getSetting()));
            workSpace.getWebPage().setMainFile(workSpace.getOpenFile());
        } finally {
            jButton3.setEnabled(true);
        }
    }

    private void doSaveAs() {
        File file = getSaveSelectedFile(MyBundle.getString("webpage.file"));
        if (file != null) {
            saveToFile(file,
                    Util.insertContent(webContentJTA.getText(), workSpace.getWebPage().getKeywords(), workSpace.getWebPage().getSetting()));
            workSpace.getWebPage().setMainFile(file);
        }
    }

    private void fillWorkSpace() {
        workSpace.setKeywordInput(jTextField2.getText());
        workSpace.setSearchKeyWord(jRadioButton2.isSelected());
        workSpace.setWebContentJTA(webContentJTA.getText());
        workSpace.setjTextArea1(jTextArea1.getText());
    }

    private void doSaveWorkSpace() {
        fillWorkSpace();
        File file = workSpace.getFromFile();
        //说明是新建的一个工作区
        if (file == null) {
            file = getSaveSelectedFile(MyBundle.getString("workspace.file"), "wks");
            if (!file.toString().endsWith(".wks")) {
                file = new File(file.getPath() + ".wks");
            }
        }
        if (file != null) {
            boolean b = Util.saveWorkSpace(workSpace, file);
            String text = MyBundle.getString("save.workspace.succ");
            if (!b) {
                text = MyBundle.getString("save.workspace.failure");
            }
            JOptionPane.showMessageDialog(this, text);
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jLabel2 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jRadioButton1 = new javax.swing.JRadioButton();
        jButton2 = new javax.swing.JButton();
        myProgressBar = new javax.swing.JProgressBar();
        myLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        webContentJTA = new javax.swing.JTextArea();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane9 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        titleJTA = new javax.swing.JTextArea();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        keywordJTA = new javax.swing.JTextArea();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        descriptionJTA = new javax.swing.JTextArea();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        navJTA = new javax.swing.JTextArea();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        centerHJTA = new javax.swing.JTextArea();
        jPanel19 = new javax.swing.JPanel();
        jScrollPane15 = new javax.swing.JScrollPane();
        friendJTA = new javax.swing.JTextArea();
        jPanel13 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        allJTA = new javax.swing.JTextArea();
        jPanel17 = new javax.swing.JPanel();
        jScrollPane10 = new javax.swing.JScrollPane();
        allCodeJTA = new javax.swing.JTextArea();
        jPanel16 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jTextField13 = new javax.swing.JTextField();
        jButton23 = new javax.swing.JButton();
        jComboBox2 = new javax.swing.JComboBox();
        jLabel18 = new javax.swing.JLabel();
        jScrollPane12 = new javax.swing.JScrollPane();
        jTextArea4 = new javax.swing.JTextArea();
        jLabel19 = new javax.swing.JLabel();
        jTextField14 = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        jTextField15 = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        jTextField16 = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        jTextField17 = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        jTextField18 = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jTextField19 = new javax.swing.JTextField();
        jTextField20 = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jTextField21 = new javax.swing.JTextField();
        jTextField22 = new javax.swing.JTextField();
        jTextField23 = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jProgressBar3 = new javax.swing.JProgressBar();
        jButton24 = new javax.swing.JButton();
        jButton25 = new javax.swing.JButton();
        jButton26 = new javax.swing.JButton();
        jButton27 = new javax.swing.JButton();
        jButton28 = new javax.swing.JButton();
        jPanel15 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jTextField9 = new javax.swing.JTextField();
        jButton17 = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        jTextField10 = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jTextField11 = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jTextField12 = new javax.swing.JTextField();
        jButton18 = new javax.swing.JButton();
        jScrollPane11 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        jButton19 = new javax.swing.JButton();
        jButton20 = new javax.swing.JButton();
        jButton21 = new javax.swing.JButton();
        jProgressBar1 = new javax.swing.JProgressBar();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jButton22 = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jProgressBar2 = new javax.swing.JProgressBar();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jPanel11 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jButton7 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jTextField4 = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane8 = new javax.swing.JScrollPane();
        jTextArea3 = new javax.swing.JTextArea();
        jButton11 = new javax.swing.JButton();
        jPanel12 = new javax.swing.JPanel();
        settingPanel1 = new com.hadeslee.webpage.ui.panel.SettingPanel();
        jPanel14 = new javax.swing.JPanel();
        autoBuildSitePanel1 = new com.hadeslee.webpage.ui.panel.AutoBuildSitePanel();
        jPanel9 = new javax.swing.JPanel();
        jButton12 = new javax.swing.JButton();
        jTextField5 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jButton14 = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();
        jButton29 = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jTextField7 = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jTextField8 = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        jTextField24 = new javax.swing.JTextField();
        jScrollPane13 = new javax.swing.JScrollPane();
        jTable1 = initTable();
        jPanel10 = new javax.swing.JPanel();
        jScrollPane14 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jLabel31 = new javax.swing.JLabel();
        jButton13 = new javax.swing.JButton();
        jButton30 = new javax.swing.JButton();
        jPanel18 = new javax.swing.JPanel();
        recurringContentPanel1 = new com.hadeslee.webpage.ui.panel.RecurringContentPanel(workSpace,this);

        jLabel1.setText(MyBundle.getString("page.url")); // NOI18N

        jTextField1.setEditable(false);

        jButton1.setText(MyBundle.getString("browse")); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        buttonGroup1.add(jRadioButton2);
        jRadioButton2.setText(MyBundle.getString("auto.search.keyword")); // NOI18N
        jRadioButton2.setEnabled(false);
        jRadioButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton2ActionPerformed(evt);
            }
        });

        jLabel2.setText(MyBundle.getString("search.keyword")); // NOI18N

        jTextField2.setEnabled(false);
        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });

        buttonGroup1.add(jRadioButton1);
        jRadioButton1.setSelected(true);
        jRadioButton1.setText(MyBundle.getString("mani.do.keyword")); // NOI18N
        jRadioButton1.setEnabled(false);
        jRadioButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton1ActionPerformed(evt);
            }
        });

        jButton2.setText(MyBundle.getString("ok")); // NOI18N
        jButton2.setEnabled(false);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        myProgressBar.setIndeterminate(true);

        myLabel.setText("正在******");

        webContentJTA.setColumns(20);
        webContentJTA.setRows(5);
        Util.addFindForTextArea(main,webContentJTA);
        jScrollPane1.setViewportView(webContentJTA);

        jButton3.setText(MyBundle.getString("save.main.page.file")); // NOI18N
        jButton3.setEnabled(false);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText(MyBundle.getString("page.save.as")); // NOI18N
        jButton4.setEnabled(false);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabel3.setText(MyBundle.getString("used.keyword")); // NOI18N

        jTextArea1.setColumns(20);
        jTextArea1.setEditable(false);
        jTextArea1.setForeground(new java.awt.Color(255, 0, 0));
        jTextArea1.setLineWrap(true);
        jTextArea1.setRows(1);
        jScrollPane9.setViewportView(jTextArea1);

        jButton8.setText(MyBundle.getString("save.workspace")); // NOI18N
        jButton8.setEnabled(false);
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jButton9.setText(MyBundle.getString("close.workspace")); // NOI18N
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("宋体", 0, 14));
        jLabel8.setForeground(new java.awt.Color(255, 0, 0));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("jLabel8");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 637, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jRadioButton2)
                        .addGap(18, 18, 18)
                        .addComponent(jRadioButton1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(jTextField2, javax.swing.GroupLayout.DEFAULT_SIZE, 643, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2)))
                .addGap(9, 9, 9))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(myLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(myProgressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 151, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3)
                .addGap(18, 18, 18)
                .addComponent(jButton4)
                .addGap(26, 26, 26))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 789, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 705, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButton2)
                    .addComponent(jRadioButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jButton2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jLabel8)
                            .addComponent(jButton9)))
                    .addComponent(myLabel)
                    .addComponent(myProgressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jButton8))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jButton4))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jButton3)))
                .addContainerGap())
        );

        jTabbedPane1.addTab("网站程序优化", jPanel1);

        jTabbedPane2.setTabPlacement(javax.swing.JTabbedPane.LEFT);
        jTabbedPane2.setEnabled(false);

        titleJTA.setColumns(20);
        titleJTA.setLineWrap(true);
        titleJTA.setRows(5);
        jScrollPane3.setViewportView(titleJTA);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 696, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 439, Short.MAX_VALUE)
        );

        jTabbedPane2.addTab("标题预览", jPanel3);

        keywordJTA.setColumns(20);
        keywordJTA.setLineWrap(true);
        keywordJTA.setRows(5);
        jScrollPane5.setViewportView(keywordJTA);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 696, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 439, Short.MAX_VALUE)
        );

        jTabbedPane2.addTab("关键字词预览", jPanel5);

        descriptionJTA.setColumns(20);
        descriptionJTA.setLineWrap(true);
        descriptionJTA.setRows(5);
        jScrollPane4.setViewportView(descriptionJTA);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 696, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 439, Short.MAX_VALUE)
        );

        jTabbedPane2.addTab("页面描述预览", jPanel4);

        navJTA.setColumns(20);
        navJTA.setLineWrap(true);
        navJTA.setRows(5);
        jScrollPane6.setViewportView(navJTA);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 696, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 439, Short.MAX_VALUE)
        );

        jTabbedPane2.addTab("导航页预览", jPanel6);

        centerHJTA.setColumns(20);
        centerHJTA.setLineWrap(true);
        centerHJTA.setRows(5);
        jScrollPane7.setViewportView(centerHJTA);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 696, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 439, Short.MAX_VALUE)
        );

        jTabbedPane2.addTab("居中代码预览", jPanel7);

        friendJTA.setColumns(20);
        friendJTA.setRows(5);
        jScrollPane15.setViewportView(friendJTA);

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane15, javax.swing.GroupLayout.DEFAULT_SIZE, 696, Short.MAX_VALUE)
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane15, javax.swing.GroupLayout.DEFAULT_SIZE, 439, Short.MAX_VALUE)
        );

        jTabbedPane2.addTab("友情链接预览", jPanel19);

        allJTA.setColumns(20);
        allJTA.setEditable(false);
        allJTA.setLineWrap(true);
        allJTA.setRows(5);
        jScrollPane2.setViewportView(allJTA);

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 696, Short.MAX_VALUE)
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 439, Short.MAX_VALUE)
        );

        jTabbedPane2.addTab("整合代码预览", jPanel13);

        allCodeJTA.setColumns(20);
        allCodeJTA.setEditable(false);
        allCodeJTA.setLineWrap(true);
        allCodeJTA.setRows(5);
        jScrollPane10.setViewportView(allCodeJTA);

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane10, javax.swing.GroupLayout.DEFAULT_SIZE, 696, Short.MAX_VALUE)
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane10, javax.swing.GroupLayout.DEFAULT_SIZE, 439, Short.MAX_VALUE)
        );

        jTabbedPane2.addTab("生成代码预览", jPanel17);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 789, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 444, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("标签优化设置", jPanel2);

        jLabel17.setText(MyBundle.getString("webpage.url")); // NOI18N

        jTextField13.setText("http://");
        jTextField13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField13ActionPerformed(evt);
            }
        });

        jButton23.setText(MyBundle.getString("fetch.content")); // NOI18N
        jButton23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton23ActionPerformed(evt);
            }
        });

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "GBK", "UTF-8", "BIG5", "ISO-8859-1" }));

        jLabel18.setText(MyBundle.getString("webpage.encoding")); // NOI18N

        jTextArea4.setColumns(20);
        jTextArea4.setLineWrap(true);
        jTextArea4.setRows(5);
        Util.addFindForTextArea(main,jTextArea4);
        jScrollPane12.setViewportView(jTextArea4);

        jLabel19.setText(MyBundle.getString("article.subject.1")); // NOI18N

        jTextField14.setColumns(30);
        jTextField14.setEditable(false);
        jTextField14.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTextField14MousePressed(evt);
            }
        });

        jLabel20.setText(MyBundle.getString("article.subject.3")); // NOI18N

        jTextField15.setColumns(30);
        jTextField15.setEditable(false);
        jTextField15.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTextField15MousePressed(evt);
            }
        });

        jLabel21.setText(MyBundle.getString("article.subject.5")); // NOI18N

        jTextField16.setColumns(30);
        jTextField16.setEditable(false);
        jTextField16.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTextField16MousePressed(evt);
            }
        });

        jLabel22.setText(MyBundle.getString("article.subject.7")); // NOI18N

        jTextField17.setColumns(30);
        jTextField17.setEditable(false);
        jTextField17.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTextField17MousePressed(evt);
            }
        });

        jLabel23.setText(MyBundle.getString("article.subject.9")); // NOI18N

        jTextField18.setColumns(30);
        jTextField18.setEditable(false);
        jTextField18.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTextField18MousePressed(evt);
            }
        });

        jLabel24.setText(MyBundle.getString("article.subject.4")); // NOI18N

        jLabel25.setText(MyBundle.getString("article.subject.10")); // NOI18N

        jTextField19.setColumns(30);
        jTextField19.setEditable(false);
        jTextField19.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTextField19MousePressed(evt);
            }
        });

        jTextField20.setColumns(30);
        jTextField20.setEditable(false);
        jTextField20.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTextField20MousePressed(evt);
            }
        });

        jLabel26.setText(MyBundle.getString("article.subject.6")); // NOI18N

        jLabel27.setText(MyBundle.getString("article.subject.2")); // NOI18N

        jTextField21.setColumns(30);
        jTextField21.setEditable(false);
        jTextField21.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTextField21MousePressed(evt);
            }
        });

        jTextField22.setColumns(30);
        jTextField22.setEditable(false);
        jTextField22.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTextField22MousePressed(evt);
            }
        });

        jTextField23.setColumns(30);
        jTextField23.setEditable(false);
        jTextField23.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTextField23MousePressed(evt);
            }
        });

        jLabel28.setText(MyBundle.getString("article.subject.8")); // NOI18N

        jLabel29.setText(MyBundle.getString("fetching.content.of.webpage")); // NOI18N

        jProgressBar3.setIndeterminate(true);

        jButton24.setText(MyBundle.getString("replace.lots")); // NOI18N
        jButton24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton24ActionPerformed(evt);
            }
        });

        jButton25.setText(MyBundle.getString("generate.template")); // NOI18N
        jButton25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton25ActionPerformed(evt);
            }
        });

        jButton26.setText(MyBundle.getString("clear")); // NOI18N
        jButton26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton26ActionPerformed(evt);
            }
        });

        jButton27.setText(MyBundle.getString("save")); // NOI18N
        jButton27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton27ActionPerformed(evt);
            }
        });

        jButton28.setText(MyBundle.getString("save.as")); // NOI18N
        jButton28.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton28ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel16Layout.createSequentialGroup()
                                .addComponent(jLabel19)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField14))
                            .addGroup(jPanel16Layout.createSequentialGroup()
                                .addComponent(jLabel20)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField15))
                            .addGroup(jPanel16Layout.createSequentialGroup()
                                .addComponent(jLabel21)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField16))
                            .addGroup(jPanel16Layout.createSequentialGroup()
                                .addComponent(jLabel22)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField17))
                            .addGroup(jPanel16Layout.createSequentialGroup()
                                .addComponent(jLabel23)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField18)))
                        .addGap(33, 33, 33)
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel16Layout.createSequentialGroup()
                                .addComponent(jLabel25)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField20, 0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel16Layout.createSequentialGroup()
                                .addComponent(jLabel28)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField23, 0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel16Layout.createSequentialGroup()
                                .addComponent(jLabel26)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField22, 0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel16Layout.createSequentialGroup()
                                .addComponent(jLabel24)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField19, 0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel16Layout.createSequentialGroup()
                                .addComponent(jLabel27)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField21, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(267, Short.MAX_VALUE))
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addComponent(jScrollPane12, javax.swing.GroupLayout.DEFAULT_SIZE, 789, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addComponent(jLabel17)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField13, javax.swing.GroupLayout.DEFAULT_SIZE, 483, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton23)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel18)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(25, 25, 25))
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addComponent(jLabel29)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jProgressBar3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 160, Short.MAX_VALUE)
                        .addComponent(jButton24)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton25)
                        .addGap(18, 18, 18)
                        .addComponent(jButton26)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton27)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton28)
                        .addContainerGap())))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(jTextField13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton23))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane12, javax.swing.GroupLayout.DEFAULT_SIZE, 217, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel19)
                            .addComponent(jTextField14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel20)
                            .addComponent(jTextField15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel21)
                            .addComponent(jTextField16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel22)
                            .addComponent(jTextField17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel23)
                            .addComponent(jTextField18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel27)
                            .addComponent(jTextField21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel24)
                            .addComponent(jTextField19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel26)
                            .addComponent(jTextField22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel28)
                            .addComponent(jTextField23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel25)
                            .addComponent(jTextField20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(40, 40, 40)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jProgressBar3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel29)
                    .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton26)
                        .addComponent(jButton27)
                        .addComponent(jButton28)
                        .addComponent(jButton25)
                        .addComponent(jButton24)))
                .addContainerGap())
        );

        jTabbedPane1.addTab("列表页模板生成", jPanel16);

        jLabel11.setText(MyBundle.getString("webpage.url")); // NOI18N

        jTextField9.setText("http://");
        jTextField9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField9ActionPerformed(evt);
            }
        });

        jButton17.setText(MyBundle.getString("fetch.content")); // NOI18N
        jButton17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton17ActionPerformed(evt);
            }
        });

        jLabel12.setText(MyBundle.getString("article.subject.not.tille")); // NOI18N

        jTextField10.setEditable(false);
        jTextField10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTextField10MousePressed(evt);
            }
        });

        jLabel13.setText(MyBundle.getString("head.of.article")); // NOI18N

        jTextField11.setEditable(false);
        jTextField11.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTextField11MousePressed(evt);
            }
        });

        jLabel14.setText(MyBundle.getString("tail.of.article")); // NOI18N

        jTextField12.setEditable(false);
        jTextField12.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTextField12MousePressed(evt);
            }
        });

        jButton18.setText(MyBundle.getString("generate.template")); // NOI18N
        jButton18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton18ActionPerformed(evt);
            }
        });

        jTextArea2.setColumns(20);
        jTextArea2.setLineWrap(true);
        jTextArea2.setRows(5);
        Util.addFindForTextArea(main,jTextArea2);
        jScrollPane11.setViewportView(jTextArea2);

        jButton19.setText(MyBundle.getString("clear")); // NOI18N
        jButton19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton19ActionPerformed(evt);
            }
        });

        jButton20.setText(MyBundle.getString("save")); // NOI18N
        jButton20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton20ActionPerformed(evt);
            }
        });

        jButton21.setText(MyBundle.getString("save.as")); // NOI18N
        jButton21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton21ActionPerformed(evt);
            }
        });

        jProgressBar1.setIndeterminate(true);

        jLabel15.setText(MyBundle.getString("fetching.content.of.webpage")); // NOI18N

        jLabel16.setText(MyBundle.getString("webpage.encoding")); // NOI18N

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "GBK", "UTF-8", "BIG5", "ISO-8859-1" }));

        jButton22.setText(MyBundle.getString("replace.lots")); // NOI18N
        jButton22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton22ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane11, javax.swing.GroupLayout.DEFAULT_SIZE, 789, Short.MAX_VALUE)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField9, javax.swing.GroupLayout.DEFAULT_SIZE, 483, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton17)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(15, 15, 15))
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 160, Short.MAX_VALUE)
                        .addComponent(jButton22)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton18)
                        .addGap(18, 18, 18)
                        .addComponent(jButton19)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton20)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton21))
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel13)
                            .addComponent(jLabel12)
                            .addComponent(jLabel14))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField12, javax.swing.GroupLayout.DEFAULT_SIZE, 665, Short.MAX_VALUE)
                            .addComponent(jTextField10, javax.swing.GroupLayout.DEFAULT_SIZE, 665, Short.MAX_VALUE)
                            .addComponent(jTextField11, javax.swing.GroupLayout.DEFAULT_SIZE, 665, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton17))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(jTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(jTextField12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane11, javax.swing.GroupLayout.DEFAULT_SIZE, 301, Short.MAX_VALUE)
                .addGap(10, 10, 10)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15)
                    .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton19)
                        .addComponent(jButton20)
                        .addComponent(jButton21)
                        .addComponent(jButton18)
                        .addComponent(jButton22)))
                .addContainerGap())
        );

        jTabbedPane1.addTab("内容页模板生成", jPanel15);

        jButton5.setText(MyBundle.getString("start.fetch")); // NOI18N
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setText(MyBundle.getString("save.template.content.page")); // NOI18N
        jButton6.setEnabled(false);
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder(MyBundle.getString("template.setting"))); // NOI18N

        jLabel7.setText(MyBundle.getString("template.of.keyword.list")); // NOI18N

        jTextField3.setEditable(false);

        jButton7.setText(MyBundle.getString("browse")); // NOI18N
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton10.setText(MyBundle.getString("browse")); // NOI18N
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jTextField4.setEditable(false);

        jLabel9.setText(MyBundle.getString("template.of.keyword.content")); // NOI18N

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel11Layout.createSequentialGroup()
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton10))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel11Layout.createSequentialGroup()
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 515, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton7)))
                .addContainerGap(51, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton10))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTextArea3.setBackground(new java.awt.Color(0, 0, 0));
        jTextArea3.setColumns(20);
        jTextArea3.setEditable(false);
        jTextArea3.setForeground(new java.awt.Color(255, 255, 255));
        jTextArea3.setRows(5);
        jTextArea3.setText("开始抓取关键字“XXXXX”的相关内容...\n");
        jScrollPane8.setViewportView(jTextArea3);

        jButton11.setText(MyBundle.getString("save.template.list.page")); // NOI18N
        jButton11.setEnabled(false);
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 799, Short.MAX_VALUE)
                    .addComponent(jPanel11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jProgressBar2, javax.swing.GroupLayout.DEFAULT_SIZE, 789, Short.MAX_VALUE))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jButton5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton6)))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton6)
                    .addComponent(jButton11)
                    .addComponent(jButton5))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 303, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jProgressBar2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane1.addTab("原创化内容生成", jPanel8);

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(settingPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(67, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(settingPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 388, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(66, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("设置", jPanel12);

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(autoBuildSitePanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 809, Short.MAX_VALUE)
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addComponent(autoBuildSitePanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 459, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("自动建站", jPanel14);

        jButton12.setText(MyBundle.getString("save")); // NOI18N
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        jTextField5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField5KeyTyped(evt);
            }
        });

        jLabel5.setText(MyBundle.getString("delete.tail.count")); // NOI18N

        jTextField6.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField6KeyTyped(evt);
            }
        });

        jLabel4.setText(MyBundle.getString("delete.head.count")); // NOI18N

        jButton14.setText(MyBundle.getString("add")); // NOI18N
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });

        jButton15.setText(MyBundle.getString("modify")); // NOI18N
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });

        jButton29.setText(MyBundle.getString("delete")); // NOI18N
        jButton29.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton29ActionPerformed(evt);
            }
        });

        jLabel6.setText(MyBundle.getString("content.to.be.replaced")); // NOI18N

        jLabel10.setText(MyBundle.getString("replace.content")); // NOI18N

        jLabel30.setText(MyBundle.getString("add.link.content")); // NOI18N

        jScrollPane13.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane13, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 789, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel9Layout.createSequentialGroup()
                                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel6)
                                    .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel30, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextField24, javax.swing.GroupLayout.DEFAULT_SIZE, 701, Short.MAX_VALUE)
                                    .addComponent(jTextField8, javax.swing.GroupLayout.DEFAULT_SIZE, 701, Short.MAX_VALUE)
                                    .addComponent(jTextField7, javax.swing.GroupLayout.DEFAULT_SIZE, 701, Short.MAX_VALUE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel9Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 293, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTextField5, javax.swing.GroupLayout.DEFAULT_SIZE, 296, Short.MAX_VALUE))))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(214, 214, 214)
                        .addComponent(jButton14)
                        .addGap(29, 29, 29)
                        .addComponent(jButton15)
                        .addGap(39, 39, 39)
                        .addComponent(jButton29)
                        .addGap(34, 34, 34)
                        .addComponent(jButton12)))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addComponent(jScrollPane13, javax.swing.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addGap(18, 18, 18)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel6)
                    .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel30)
                    .addComponent(jTextField24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(33, 33, 33)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton14)
                    .addComponent(jButton15)
                    .addComponent(jButton29)
                    .addComponent(jButton12))
                .addContainerGap())
        );

        jTabbedPane1.addTab("原创设置", jPanel9);

        jList1.setModel(new SearcherListModel());
        jList1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane14.setViewportView(jList1);

        jLabel31.setText(MyBundle.getString("collection.order")); // NOI18N

        jButton13.setText(MyBundle.getString("move.up")); // NOI18N
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        jButton30.setText(MyBundle.getString("move.down")); // NOI18N
        jButton30.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton30ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(195, 195, 195)
                .addComponent(jLabel31)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane14, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton30)
                    .addComponent(jButton13))
                .addContainerGap(276, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(109, 109, 109)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(jButton13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton30))
                    .addComponent(jScrollPane14, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel31))
                .addContainerGap(132, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("采集设置", jPanel10);

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(recurringContentPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(17, Short.MAX_VALUE))
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addComponent(recurringContentPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("循环生成", jPanel18);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 814, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 493, Short.MAX_VALUE)
        );

        jTabbedPane1.getAccessibleContext().setAccessibleName("");
    }// </editor-fold>//GEN-END:initComponents

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        doSave();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        File file = getOpenSelectedFile(MyBundle.getString("webpage.file"), "htm", "html");
        if (file != null) {
            String pageContent = Util.readFile(file, workSpace.getWebPage().getSetting().getReadEncoding());
            workSpace.getWebPage().setOriginalContent(pageContent);
            webContentJTA.setText(pageContent);
            jTextField1.setText(file.getAbsolutePath());
            jRadioButton1.setEnabled(true);
            jRadioButton2.setEnabled(true);
            jButton2.setEnabled(true);
            jTextField2.setEnabled(true);
            jButton8.setEnabled(true);
            workSpace.setOpenFile(file);
            if (workSpace.getWebPage().getKeywords().size() > 0) {
                searchKeywordDone();
            }
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jRadioButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton2ActionPerformed
        jTextField2.setText("");
        jLabel2.setText(MyBundle.getString("search.keyword"));
        jButton2.setText(MyBundle.getString("search"));
    }//GEN-LAST:event_jRadioButton2ActionPerformed

    private void jRadioButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton1ActionPerformed
        jTextField2.setText("");
        jLabel2.setText(MyBundle.getString("keyword.to.page"));
        jButton2.setText(MyBundle.getString("ok"));
    }//GEN-LAST:event_jRadioButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        doSearchStart();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        doSearchStart();
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        doSaveAs();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        int i = JOptionPane.showConfirmDialog(this, MyBundle.getString("confirm.close.workspace"),
                MyBundle.getString("confirm"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (i == JOptionPane.YES_OPTION) {
            main.removeWorkPanel(this);
        }
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        doSaveWorkSpace();
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        String cmd = evt.getActionCommand();
        final JButton jb = (JButton) evt.getSource();
        jb.setEnabled(false);
        if (MyBundle.getString("start.fetch").equals(cmd)) {
            changeTextThread = new Thread() {

                public void run() {
                    try {
                        Thread.sleep(1000L);
                        jb.setText(MyBundle.getString("stop.fetch"));
                        jb.setEnabled(true);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            };
            changeTextThread.start();
            startFetchKeywordContent();
        } else {
            changeTextThread = new Thread() {

                public void run() {
                    try {
                        Thread.sleep(1000L);
                        jb.setText(MyBundle.getString("start.fetch"));
                        jb.setEnabled(true);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            };
            changeTextThread.start();
            stopFetchKeywordContent();
        }



    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        doSaveKeywordListPage();
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        doSaveKeywordContentPage();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        File file = getOpenSelectedFile(MyBundle.getString("webpage.file"), "htm", "html");
        if (file != null) {
            String pageContent = Util.readFile(file, workSpace.getWebPage().getSetting().getReadEncoding());
            workSpace.getWebPage().setKeywordListTemplate(new KeywordListTemplate(pageContent));
            jTextField3.setText(file.getAbsolutePath());
            workSpace.setKeywordListTemplate(file);
        }
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        File file = getOpenSelectedFile(MyBundle.getString("webpage.file"), "htm", "html");
        if (file != null) {
            String pageContent = Util.readFile(file, workSpace.getWebPage().getSetting().getReadEncoding());
            workSpace.getWebPage().setKeywordContentTemplate(new KeywordContentTemplate(pageContent));
            jTextField4.setText(file.getAbsolutePath());
            workSpace.setKeywordContentTemplate(file);
        }
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jTextField9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField9ActionPerformed
        fetchContentTemplateContent();
    }//GEN-LAST:event_jTextField9ActionPerformed

    private void jButton17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton17ActionPerformed
        fetchContentTemplateContent();
    }//GEN-LAST:event_jButton17ActionPerformed

    private void jButton19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton19ActionPerformed
        jTextField9.setText("http://");
        jTextField10.setText("");
        jTextField11.setText("");
        jTextField12.setText("");
        jTextArea2.setText("");
        jButton18.setEnabled(true);
    }//GEN-LAST:event_jButton19ActionPerformed

    private void jButton20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton20ActionPerformed
        if (checkSaveContentTemplate()) {
            File mainFile = workSpace.getWebPage().getMainFile();
            File file = new File(mainFile.getParent(), "content.htm");
            saveToFile(file, jTextArea2.getText());
        }
    }//GEN-LAST:event_jButton20ActionPerformed

    private void jButton21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton21ActionPerformed
        File file = getSaveSelectedFile(MyBundle.getString("webpage.file"), "html", "htm");
        if (file != null) {
            saveToFile(file, jTextArea2.getText(), ".htm", ".html");
        }
    }//GEN-LAST:event_jButton21ActionPerformed

    private void jButton18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton18ActionPerformed
        generateContentTemplate();
    }//GEN-LAST:event_jButton18ActionPerformed

    private void jButton22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton22ActionPerformed
        String content = jTextArea2.getText();
        if (Util.isEmpty(content)) {
            JOptionPane.showMessageDialog(this, MyBundle.getString("current.content.empty.no.replace"));
            return;
        }
        ReplaceDialog d = new ReplaceDialog(main, content);
        d.setLocationRelativeTo(this);
        d.setVisible(true);
        String newContent = d.getNewContent();
        if (newContent != null) {
            jTextArea2.setText(newContent);
        }
    }//GEN-LAST:event_jButton22ActionPerformed

    private void jTextField13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField13ActionPerformed
        fetchListTemplateContent();
    }//GEN-LAST:event_jTextField13ActionPerformed

    private void jButton23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton23ActionPerformed
        fetchListTemplateContent();
    }//GEN-LAST:event_jButton23ActionPerformed

    private void jButton24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton24ActionPerformed
        String content = jTextArea4.getText();
        if (Util.isEmpty(content)) {
            JOptionPane.showMessageDialog(this, MyBundle.getString("current.content.empty.no.replace"));
            return;
        }
        ReplaceDialog d = new ReplaceDialog(main, jTextArea4.getText());
        d.setLocationRelativeTo(this);
        d.setVisible(true);
        String newContent = d.getNewContent();
        if (newContent != null) {
            jTextArea4.setText(newContent);
        }
    }//GEN-LAST:event_jButton24ActionPerformed

    private void jButton25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton25ActionPerformed
        generateListTemplate();
    }//GEN-LAST:event_jButton25ActionPerformed

    private void jButton26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton26ActionPerformed
        jTextField13.setText("http://");
        jTextArea4.setText("");
        jTextField14.setText("");
        jTextField15.setText("");
        jTextField16.setText("");
        jTextField17.setText("");
        jTextField18.setText("");
        jTextField19.setText("");
        jTextField20.setText("");
        jTextField21.setText("");
        jTextField22.setText("");
        jTextField23.setText("");
    }//GEN-LAST:event_jButton26ActionPerformed

    private void jButton27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton27ActionPerformed
        if (checkSaveListTemplate()) {
            File mainFile = workSpace.getWebPage().getMainFile();
            File file = new File(mainFile.getParent(), "list.htm");
            saveToFile(file, jTextArea4.getText());
        }
    }//GEN-LAST:event_jButton27ActionPerformed

    private void jButton28ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton28ActionPerformed
        File file = getSaveSelectedFile(MyBundle.getString("webpage.file"), "html", "htm");
        if (file != null) {
            saveToFile(file, jTextArea4.getText(), ".htm", ".html");
        }
    }//GEN-LAST:event_jButton28ActionPerformed
    private void replaceLinksUpdate(JTextField jtf, int index) {
        String text = Util.getTextAreaContent(main, jtf.getText());
        if (text != null) {
            jtf.setText(text);
            replaceLinks.set(index, text);
        }
    }
    private void jTextField14MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField14MousePressed
        replaceLinksUpdate((JTextField) evt.getSource(), 0);
    }//GEN-LAST:event_jTextField14MousePressed

    private void jTextField21MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField21MousePressed
        replaceLinksUpdate((JTextField) evt.getSource(), 1);
    }//GEN-LAST:event_jTextField21MousePressed

    private void jTextField15MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField15MousePressed
        replaceLinksUpdate((JTextField) evt.getSource(), 2);
    }//GEN-LAST:event_jTextField15MousePressed

    private void jTextField19MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField19MousePressed
        replaceLinksUpdate((JTextField) evt.getSource(), 3);
    }//GEN-LAST:event_jTextField19MousePressed

    private void jTextField16MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField16MousePressed
        replaceLinksUpdate((JTextField) evt.getSource(), 4);
    }//GEN-LAST:event_jTextField16MousePressed

    private void jTextField22MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField22MousePressed
        replaceLinksUpdate((JTextField) evt.getSource(), 5);
    }//GEN-LAST:event_jTextField22MousePressed

    private void jTextField17MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField17MousePressed
        replaceLinksUpdate((JTextField) evt.getSource(), 6);
    }//GEN-LAST:event_jTextField17MousePressed

    private void jTextField23MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField23MousePressed
        replaceLinksUpdate((JTextField) evt.getSource(), 7);
    }//GEN-LAST:event_jTextField23MousePressed

    private void jTextField18MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField18MousePressed
        replaceLinksUpdate((JTextField) evt.getSource(), 8);
    }//GEN-LAST:event_jTextField18MousePressed

    private void jTextField20MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField20MousePressed
        replaceLinksUpdate((JTextField) evt.getSource(), 9);
    }//GEN-LAST:event_jTextField20MousePressed

    private void jTextField10MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField10MousePressed
        String text = Util.getTextAreaContent(main, jTextField10.getText());
        if (text != null) {
            articleTitle = text;
            jTextField10.setText(text);
        }
    }//GEN-LAST:event_jTextField10MousePressed

    private void jTextField11MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField11MousePressed
        String text = Util.getTextAreaContent(main, jTextField11.getText());
        if (text != null) {
            articleHead = text;
            jTextField11.setText(text);
        }
    }//GEN-LAST:event_jTextField11MousePressed

    private void jTextField12MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField12MousePressed
        String text = Util.getTextAreaContent(main, jTextField2.getText());
        if (text != null) {
            articleTail = text;
            jTextField12.setText(text);
        }
    }//GEN-LAST:event_jTextField12MousePressed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        try {
            try {
                info.setHeadTrimCount(Integer.parseInt(jTextField6.getText()));
                info.setTailTrimCount(Integer.parseInt(jTextField5.getText()));

            } catch (NumberFormatException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, MyBundle.getString("delete.count.must.be.number"));
                return;
            }
            Util.saveReplaceInfo(info);
            JOptionPane.showMessageDialog(this, MyBundle.getString("save.succ"));
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, MyBundle.getString("save.failure") + ex.toString());
        }
}//GEN-LAST:event_jButton12ActionPerformed

    private void jTextField5KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField5KeyTyped
        if (!Character.isDigit(evt.getKeyChar())) {
            evt.consume();
        }
}//GEN-LAST:event_jTextField5KeyTyped

    private void jTextField6KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField6KeyTyped
        if (!Character.isDigit(evt.getKeyChar())) {
            evt.consume();
        }
}//GEN-LAST:event_jTextField6KeyTyped

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        if (Util.isEmpty(jTextField7.getText())) {
            JOptionPane.showMessageDialog(this, MyBundle.getString("current.content.empty.no.replace"));
            return;
        }
        ReplaceContent content = new ReplaceContent();
        content.setKey(jTextField7.getText());
        content.setValue(jTextField8.getText());
        content.setLink(jTextField24.getText());
        info.getReplaceContents().add(content);
        SwingUtilities.updateComponentTreeUI(jScrollPane13);
        jTextField7.setText("");
        jTextField8.setText("");
        jTextField24.setText("");
}//GEN-LAST:event_jButton14ActionPerformed

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
        int index = jTable1.getSelectedRow();
        if (index > -1) {
            index = ts.convertRowIndexToModel(index);
            ReplaceContent rc = info.getReplaceContents().get(index);
            if (Util.isEmpty(jTextField7.getText())) {
                JOptionPane.showMessageDialog(this, MyBundle.getString("current.content.empty.no.replace"));
            } else {
                rc.setKey(jTextField7.getText());
                rc.setValue(jTextField8.getText());
                rc.setLink(jTextField24.getText());
                SwingUtilities.updateComponentTreeUI(jScrollPane13);
            }
        }
}//GEN-LAST:event_jButton15ActionPerformed

    private void jButton29ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton29ActionPerformed
        int index = jTable1.getSelectedRow();
        if (index > -1) {
            index = ts.convertRowIndexToModel(index);
            info.getReplaceContents().remove(index);
            SwingUtilities.updateComponentTreeUI(jScrollPane13);
        }
}//GEN-LAST:event_jButton29ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        Object value = jList1.getSelectedValue();
        Setting setting = workSpace.getWebPage().getSetting();
        if (value != null) {
            Vector<MySearcher> v = setting.getSearchers();
            MySearcher content = (MySearcher) value;
            int index = -1;
            for (int i = 0; i < v.size(); i++) {
                if (v.get(i).equals(content)) {
                    index = i;
                    break;
                }
            }
            if (index > 0) {
                v.remove(content);
                v.add(index - 1, content);
            }
            jList1.setListData(v);
            jList1.setSelectedValue(content, true);
        }
}//GEN-LAST:event_jButton13ActionPerformed

    private void jButton30ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton30ActionPerformed
        Object value = jList1.getSelectedValue();
        Setting setting = workSpace.getWebPage().getSetting();
        if (value != null) {
            Vector<MySearcher> v = setting.getSearchers();
            MySearcher content = (MySearcher) value;
            int index = -1;
            for (int i = 0; i < v.size(); i++) {
                if (v.get(i).equals(content)) {
                    index = i;
                    break;
                }
            }
            if (index != -1 && index < v.size() - 1) {
                v.remove(content);
                v.add(index + 1, content);
            }
            jList1.setListData(v);
            jList1.setSelectedValue(content, true);
        }
}//GEN-LAST:event_jButton30ActionPerformed
    private boolean checkSaveContentTemplate() {
        if (workSpace.getWebPage().getMainFile() == null) {
            JOptionPane.showMessageDialog(this, MyBundle.getString("pls.save.main.page.fist"));
            return false;
        } else if (Util.isEmpty(jTextArea2.getText())) {
            JOptionPane.showMessageDialog(this, MyBundle.getString("content.to.save.can.not.empty"));
            return false;
        }
        return true;
    }

    private boolean checkSaveListTemplate() {
        if (workSpace.getWebPage().getMainFile() == null) {
            JOptionPane.showMessageDialog(this, MyBundle.getString("pls.save.main.page.fist"));
            return false;
        } else if (Util.isEmpty(jTextArea4.getText())) {
            JOptionPane.showMessageDialog(this, MyBundle.getString("content.to.save.can.not.empty"));
            return false;
        }
        return true;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea allCodeJTA;
    private javax.swing.JTextArea allJTA;
    private com.hadeslee.webpage.ui.panel.AutoBuildSitePanel autoBuildSitePanel1;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JTextArea centerHJTA;
    private javax.swing.JTextArea descriptionJTA;
    private javax.swing.JTextArea friendJTA;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton17;
    private javax.swing.JButton jButton18;
    private javax.swing.JButton jButton19;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton20;
    private javax.swing.JButton jButton21;
    private javax.swing.JButton jButton22;
    private javax.swing.JButton jButton23;
    private javax.swing.JButton jButton24;
    private javax.swing.JButton jButton25;
    private javax.swing.JButton jButton26;
    private javax.swing.JButton jButton27;
    private javax.swing.JButton jButton28;
    private javax.swing.JButton jButton29;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton30;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JList jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JProgressBar jProgressBar2;
    private javax.swing.JProgressBar jProgressBar3;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane15;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JTextArea jTextArea3;
    private javax.swing.JTextArea jTextArea4;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField11;
    private javax.swing.JTextField jTextField12;
    private javax.swing.JTextField jTextField13;
    private javax.swing.JTextField jTextField14;
    private javax.swing.JTextField jTextField15;
    private javax.swing.JTextField jTextField16;
    private javax.swing.JTextField jTextField17;
    private javax.swing.JTextField jTextField18;
    private javax.swing.JTextField jTextField19;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField20;
    private javax.swing.JTextField jTextField21;
    private javax.swing.JTextField jTextField22;
    private javax.swing.JTextField jTextField23;
    private javax.swing.JTextField jTextField24;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    private javax.swing.JTextArea keywordJTA;
    private javax.swing.JLabel myLabel;
    private javax.swing.JProgressBar myProgressBar;
    private javax.swing.JTextArea navJTA;
    private com.hadeslee.webpage.ui.panel.RecurringContentPanel recurringContentPanel1;
    private com.hadeslee.webpage.ui.panel.SettingPanel settingPanel1;
    private javax.swing.JTextArea titleJTA;
    private javax.swing.JTextArea webContentJTA;
    // End of variables declaration//GEN-END:variables

    public void propertyChange(PropertyChangeEvent evt) {
        if (Setting.TEXTAREA_WRAP.equals(evt.getPropertyName())) {
            boolean wrap = (Boolean) evt.getNewValue();
            titleJTA.setLineWrap(wrap);
            descriptionJTA.setLineWrap(wrap);
            keywordJTA.setLineWrap(wrap);
            navJTA.setLineWrap(wrap);
            centerHJTA.setLineWrap(wrap);
            allJTA.setLineWrap(wrap);
            allCodeJTA.setLineWrap(wrap);
            jTextArea2.setLineWrap(wrap);
            jTextArea4.setLineWrap(wrap);
        } else if (Setting.HN_CHANGE.equals(evt.getPropertyName())) {
            String hn = (String) evt.getNewValue();
            centerHJTA.setText(workSpace.getWebPage().getCenterH(hn));
        }
    }

    private void stopFetchKeywordContent() {
        for (KeyWordSearcher keyWordSearcher : keywordSearchers) {
            keyWordSearcher.removeKeyWordSearchListener(this);
            keyWordSearcher.cancel();
        }
        jProgressBar2.setVisible(false);
        appendInfo("搜索已停止!");
    }

    private void startFetchKeywordContent() {
        currentIndex = -1;
        for (KeyWordSearcher keyWordSearcher : keywordSearchers) {
            keyWordSearcher.removeKeyWordSearchListener(this);
            keyWordSearcher.cancel();
        }
        keywordSearchers.clear();
        List<String> keywords = workSpace.getWebPage().getKeywords();
        System.out.println("keywords=" + keywords);
        ReplaceInfo pro = null;
        if (workSpace.getWebPage().getSetting().isOriginalContent()) {
            pro = Util.readReplaceInfo();
        }
        System.out.println("pro = " + pro);
        for (String s : keywords) {
            KeyWordSearcher keyWordSearcher = new KeyWordSearcher(s, workSpace.getWebPage().getSetting().getKeywordPageCount(), pro, workSpace.getWebPage().getSetting());
            keyWordSearcher.addKeyWordSearchListener(this);
            keywordSearchers.add(keyWordSearcher);
        }
        if (keywordSearchers.size() > 0) {
            System.gc();
            System.gc();
            System.gc();
            currentIndex = 0;
            jTextArea3.setText(null);
            keywordSearchers.get(0).startSearch();
            jButton11.setEnabled(false);
            jButton6.setEnabled(false);
            jProgressBar2.setMaximum(keywordSearchers.size() * workSpace.getWebPage().getSetting().getKeywordPageCount());
            jProgressBar2.setMinimum(0);
            jProgressBar2.setValue(0);
            jProgressBar2.setVisible(true);
        } else {
            changeTextThread.interrupt();
            jButton5.setText("开始抓取");
            jButton5.setEnabled(true);
            JOptionPane.showMessageDialog(this, "当前没有关键字可以搜索");
        }
    }

    /**
     * 检查是否能能保存关键字列表的页面，看看内容
     * 是不是准备齐了
     */
    private boolean checkSaveKeywordListPage() {
        try {
            if (workSpace.getWebPage().getKeywordListTemplate() == null) {
                JOptionPane.showMessageDialog(this, "请先指定关键字列表的模版!");
                return false;
            }
            if (workSpace.getWebPage().getMainFile() == null) {
                JOptionPane.showMessageDialog(this, "请先保存主页面!");
                return false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "出错了，异常如下：" + ex);
            return false;
        }
        return true;
    }

    private boolean checkSaveKeywordContentPage() {
        try {
            if (workSpace.getWebPage().getKeywordContentTemplate() == null) {
                JOptionPane.showMessageDialog(this, "请先指定关键字内容的模版!");
                return false;
            }
            if (workSpace.getWebPage().getMainFile() == null) {
                JOptionPane.showMessageDialog(this, "请先保存主页面!");
                return false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "出错了，异常如下：" + ex);
            return false;
        }
        return true;
    }

    private void doSaveKeywordListPage0() {
        jButton6.setEnabled(false);
        jButton5.setEnabled(false);
        jProgressBar2.setVisible(true);
        jProgressBar2.setMaximum(keywordSearchers.size());
        jProgressBar2.setMinimum(0);
        jProgressBar2.setValue(0);
        try {
            KeywordListTemplate template = workSpace.getWebPage().getKeywordListTemplate();
            for (KeyWordSearcher searcher : keywordSearchers) {
                jProgressBar2.setValue(jProgressBar2.getValue() + 1);
                KeywordListValueHolder holder = new KeywordListValueHolder(searcher,
                        workSpace.getWebPage().getMainFile(),
                        Util.searchKeyWords(workSpace.getWebPage().getSetting(), searcher.getKeyword()));
                String temp = template.applyTemplateValue(holder);
                File file = getKeywordListPageFile(searcher.getKeyword());
                appendInfo("正在保存文件到:" + file.getPath());
                Util.saveFile(temp, workSpace.getWebPage().getSetting().getWriteEncoding(), file);
            }
            JOptionPane.showMessageDialog(this, "关键字列表文件保存完毕");
        } finally {
            jButton6.setEnabled(true);
            jButton5.setEnabled(true);
            jProgressBar2.setVisible(false);
        }

    }

    private void doSaveKeywordListPage() {
        if (checkSaveKeywordListPage()) {
            new Thread() {

                public void run() {
                    doSaveKeywordListPage0();
                }
            }.start();
        }
    }

    private File getKeywordListPageFile(String keyword) {
        File mainFile = workSpace.getWebPage().getMainFile();
        File file = new File(mainFile.getParent(), Util.getPinyin(keyword) + ".htm");
        file.getParentFile().mkdirs();
        return file;
    }

    private File getKeywordContentPageFile(String keyword, Article article) {
        File mainFile = workSpace.getWebPage().getMainFile();
        File file = new File(mainFile.getParent() + "/" + Util.getPinyin(keyword), Util.generateUniqueName(article.getTitle(), "htm", mainFile.getParentFile()));
        file.getParentFile().mkdirs();
        return file;
    }

    private int getArticleCount() {
        int count = 0;
        for (KeyWordSearcher searcher : keywordSearchers) {
            count += searcher.getContent().size();
        }
        return count;
    }

    private void doSaveKeywordContentPage0() {
        jButton11.setEnabled(false);
        jButton5.setEnabled(false);
        jProgressBar2.setVisible(true);
        jProgressBar2.setMaximum(getArticleCount());
        jProgressBar2.setMinimum(0);
        jProgressBar2.setValue(0);
        try {
            KeywordContentTemplate template = workSpace.getWebPage().getKeywordContentTemplate();
            for (KeyWordSearcher searcher : keywordSearchers) {
                for (Article article : searcher.getContent()) {
                    KeywordContentValueHolder holder = new KeywordContentValueHolder(article, workSpace.getWebPage().getKeywords());
                    String temp = template.applyTemplateValue(holder);
                    File file = getKeywordContentPageFile(searcher.getKeyword(), article);
                    jProgressBar2.setValue(jProgressBar2.getValue() + 1);
                    appendInfo("正在保存文件:" + file.getPath());
                    Util.saveFile(temp, workSpace.getWebPage().getSetting().getWriteEncoding(), file);
                }
            }
            JOptionPane.showMessageDialog(this, "关键字内容文件保存完毕");
        } finally {
            jButton11.setEnabled(true);
            jButton5.setEnabled(true);
            jProgressBar2.setVisible(false);
        }
    }

    private void doSaveKeywordContentPage() {
        if (checkSaveKeywordContentPage()) {
            new Thread() {

                public void run() {
                    doSaveKeywordContentPage0();
                }
            }.start();
        }
    }

    public void keywordStartSearch(String keyword) {
        appendInfo("正在搜索关键字“" + keyword + "”...");
    }

    private void appendInfo(String info) {
        jTextArea3.append(info + "\n");
        jTextArea3.setCaretPosition(jTextArea3.getText().length());
    }

    public void articleSearched(String keyword, Article article) {
        int value = jProgressBar2.getValue() + 1;
        jProgressBar2.setValue(value);
        appendInfo("搜索到关键字“" + keyword + "”相关的文章:" + article.getTitle());
    }

    public synchronized void searchDone(KeyWordSearcher keyWordSearcher) {
        if (currentIndex + 1 < keywordSearchers.size()) {
            currentIndex += 1;
            keywordSearchers.get(currentIndex).startSearch();
        } else {
            jButton11.setEnabled(true);
            jButton6.setEnabled(true);
            jProgressBar2.setVisible(false);
            appendInfo("关键字搜索完成");
            jButton5.setText("开始抓取");
            JOptionPane.showMessageDialog(this, "关键字搜索完成!");
        }
    }

    private void fetchListTemplateContent() {
        new Thread() {

            public void run() {
                fetchListTemplateContent0();
            }
        }.start();
    }

    private void fetchListTemplateContent0() {
        String url = jTextField13.getText().trim();
        if (Util.isEmpty(url)) {
            JOptionPane.showMessageDialog(this, "网页地址不能为空!");
            return;
        }
        if (!url.toLowerCase().startsWith("http://")) {
            JOptionPane.showMessageDialog(this, "网页内容必须以http://开头!");
        } else if (url.length() == 7) {
            JOptionPane.showMessageDialog(this, "网页地址不能为空!");
        } else {
            try {
                jLabel29.setVisible(true);
                jProgressBar3.setVisible(true);
                String content = Util.readUrl(url, String.valueOf(jComboBox2.getSelectedItem()));
                jTextArea4.setText(content);
                jButton25.setEnabled(true);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "出现了错误 ，如下:\n" + ex.toString());
            } finally {
                jLabel29.setVisible(false);
                jProgressBar3.setVisible(false);
            }
        }
    }

    private void fetchContentTemplateContent() {
        new Thread() {

            public void run() {
                fetchContentTemplateContent0();
            }
        }.start();
    }

    private void fetchContentTemplateContent0() {
        String url = jTextField9.getText().trim();
        if (Util.isEmpty(url)) {
            JOptionPane.showMessageDialog(this, "网页地址不能为空!");
            return;
        }
        if (!url.toLowerCase().startsWith("http://")) {
            JOptionPane.showMessageDialog(this, "网页内容必须以http://开头!");
        } else if (url.length() == 7) {
            JOptionPane.showMessageDialog(this, "网页地址不能为空!");
        } else {
            try {
                jLabel15.setVisible(true);
                jProgressBar1.setVisible(true);
                String content = Util.readUrl(url, String.valueOf(jComboBox1.getSelectedItem()));
                jTextArea2.setText(content);
                jButton18.setEnabled(true);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "出现了错误 ，如下:\n" + ex.toString());
            } finally {
                jLabel15.setVisible(false);
                jProgressBar1.setVisible(false);
            }
        }
    }

    private void generateContentTemplate() {
        String content = jTextArea2.getText();
        if (Util.isEmpty(content)) {
            JOptionPane.showMessageDialog(this, "读取到的内容不能为空");
        } else {
            String title = jTextField10.getText();
            if (!Util.isEmpty(title)) {
                content = content.replace(title, "<h3>{文章标题}</h3>");
            }
            Matcher m = Pattern.compile("<title>(.*?)</title>", Pattern.DOTALL | Pattern.CASE_INSENSITIVE).matcher(content);
            if (m.find()) {
                content = m.replaceAll("<title>{文章标题}</title>\n");
            }
            String head = articleHead;
            String tail = articleTail;
            if (!Util.isEmpty(head) && !Util.isEmpty(tail)) {
                int from = content.indexOf(head);
                int to = content.lastIndexOf(tail);
                if (from != -1 && to != -1) {
                    StringBuilder sb = new StringBuilder(content);
                    sb.replace(from, to, "{文章内容}\n"
                            + " <center><h1>{关键词1}|{关键词2}|{关键词3}|{关键词4}</h1></center>\n");
                    content = sb.toString().replace(tail, "");
                }
            }
            content = Util.replaceSrc_href(content);
//            content = content.replaceFirst("(?i)(<body.*?>)", "$1\n"
//                    + "<script src=\"../tiaozhuan.js\"></script>\n"
//                    + "<script src=\"../tanchuang.js\"></script>\n"
//                    + "<script src=\"../jintan.js\"></script>\n"
//                    + "<script src=\"../tuitan.js\"></script>");
            jTextArea2.setText(content);
            jButton18.setEnabled(false);
            JOptionPane.showMessageDialog(this, "内容模版生成完毕!");
        }
    }

    private void generateListTemplate() {
        String content = jTextArea4.getText();
        if (Util.isEmpty(content)) {
            JOptionPane.showMessageDialog(this, "读取到的内容不能为空");
        } else {
            content = Util.replaceListTemplateTitle(content);
            for (int i = 0; i < replaceLinks.size(); i++) {
                content = Util.replaceLinkOfListTemplate(content, replaceLinks.get(i), i + 1);
            }
//            content = content.replaceFirst("(?i)(<body.*?>)", "$1\n"
//                    + "<script src=\"../tiaozhuan.js\"></script>\n"
//                    + "<script src=\"../tanchuang.js\"></script>\n"
//                    + "<script src=\"../jintan.js\"></script>\n"
//                    + "<script src=\"../tuitan.js\"></script>");
            jTextArea4.setText(content);
            jButton25.setEnabled(false);
            JOptionPane.showMessageDialog(this, "列表模版生成完毕!");
        }
    }
}
