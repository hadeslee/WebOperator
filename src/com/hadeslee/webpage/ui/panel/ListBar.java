package com.hadeslee.webpage.ui.panel;

import com.hadeslee.webpage.util.Util;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.basic.BasicListUI;

/**
 *
 * @author  Administrator
 */
public class ListBar extends javax.swing.JPanel implements MouseListener, MouseMotionListener {

    private Vector<String> names;
    private Map<String, Component> map;
    private int onIndex = -1;

    /** Creates new form ListBar */
    public ListBar() {
        initComponents();
        initOther();
    }

    public void removeAll() {
        names.clear();
        map.clear();
        content.removeAll();
        SwingUtilities.updateComponentTreeUI(this);
    }

    private void initOther() {
        map = new HashMap<String, Component>();
        names = new Vector<String>();
        list.setUI(new YOYOListUI());
        list.setFixedCellHeight(25);
        list.addMouseListener(this);
        list.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    updateSelected();
                }
            }
        });
        list.addMouseMotionListener(this);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);


    }

    public int getBarComponentCount() {
        return map.size();
    }

    public Component getBarComponent(int index) {
        return map.get(names.get(index));
    }

    public Component getComponent(String name) {
        return map.get(name);
    }

    private void updateSelected() {
        Object value = list.getSelectedValue();
        if (value != null) {
            String name = value.toString();
            Component com = map.get(name);
            if (com != null) {
                content.removeAll();
                content.add(com, BorderLayout.CENTER);
//                content.revalidate();
                SwingUtilities.updateComponentTreeUI(content);
            }
        }
    }

    public synchronized void addComponent(String name, Component com) {
        if (!names.contains(name)) {
            names.add(name);
        }
        Dimension size = com.getPreferredSize();
        Dimension me = content.getPreferredSize();
        me.width = size.width > me.width ? size.width : me.width;
        me.height = size.height > me.height ? size.height : me.height;
        content.setPreferredSize(me);
        map.put(name, com);
        list.setListData(names);
    }

    public void setSelectedComponent(String name) {
        Component com = map.get(name);
        if (com != null) {
            list.setSelectedValue(name, true);
            content.removeAll();
            content.add(com, BorderLayout.CENTER);
//                content.revalidate();
            SwingUtilities.updateComponentTreeUI(content);
        }
    }

    public void removeComponent(String name) {
    }

    public void removeComponent(Component com) {
    }

    private class MyList extends JList {

        private boolean uiseted;//UI是否已经设了

        public MyList() {
        }

//        @Override
//        public void setUI(ListUI ui) {
//            if (!uiseted) {
//                super.setUI(new YOYOListUI());
//                uiseted = true;
//            }
//        }
    }

    private class YOYOListUI extends BasicListUI {

        private Image line, select;

        public YOYOListUI() {
            try {
                line = ImageIO.read(this.getClass().getResource("line.png"));
                select = ImageIO.read(this.getClass().getResource("select.png"));
            } catch (IOException ex) {
                Logger.getLogger(ListBar.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        protected void paintCell(Graphics g, int row, Rectangle rowBounds, ListCellRenderer cellRenderer, ListModel dataModel, ListSelectionModel selModel, int leadIndex) {
            int width = rowBounds.width;
            int height = rowBounds.height;
            int x = rowBounds.x;
            int y = rowBounds.y;
            g.translate(x, y);
            String s = dataModel.getElementAt(row).toString();
            g.setColor(Color.BLACK);
            Util.drawString(g, s, (width - Util.getStringWidth(s, g)) / 2, (height - Util.getStringHeight(s, g)) / 2);
            if (selModel.isSelectedIndex(row)) {
                g.setColor(new Color(48, 106, 198));
                g.fillRect(0, 0, rowBounds.width, rowBounds.height);
                g.setColor(Color.WHITE);
                Util.drawString(g, s, (width - Util.getStringWidth(s, g)) / 2, (height - Util.getStringHeight(s, g)) / 2);
                g.drawImage(line, (width - line.getWidth(list)) / 2, height - 1, list);
                g.drawImage(select, 2, (height - select.getHeight(list)) / 2, list);
                g.setColor(new Color(120, 149, 226));
                g.drawRect(0, 0, width - 1, height - 1);
                g.setColor(new Color(192, 192, 255));
                g.drawLine(0, height - 1, width, height - 1);
            } else {
                g.drawImage(line, (width - line.getWidth(list)) / 2, height - 1, list);
            }
            if (row == onIndex) {
                g.setColor(new Color(151, 180, 226));
                g.fillRect(0, 0, rowBounds.width, rowBounds.height);
                g.setColor(Color.WHITE);
                Util.drawString(g, s, (width - Util.getStringWidth(s, g)) / 2, (height - Util.getStringHeight(s, g)) / 2);
                g.setColor(new Color(120, 149, 226));
                g.drawLine(0, height - 1, width, height - 1);
            }
            g.translate(0 - x, 0 - y);
        }
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
        onIndex = -1;
        list.repaint();
    }

    public void mouseDragged(MouseEvent e) {
    }

    public void mouseMoved(MouseEvent e) {
        onIndex = list.locationToIndex(e.getPoint());
        list.repaint();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        list = new MyList();
        content = new javax.swing.JPanel();

        jScrollPane1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        list.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(list);

        content.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        content.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(content, javax.swing.GroupLayout.DEFAULT_SIZE, 520, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 396, Short.MAX_VALUE)
                    .addComponent(content, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 396, Short.MAX_VALUE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel content;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JList list;
    // End of variables declaration//GEN-END:variables
}
