/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * Main.java
 *
 * Created on 2009-9-19, 16:03:01
 */
package com.hadeslee.webpage.ui;

import com.hadeslee.webpage.data.WorkSpace;
import com.hadeslee.webpage.ui.panel.WorkPanel;
import com.hadeslee.webpage.util.ClipBoardListener;
import com.hadeslee.webpage.util.MyBundle;
import com.hadeslee.webpage.util.R;
import com.hadeslee.webpage.util.RegInfo;
import com.hadeslee.webpage.util.RemoteRegInfo;
import com.hadeslee.webpage.util.Util;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URI;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.soap.MessageFactory;

/**
 *
 * @author Hadeslee
 */
public class Main extends javax.swing.JFrame implements ActionListener, Runnable {

    static {

        try {
//            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//            UIManager.setLookAndFeel("com.birosoft.liquid.LiquidLookAndFeel");
            UIManager.setLookAndFeel("com.jtattoo.plaf.mcwin.McWinLookAndFeel");
//            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
//             com.birosoft.liquid.LiquidLookAndFeel.setLiquidDecorations(true);
//             com.birosoft.liquid.LiquidLookAndFeel.setShowTableGrids(true);
//             com.birosoft.liquid.LiquidLookAndFeel.setStipples(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    private int spaceCount = 1;//空间个数
    private ButtonGroup bg1 = new ButtonGroup();
    private Map<JRadioButtonMenuItem, String> map = new HashMap<JRadioButtonMenuItem, String>();
    private JFileChooser jfc;
    private volatile boolean isDemo = true;
    public static String name;
    private int rest = 1;
    private ExecutorService es;
    private Properties pro;
    private RegInfo regInfo;
    private static Main main;

    /** Creates new form Main */
    public Main() {
        initComponents();
        setLocationRelativeTo(null);
        es = Executors.newCachedThreadPool();
        name = getID(Util.getMACAddress());
        System.out.println("name=" + name);
        checkReg();
        mainPanel.removeAll();
        if (rest <= 0) {
            jMenu1.setEnabled(false);
            jMenu2.setEnabled(false);
        } else {
            initOther();
        }
        setTitle(R.getTitle());
        main = this;
    }

    public static Main getInstance() {
        return main;
    }

    public void run() {
        //去网上检查此ID有没有过期，或者有没有列表中
        try {
            if(true){
                return;
            }
            String back = Util.readUrl(MessageFormat.format(R.getCheckRegURL(), name), "GBK");
            System.out.println("back="+back);
            //be7a917375bce75,422-11224-111da0921-adasdf,2008-07-11
            //此时应该是过期或者非法
            if (Util.isEmpty(back)) {
                JPanel panel = new RegIdPanel(MyBundle.getString("software.id.invalid"), MyBundle.getString("you.reg.id"), name);
                JOptionPane.showMessageDialog(this, panel);
                System.exit(0);
            } else {
                String[] ss = back.split(",");
                Date date = Util.getRemoteDate();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                if (ss[0].equals(name) && ss[1].equals(regInfo.getPassword())) {
                    Date expiredDate = sdf.parse(ss[2]);
                    if (expiredDate.before(date)) {
                        JPanel panel = new RegIdPanel(MyBundle.getString("software.expired"), MyBundle.getString("you.reg.id"), name);
                        JOptionPane.showMessageDialog(this, panel);
                        System.exit(0);
                    }
                } else {
                    JPanel panel = new RegIdPanel(MyBundle.getString("software.id.invalid"), MyBundle.getString("you.reg.id"), name);
                    JOptionPane.showMessageDialog(this, panel);
                    System.exit(0);
                }
            }

//            System.out.println("check online...");
//            List<RemoteRegInfo> list = Util.getRemoteRegInfoList();
//            Date date = Util.getRemoteDate();
//            System.out.println("remoteRegInfo.list =" + list);
//            System.out.println("remoteDate =" + date);
//            //只有非空的情况下才需要验证是否过时了
//            boolean find = false;
//            for (RemoteRegInfo info : list) {
//                if (info.getRegId().equals(name) && info.getRegPwd().equals(regInfo.getPassword())) {
//                    find = true;
//                    Date expiredDate = info.getExpiredDate();
//                    if (expiredDate.before(date)) {
//                        JPanel panel = new RegIdPanel(MyBundle.getString("software.expired"), MyBundle.getString("you.reg.id"), name);
//                        JOptionPane.showMessageDialog(this, panel);
//                        System.exit(0);
//                    }
//                }
//            }
//            if (find == false) {
//                JPanel panel = new RegIdPanel(MyBundle.getString("software.id.invalid"), MyBundle.getString("you.reg.id"), name);
//                JOptionPane.showMessageDialog(this, panel);
//                System.exit(0);
//            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    //检查是否注册

    private void checkReg() {
        try {
            regInfo = Util.getRegInfo();
            String pwd = regInfo.getPassword();
            System.out.println(pwd);
            if (pwd.equals(this.getPWD(name))) {
                isDemo = false;
                if (!Util.DEBUG) {
                    new Thread(this).start();
                }
            } else {
                rest = regInfo.getRestTime();
                System.out.println("还有:" + rest + "次！");
                if (rest <= 0) {
                    JOptionPane.showMessageDialog(Main.this, MyBundle.getString("try.over"));
                } else {
                    JOptionPane.showMessageDialog(Main.this, MyBundle.format("try.remain", rest));
                }
                new Thread(new Runnable() {

                    public void run() {
                        int i = 0;
                        while (true) {
                            try {
                                Thread.sleep(60000 + (int) (Math.random() * 60000));
                                if (!isDemo) {
                                    break;
                                }
                                es.execute(new Runnable() {

                                    public void run() {
                                        JOptionPane.showMessageDialog(Main.this, MyBundle.format("please.reg", rest));
                                    }
                                });

                            } catch (Exception exe) {
                                exe.printStackTrace();
                            }
                        }
                    }
                }).start();
            }
        } catch (Exception exe) {
            exe.printStackTrace();
            JOptionPane.showMessageDialog(this, MyBundle.getString("init.error"),
                    "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }

    private void initOther() {
        addWorkSpace();
    }

    private String getPWD(String name) {
        int hash = name.hashCode();
        int sum = 0, sum1 = 0, sum2 = 0;
        for (int i = 0; i < name.length(); i++) {
            sum += name.charAt(i);
        }
        sum &= 0x8FFFFFFF;
        sum1 = sum | 872392223;
        hash &= 0xabcdef;
        hash += 333;
        sum2 = (sum + sum1) % 999;
        String pwd = Integer.toHexString(sum) + "-" + sum2 + "-" + Integer.toHexString(hash) + "-" + Integer.toHexString(sum1);
        return pwd;
    }

    private String getID(String name) {
        System.out.println("MACNAME = " + name + ",name.length = " + name.length());
        int sum = 0;
        for (int i = 0; i < name.length(); i++) {
            sum += name.charAt(i);
        }
        sum ^= 123456789;
        return Integer.toHexString(name.hashCode() ^ 987654321) + Integer.toHexString(sum);
    }

    private void doReg() {
        final JDialog jd = new JDialog(this, MyBundle.getString("reg.dialog"), true);
        JPanel up = new JPanel(new GridLayout(2, 2, 5, 10));
        final JTextField id, pwd;
        JButton ok, cancel;
        up.add(new JLabel(MyBundle.getString("reg.id"), JLabel.CENTER));
        up.add(id = new JTextField(20));
        id.setEditable(false);
        id.setText(name);
        up.add(new JLabel(MyBundle.getString("reg.code"), JLabel.CENTER));
        up.add(pwd = new JTextField(20));
        id.addMouseListener(new ClipBoardListener());
        pwd.addMouseListener(new ClipBoardListener());
        up.setBorder(BorderFactory.createTitledBorder(MyBundle.getString("input.area")));
        JPanel down = new JPanel();
        down.add(ok = new JButton(MyBundle.getString("ok")));
        down.add(cancel = new JButton(MyBundle.getString("cancel")));
        ok.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                String psd = pwd.getText().trim();
                if (psd.equals(getPWD(name))) {
                    try {
                        RegInfo regInfo = new RegInfo();
                        regInfo.setPassword(psd);
                        Util.saveRegInfo(regInfo);
                        isDemo = false;
                    } catch (Exception exe) {
                        exe.printStackTrace();
                    }
                    jd.dispose();
                    JOptionPane.showMessageDialog(Main.this, MyBundle.getString("reg.succes"));
                    jMenu1.setEnabled(true);
                    jMenu2.setEnabled(true);
                    initOther();
                    SwingUtilities.updateComponentTreeUI(Main.this);
                } else {
                    JOptionPane.showMessageDialog(jd, MyBundle.getString("reg.code.error"),
                            "Error", JOptionPane.ERROR_MESSAGE);
                    pwd.setText(null);
                    pwd.requestFocusInWindow();
                }
            }
        });
        cancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                jd.dispose();
            }
        });
        Container cc = jd.getContentPane();
        cc.add(up, BorderLayout.NORTH);
        cc.add(down, BorderLayout.SOUTH);
        jd.pack();
        jd.setLocationRelativeTo(this);
        jd.setResizable(false);
        jd.setVisible(true);
        jd.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    private void buildLookAndFeelMenu(JMenu menu) {
        ButtonGroup bg = new ButtonGroup();
        LookAndFeelInfo[] ls = UIManager.getInstalledLookAndFeels();
        String current = UIManager.getLookAndFeel().getName();
        for (LookAndFeelInfo li : ls) {
            String name = li.getName();
            System.out.println("name=" + name);
            JRadioButtonMenuItem item = new JRadioButtonMenuItem(name, name.equals(current));
            menu.add(item);
            bg.add(item);
            item.addActionListener(this);
            map.put(item, li.getClassName());
        }
    }

    private JFileChooser getFileChooser(String desc, String... exts) {
        if (jfc == null) {
            jfc = new JFileChooser(".");
        }
        jfc.setFileFilter(new FileNameExtensionFilter(desc, exts));
        return jfc;
    }

    public void removeWorkPanel(WorkPanel panel) {
        mainPanel.remove(panel);
        jMenu2.remove(panel.getMenuItem());
        SwingUtilities.updateComponentTreeUI(this);
    }

    private WorkPanel addWorkSpace() {
        mainPanel.removeAll();
        String temp = MyBundle.getString("workspace") + (spaceCount++);
        JRadioButtonMenuItem jrb = new JRadioButtonMenuItem(temp);
        final WorkPanel panel = new WorkPanel(this, temp, jrb);
        bg1.add(jrb);
        jrb.setSelected(true);
        jrb.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                mainPanel.removeAll();
                mainPanel.add(panel, BorderLayout.CENTER);
                SwingUtilities.updateComponentTreeUI(mainPanel);
            }
        });
        jMenu2.add(jrb);
        mainPanel.add(panel, BorderLayout.CENTER);
        SwingUtilities.updateComponentTreeUI(this);
        return panel;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenu5 = new javax.swing.JMenu();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("清伍seo优化软件 手工优化王 V 5.0 联系QQ：195042644 ");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        mainPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        mainPanel.setPreferredSize(new java.awt.Dimension(896, 527));
        mainPanel.setLayout(new java.awt.BorderLayout());

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/logo.png"))); // NOI18N

