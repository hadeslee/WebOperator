/*
 * ClipBoardListener.java
 *
 * Created on 2007年1月4日, 下午3:56
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.hadeslee.webpage.util;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.TransferHandler;
import javax.swing.text.JTextComponent;

/**
 *
 * @author lbf
 */
public class ClipBoardListener implements MouseListener {

    /** Creates a new instance of ClipBoardListener */
    public ClipBoardListener() {
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
        Object obj = e.getSource();
        if (obj instanceof JTextComponent && e.isPopupTrigger()) {
            final JTextComponent jtc = (JTextComponent) obj;
            try {
                boolean isHaveText = false;
                try {
                    String data = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
                    if (data != null || !data.equals("")) {
                        isHaveText = true;
                    }
                } catch (UnsupportedFlavorException ue) {
                }
                JPopupMenu jp = new JPopupMenu(MyBundle.getString("option"));
                JMenuItem clear = new JMenuItem(MyBundle.getString("clear.it"));
                jp.add(clear);
                if (jtc.isEditable()) {
                    clear.addActionListener(new ActionListener() {

                        public void actionPerformed(ActionEvent ae) {
                            jtc.setText(null);
                        }
                    });
                } else {
                    clear.setEnabled(false);
                }
                JMenuItem copy = new JMenuItem(MyBundle.getString("copy"));
                jp.add(copy);
                String selected = jtc.getSelectedText();
                if (selected == null) {
                    copy.setEnabled(false);
                } else {
                    copy.addActionListener(new ActionListener() {

                        public void actionPerformed(ActionEvent ae) {
                            jtc.copy();
                        }
                    });
                }
                JMenuItem cut = new JMenuItem(MyBundle.getString("cut"));
                jp.add(cut);
                if (selected == null || !jtc.isEditable() || !jtc.isEnabled()) {
                    cut.setEnabled(false);
                } else {
                    cut.addActionListener(new ActionListener() {

                        public void actionPerformed(ActionEvent ae) {
                            jtc.cut();
                        }
                    });
                }
                JMenuItem paste = new JMenuItem(MyBundle.getString("paste"));
                jp.add(paste);
                if (!isHaveText || !jtc.isEditable() || !jtc.isEnabled()) {
                    paste.setEnabled(false);
                } else {
                    paste.addActionListener(new ActionListener() {

                        public void actionPerformed(ActionEvent ae) {
                            jtc.paste();
                        }
                    });
                }
                JMenuItem selectAll = new JMenuItem(MyBundle.getString("select.all"));
                jp.add(selectAll);
                selectAll.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent ae) {
                        jtc.selectAll();
                    }
                });
                jp.setBorderPainted(true);
                jp.show(jtc, e.getX(), e.getY());
            } catch (Exception exe) {
                exe.printStackTrace();
            }
        }
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }
}
