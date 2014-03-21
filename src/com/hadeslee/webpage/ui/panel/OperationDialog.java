/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * OperationDialog.java
 *
 * Created on 2010-1-1, 17:49:20
 */
package com.hadeslee.webpage.ui.panel;

import com.hadeslee.webpage.util.MyTableColumnModel;
import com.hadeslee.webpage.util.ReplaceInfo;
import com.hadeslee.webpage.util.ReplaceInfo.ReplaceContent;
import com.hadeslee.webpage.util.Util;
import java.awt.Color;
import java.awt.Component;
import java.awt.Rectangle;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author Hadeslee
 */
public class OperationDialog extends javax.swing.JDialog {

    static {

        try {
//            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//            UIManager.setLookAndFeel("com.birosoft.liquid.LiquidLookAndFeel");
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
//             com.birosoft.liquid.LiquidLookAndFeel.setLiquidDecorations(true);
//             com.birosoft.liquid.LiquidLookAndFeel.setShowTableGrids(true);
//             com.birosoft.liquid.LiquidLookAndFeel.setStipples(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    private TableRowSorter<TableModel> ts;
    private ReplaceInfo info;

    /** Creates new form OperationDialog */
    public OperationDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initOther();
        initComponents();
        jTextField4.setTransferHandler(null);
        jTextField5.setTransferHandler(null);
        jTextField4.setText(String.valueOf(info.getHeadTrimCount()));
        jTextField5.setText(String.valueOf(info.getTailTrimCount()));
        Util.addClipboardListener(this);
    }

    private void initOther() {
        info = Util.readReplaceInfo();
    }

    private JTable initTable() {
        MyTableColumnModel mcm = new MyTableColumnModel(new MyTableCellRenderer());
        mcm.setHeads("被替换的内容", "替换的内容", "添加的链接");
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
        jt.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        jt.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting() == false) {
                    int index = jTable1.getSelectedRow();
                    if (index > -1) {
                        index = ts.convertRowIndexToModel(index);
                        ReplaceContent rc = info.getReplaceContents().get(index);
                        jTextField1.setText(rc.getKey());
                        jTextField2.setText(rc.getValue());
                        jTextField3.setText(rc.getLink());
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
                this.setBackground(new Color(255, 255, 120));
                this.setForeground(UIManager.getColor("Table.selectionForeground"));
            } else {
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

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = initTable();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jButton5 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("制作替换窗口");

        jScrollPane1.setViewportView(jTable1);

        jLabel1.setText("被替换的内容：");

        jLabel2.setText("替换的内容：");

        jLabel3.setText("添加的链接：");

        jButton1.setText("修改");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("删除");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("退出");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("添加");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabel4.setText("删除前面字数：");

        jTextField4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField4KeyTyped(evt);
            }
        });

        jLabel5.setText("删除后面字数：");

        jTextField5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField5KeyTyped(evt);
            }
        });

        jButton5.setText("保存");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 489, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel3)
                                        .addComponent(jLabel2))
                                    .addComponent(jLabel1))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextField3, javax.swing.GroupLayout.DEFAULT_SIZE, 401, Short.MAX_VALUE)
                                    .addComponent(jTextField2, javax.swing.GroupLayout.DEFAULT_SIZE, 401, Short.MAX_VALUE)
                                    .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 401, Short.MAX_VALUE)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addComponent(jButton4)
                        .addGap(29, 29, 29)
                        .addComponent(jButton1)
                        .addGap(39, 39, 39)
                        .addComponent(jButton2)
                        .addGap(34, 34, 34)
                        .addComponent(jButton5)
                        .addGap(31, 31, 31)
                        .addComponent(jButton3)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel1)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel2)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel3)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(33, 33, 33)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton4)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton5)
                    .addComponent(jButton3))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        ReplaceContent content = new ReplaceContent();
        content.setKey(jTextField1.getText());
        content.setValue(jTextField2.getText());
        content.setLink(jTextField3.getText());
        info.getReplaceContents().add(content);
        SwingUtilities.updateComponentTreeUI(jScrollPane1);
        jTextField1.setText("");
        jTextField2.setText("");
        jTextField3.setText("");
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        int index = jTable1.getSelectedRow();
        if (index > -1) {
            index = ts.convertRowIndexToModel(index);
            ReplaceContent rc = info.getReplaceContents().get(index);
            if (Util.isEmpty(jTextField1.getText())) {
                JOptionPane.showMessageDialog(this, "被替换的内容不能为空！");
            } else {
                rc.setKey(jTextField1.getText());
                rc.setValue(jTextField2.getText());
                rc.setLink(jTextField3.getText());
                SwingUtilities.updateComponentTreeUI(jScrollPane1);
            }
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        int index = jTable1.getSelectedRow();
        if (index > -1) {
            index = ts.convertRowIndexToModel(index);
            info.getReplaceContents().remove(index);
            SwingUtilities.updateComponentTreeUI(jScrollPane1);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        this.dispose();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        try {
            try {
                info.setHeadTrimCount(Integer.parseInt(jTextField4.getText()));
                info.setTailTrimCount(Integer.parseInt(jTextField5.getText()));

            } catch (NumberFormatException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "删除的数字必须是数字！");
                return;
            }
            Util.saveReplaceInfo(info);
            JOptionPane.showMessageDialog(this, "保存成功！");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "保存失败：" + ex.toString());
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jTextField5KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField5KeyTyped
        if (!Character.isDigit(evt.getKeyChar())) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextField5KeyTyped

    private void jTextField4KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField4KeyTyped
        if (!Character.isDigit(evt.getKeyChar())) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextField4KeyTyped

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                OperationDialog dialog = new OperationDialog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {

                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    // End of variables declaration//GEN-END:variables
}