        jMenu1.setMnemonic('F');
        jMenu1.setText(MyBundle.getString("file")); // NOI18N

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem1.setText(MyBundle.getString("new.workspace")); // NOI18N
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem4.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem4.setText(MyBundle.getString("open.workspace")); // NOI18N
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem4);

        jMenuItem2.setText(MyBundle.getString("menu.about")); // NOI18N
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuItem3.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.ALT_MASK));
        jMenuItem3.setText(MyBundle.getString("exit")); // NOI18N
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem3);

        jMenuBar1.add(jMenu1);

        jMenu2.setMnemonic('W');
        jMenu2.setText(MyBundle.getString("window")); // NOI18N
        jMenuBar1.add(jMenu2);

        jMenu4.setMnemonic('R');
        jMenu4.setText(MyBundle.getString("menu.reg")); // NOI18N

        jMenuItem5.setText(MyBundle.getString("menu.input.reg")); // NOI18N
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem5);

        jMenuBar1.add(jMenu4);

        jMenu5.setMnemonic('C');
        jMenu5.setText(MyBundle.getString("contact.us")); // NOI18N

        jMenuItem6.setText(MyBundle.getString("view.my.web")); // NOI18N
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem6);

        jMenuItem7.setText(MyBundle.getString("link.to.author")); // NOI18N
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem7);

        jMenuBar1.add(jMenu5);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 896, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 527, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void confirmClose() {
        int i = JOptionPane.showConfirmDialog(this, MyBundle.getString("confirm.exit"),
                MyBundle.getString("confirm"),
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (i == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        confirmClose();
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        confirmClose();
    }//GEN-LAST:event_formWindowClosing

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        addWorkSpace();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
//        JOptionPane.showMessageDialog(this, MyBundle.getString("about"));
        JOptionPane.showMessageDialog(this, R.getContactInfo());
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        JFileChooser temp = getFileChooser(MyBundle.getString("workspace.file"), "wks");
        int i = temp.showOpenDialog(this);
        if (i == JFileChooser.APPROVE_OPTION) {
            File file = temp.getSelectedFile();
            if (file != null) {
                WorkSpace workSpace = Util.readWorkSpace(file);
                if (workSpace != null) {
                    workSpace.setFromFile(file);
                    WorkPanel panel = addWorkSpace();
                    panel.loadFromWorkSpace(workSpace);
                } else {
                    JOptionPane.showMessageDialog(this, MyBundle.getString("invalid.workspace.file"));
                }
            }
        }
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        if (!isDemo) {
            JOptionPane.showMessageDialog(this, MyBundle.getString("already.reged"));
        } else {
            doReg();
        }
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        try {
            Desktop.getDesktop().browse(new URI(R.getContactURL()));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        JOptionPane.showMessageDialog(this, R.getContactInfo());
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new Main().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JPanel mainPanel;
    // End of variables declaration//GEN-END:variables

    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        if (obj instanceof JRadioButtonMenuItem) {
            try {
                UIManager.setLookAndFeel(map.get((JRadioButtonMenuItem) obj));
                SwingUtilities.updateComponentTreeUI(this);
            } catch (Exception exe) {
                exe.printStackTrace();
            }
        }
    }
}
