/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hadeslee.webpage.util;

import com.google.api.translate.Language;
import com.google.api.translate.Translate;
import com.hadeslee.webpage.data.WorkSpace;
import com.hadeslee.webpage.ui.panel.FindDialog;
import com.hadeslee.webpage.ui.panel.TextAreaDialog;
import com.hadeslee.webpage.util.ReplaceInfo.ReplaceContent;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.text.JTextComponent;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

/**
 * ������
 *
 * @author Hadeslee
 */
public final class Util {

    private static final int FILE_NAME_MAX_LENGTH = 10;
    private static String searckKeywordURL = null;
    private static String REPLACE_DATA_FILE = "replace.dat";
    private static String currentFriendLinkContent;//��ǰ�������ӵ����ݣ�һ��ʼ�Ƕ��ģ������ǿ��Ա༭��
    public static final ClipBoardListener CLIPBOARD_LISTENER = new ClipBoardListener();
    public static volatile boolean DEBUG = false;
    private static List<MyLink> myLinkList;
    private static int currentIndex;

    public static String matcher(String input, String regex) {
        return matcher(input, 0, regex, Pattern.DOTALL | Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
    }

    /**
     * windows 7 ר�� ��ȡMAC��ַ
     *
     * @return
     * @throws Exception
     */
    private static String getWin7MACAddress() throws Exception {

        // ��ȡ����IP����
        InetAddress ia = InetAddress.getLocalHost();
        // �������ӿڶ��󣨼������������õ�mac��ַ��mac��ַ������һ��byte�����С�
        byte[] mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();

        // ��������ǰ�mac��ַƴװ��String
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < mac.length; i++) {
            if (i != 0) {
                sb.append("-");
            }
            // mac[i] & 0xFF ��Ϊ�˰�byteת��Ϊ������
            String s = Integer.toHexString(mac[i] & 0xFF);
            sb.append(s.length() == 1 ? 0 + s : s);
        }

        // ���ַ�������Сд��ĸ��Ϊ��д��Ϊ�����mac��ַ������
        return sb.toString().toUpperCase();
    }

    public static String matcher(String input, int group, String regex, int flags) {
        Pattern p = Pattern.compile(regex, flags);
        Matcher m = p.matcher(input);
        if (m.find()) {
            return m.group(group);
        }
        return null;
    }

    public static List<MyLink> getNextLink() {
        getMyLinkList();
        int zhanqunCount = Setting.getInstance().getZhanqunCount();
        if (zhanqunCount > 0) {
            List<MyLink> list = new ArrayList<MyLink>();
            while (zhanqunCount-- > 0) {
                if (currentIndex < myLinkList.size()) {
                } else {
                    currentIndex = 0;
                }
                list.add(myLinkList.get(currentIndex));
                currentIndex++;
            }
            return list;

        } else {
            return Collections.emptyList();
        }
    }

    private static List<MyLink> getMyLinkList() {
        if (myLinkList == null) {
            myLinkList = new ArrayList<MyLink>();
            String content = readUrl(R.getZhanQunURL(), "GBK");
            content = content.trim();
            String[] ss = content.split("\\|");
            for (String s : ss) {
                String[] temp = s.split(",");
                myLinkList.add(new MyLink(temp[0], temp[1]));
            }
        }
        return myLinkList;
    }

    public static class MyLink {

        private String href;
        private String name;

        public MyLink(String href, String name) {
            this.href = href;
            this.name = name;
        }

        public String toString() {
            return "<a href='" + href + "'>" + name + "</a>";
        }

        public String getHref() {
            return href;
        }

        public void setHref(String href) {
            this.href = href;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    /**
     * Ϊĳ��������������г�Ա������ֻҪ�����Ա������ JTextComponent�Լ��������࣬��Ϊ��������������
     *
     * @param parent
     */
    public static void addClipboardListener(Object parent) {
        try {
            Field[] fs = parent.getClass().getDeclaredFields();
            for (Field f : fs) {
                f.setAccessible(true);
                Object obj = f.get(parent);
                if (obj instanceof JTextComponent) {
                    JTextComponent comp = (JTextComponent) obj;
                    comp.addMouseListener(CLIPBOARD_LISTENER);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static String getCurrentFriendLinkContent() {
        if (currentFriendLinkContent == null) {
            currentFriendLinkContent = getFriendLinkContent();
        }
        return currentFriendLinkContent;
    }

    public static void setCurrentFriendLinkContent(String currentFriendLinkContent) {
        Util.currentFriendLinkContent = currentFriendLinkContent;
    }

    public static ReplaceInfo readReplaceInfo() {
        File f = new File(REPLACE_DATA_FILE);
        ReplaceInfo info = null;
        if (f.exists()) {
            ObjectInputStream ois = null;
            try {
                ois = new ObjectInputStream(new FileInputStream(f));
                info = (ReplaceInfo) ois.readUnshared();
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                close(ois);
            }
        }
        if (info == null) {
            info = new ReplaceInfo();
            saveReplaceInfo(info);
        }
        return info;
    }

    public static void saveReplaceInfo(ReplaceInfo info) {
        File f = new File(REPLACE_DATA_FILE);
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(new FileOutputStream(f));
            oos.writeUnshared(info);
            oos.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            close(oos);
        }
    }

    public static String getMACAddress() {
        String windir = System.getProperty("user.home");
        File file = new File(windir, ".wpo");
        file.getParentFile().mkdirs();
        if (file.exists()) {
            return readFile(file, "UTF-8");
        } else {
            String address = System.getProperty("user.name") + System.currentTimeMillis();
            String os = System.getProperty("os.name");
            if (os != null && os.startsWith("Windows")) {
                try {
                    if ("Windows 7".contains(os)) {
                        address = getWin7MACAddress();
                    } else {
                        String command = "cmd.exe /c ipconfig /all";
                        Process p = Runtime.getRuntime().exec(command);
                        BufferedReader br
                                = new BufferedReader(
                                        new InputStreamReader(p.getInputStream()));
                        String line;
                        while ((line = br.readLine()) != null) {
                            if (line.indexOf("Physical Address") > 0) {
                                int index = line.indexOf(":");
                                index += 2;
                                address = line.substring(index);
                                break;
                            }
                        }
                        br.close();
                    }
                } catch (Exception e) {
                }
            }
            address = address.trim();
            saveFile(address, "UTF-8", file);
            return address;
        }
    }

    /**
     * ΪJTextArea���Ctrl+F�Ĳ��ҹ���
     *
     * @param jta Ҫ��ӵ�JTA
     */
    public static void addFindForTextArea(final Frame parent, final JTextArea jta) {
        KeyStroke key = KeyStroke.getKeyStroke(KeyEvent.VK_F, KeyEvent.CTRL_DOWN_MASK, true);
        String id = "_FIND_";
        Action action = new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                FindDialog d = new FindDialog(parent, jta);
                d.setLocationRelativeTo(parent);
                d.setVisible(true);
            }
        };
        jta.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(key, id);
        jta.getActionMap().put(id, action);
    }

    public static String getTextAreaContent(Frame frame, String initContent) {
        TextAreaDialog t = new TextAreaDialog(frame, true, initContent);
        t.setLocationRelativeTo(frame);
        t.setVisible(true);
        return t.getContent();
    }

    /**
     * �������Ժ�������һЩ���������һЩ���滻�� ������һЩ���ݵ��޲�����
     *
     * @return
     */
    public static String parseContent(ReplaceInfo info, String content) {
        try {
            List<ReplaceContent> list = info.getReplaceContents();
            for (ReplaceContent rc : list) {
                String source = rc.getKey();
                String target = rc.getValue();
                if (!Util.isEmpty(rc.getLink())) {
                    target = "<a href=\"" + rc.getLink() + "\">" + target + "</a>";
                }
                content = content.replace(source, target);
            }
            int preCount = info.getHeadTrimCount();
            int postCount = info.getTailTrimCount();
            //ֻ�����߼�����С�����ݵĴ��ڲ������壬������ǿյ���
            if (preCount + postCount < content.length()) {
                StringBuilder sb = new StringBuilder(content);
                sb.delete(0, preCount);
                sb.delete(sb.length() - postCount, sb.length());
                content = sb.toString();
            } else {
                content = "";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return content;
    }

    /**
     * �滻�б�ģ���title����
     *
     * @param source Դ����
     * @return �µ�����
     */
    public static String replaceListTemplateTitle(String source) {
        Matcher m = Pattern.compile("<title>(.*?)</title>", Pattern.DOTALL | Pattern.CASE_INSENSITIVE).matcher(source);
        if (m.find()) {
            return m.replaceAll("<title>{�ؼ���1}|{�ؼ���2}|{�ؼ���3}|{�ؼ���4}|{�ؼ���5}|{�ؼ���6}|{�ؼ���7}|{�ؼ���8}</title>\n"
                    + "<META content={�ؼ���1}��{�ؼ���2}��{�ؼ���3}��{�ؼ���4}��{�ؼ���5}��{�ؼ���6}��{�ؼ���7}��{�ؼ���8} name=keywords>\n"
                    + "<META content={�ؼ���1}��{�ؼ���2}��{�ؼ���3}��{�ؼ���4}��{�ؼ���5}��{�ؼ���6}��{�ؼ���7}��{�ؼ���8} name=description>\n");
        }
        return source;
    }

    /**
     * �滻��ģ���б���������ӣ�������ɣ� 1. ѡ���б�ҳ����1 �滻�� {���±���1} ���ҽ� ���� href=�� �ɵġ� �ĳ�
     * href=��{��������1}�� 1. ѡ���б�ҳ����2 �滻�� {���±���2} ���ҽ� ���� href=�� �ɵġ� �ĳ�
     * href=��{��������2}�� 1. ѡ���б�ҳ����3 �滻�� {���±���3} ���ҽ� ���� href=�� �ɵġ� �ĳ�
     * href=��{��������3}�� ���������� 1. ѡ���б�ҳ����10 �滻�� {���±���10} ���ҽ� ���� href=�� �ɵġ� �ĳ�
     * href=��{��������10}�� ������������ʽ�� 1��������"�� 2������Ϊ'�� 3�����趼û��
     * 1,(<a(?:[^>])+?href=(?:\s)*\")(.+?)(\"[^>]*?>)(\s*content1\s*)(</a>)
     * 2,(<a(?:[^>])+?href=(?:\s)*\')(.+?)(\'[^>]*?>)(\s*content1\s*)(</a>)
     * 3,(<a(?:[^>])+?href=(?:\s)*)(.+?)(\s+[^>]*?>)(\s*content1\s*)(</a>)
     *
     * @param source Դ����
     * @param aName ���ӵ�����<a href="test.htm">����</a>
     * @param index ��������1��ʼ,����{���±���1},{���±���2}...
     * @return �滻�õ�����
     */
    public static String replaceLinkOfListTemplate(String source, String aName, int index) {
        if (Util.isEmpty(aName)) {
            return source;
        }
        Matcher m = Pattern.compile("<a[^>]*>" + aName + "</a>", Pattern.DOTALL | Pattern.CASE_INSENSITIVE).matcher(source);
        if (m.find()) {
            aName = m.group();
        }
        source = source.replace(aName, "<a href=\"{��������" + index + "}\" target=\"_blank\">{���±���" + index + "}</a>");
        return source;
    }

    /**
     * ���ʵ�����£� href= ��������� http:// �򲻴��� src= ��������� http:// �򲻴��� ��ҳ��� src=�� src=
     * �滻�� src=��../ {��Ϊ��һЩ�������д���� д src= ��������} ��ҳ��� href=�� href= �滻�� href=��../
     * {��Ϊ��һЩ�������д���� д src= ��������} ��ҳ��� src=�� src= �滻�� src=��../ {��Ϊ��һЩ�������д���� д
     * src= ��������} ��ҳ��� background=" background= �滻��background="../
     * background=../ {��Ϊ��һЩ�������д���� д src= ��������}\ TODO Ŀǰû��ʵ�֣������Ժ�ʵ��
     *
     * @param content
     * @return
     */
    public static String replaceSrc_href(String content) {
        content = replaceUrlWithPattern(content, "href\\s*=\\s*\"(.*?)\"", 1, "href=\"../{_url_}\"");
        content = replaceUrlWithPattern(content, "href\\s*=\\s*\'(.*?)\'", 1, "href=\'../{_url_}\'");
        content = replaceUrlWithPattern(content, "href=(?:\\s)*([^>\"]+?)(?=[\\s>])", 1, "href=\"../{_url_}\" ");
        content = replaceUrlWithPattern(content, "src\\s*=\\s*\"(.*?)\"", 1, "src=\"../{_url_}\"");
        content = replaceUrlWithPattern(content, "src\\s*=\\s*\'(.*?)\'", 1, "src=\'../{_url_}\'");
        content = replaceUrlWithPattern(content, "src=(?:\\s)*([^>\"]+?)(?=[\\s>])", 1, "src=\"../{_url_}\" ");
        content = replaceUrlWithPattern(content, "background\\s*=\\s*\"(.*?)\"", 1, "background=\"../{_url_}\"");
        content = replaceUrlWithPattern(content, "background\\s*=\\s*\'(.*?)\'", 1, "background=\'../{_url_}\'");
        content = replaceUrlWithPattern(content, "background=(?:\\s)*([^>\"]+?)(?=[\\s>])", 1, "background=\"../{_url_}\" ");
        return content;
    }

    private static String replaceUrlWithPattern(String content, String pattern, int urlIndex, String urlPattern) {
        Map<String, String> map = new HashMap<String, String>();
        Matcher m = Pattern.compile(pattern, Pattern.DOTALL | Pattern.CASE_INSENSITIVE).matcher(content);
        while (m.find()) {
            String url = m.group(urlIndex);
            //�����߶��Ǿ��Ե�ַ
            if (!url.toLowerCase().startsWith("http://") && !url.toLowerCase().startsWith("/")) {
                map.put(m.group(), urlPattern.replace("{_url_}", url));
            }
        }
        for (Entry<String, String> entry : map.entrySet()) {
            content = content.replace(entry.getKey(), entry.getValue());
        }
        return content;
    }

    public static boolean saveRegInfo(RegInfo regInfo) {
        String windir = System.getProperty("user.home");
        File regFile = new File(windir, "hadeslee.dat");
        regFile.getParentFile().mkdirs();
        return Util.saveObject(regInfo, regFile);
    }

    /**
     * ��ȡע����Ϣ���������ע���ļ����򷵻�֮������ �½�һ�������������ٷ���
     *
     * @return
     */
    public static RegInfo getRegInfo() {
        String windir = System.getProperty("user.home");
        File regFile = new File(windir, "hadeslee.dat");
        regFile.getParentFile().mkdirs();
        RegInfo regInfo = null;
        //������ھͱ���Ҫ�ܶ�����
        if (regFile.exists()) {
            ObjectInputStream ois = null;
            try {
                ois = new ObjectInputStream(new FileInputStream(regFile));
                regInfo = (RegInfo) ois.readUnshared();
            } catch (Exception ex) {
                ex.printStackTrace();
                regInfo = new RegInfo();
            } finally {
                close(ois);
            }
        } else {
            regInfo = new RegInfo();
            ObjectOutputStream oos = null;
            try {
                oos = new ObjectOutputStream(new FileOutputStream(regFile));
                oos.writeUnshared(regInfo);
                oos.flush();
                regInfo.setRestTime(0);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "����ϵͳ�������д����������");
            } finally {
                close(oos);
            }
        }

        return regInfo;
    }

    public static boolean saveObject(Serializable obj, File file) {
        ObjectOutputStream oos = null;
        try {
            file.getParentFile().mkdirs();
            oos = new ObjectOutputStream(new FileOutputStream(file));
            oos.writeUnshared(obj);
            oos.flush();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        } finally {
            close(oos);
        }
    }

    public static Date getRemoteDate() {
        try {
            String content = readUrl(R.getDateURL(), "UTF-8");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.parse(content.replace("&nbsp;", "").trim());
        } catch (Exception ex) {
            ex.printStackTrace();
            return new Date();
        }
    }

    public static List<RemoteRegInfo> getRemoteRegInfoList() {
        List<RemoteRegInfo> list = new ArrayList<RemoteRegInfo>();
        try {
            String content = readUrl(R.getRegURL(), "GBK");
            BufferedReader br = new BufferedReader(new StringReader(content));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false);
            String temp = null;
            while ((temp = br.readLine()) != null) {
                String[] ss = temp.split(",");
                if (ss.length == 3) {
                    String regId = ss[0];
                    String regPwd = ss[1];
                    Date expiredDate = sdf.parse(ss[2]);
                    list.add(new RemoteRegInfo(regId, regPwd, expiredDate));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    public static String getSearchKeywordURL() {
        if (searckKeywordURL == null) {
            try {
                String s = readUrl(R.getDiZhiURL(), "GBK");
                BufferedReader br = new BufferedReader(new StringReader(s));
                s = br.readLine();
                searckKeywordURL = s + "?qstr={0}";
            } catch (Exception ex) {
                ex.printStackTrace();
                searckKeywordURL = "http://search.75pp.com/1234567.php?qstr={0}";
            }
        }
        return searckKeywordURL;

    }

    /**
     * ����title����ĳ��Ŀ¼�����Ψһ���֣� ���������ֲ���̫��
     *
     * @param title ����
     * @param dir ���ĸ�Ŀ¼��
     * @return ��Ŀ¼��Ψһ�����֣�����ͱ�����ֳ�ͻ
     */
    public static String generateUniqueName(String title, String ext, File dir) {
        String pinyin = Util.getPinyin(title);
        if (pinyin.length() + 2 > FILE_NAME_MAX_LENGTH) {
            pinyin = pinyin.substring(0, FILE_NAME_MAX_LENGTH - 2);
        }
//        int index = 0;
//        while (new File(dir, pinyin + index + "." + ext).exists()) {
//            index++;
//        }
//        pinyin = pinyin + index;
        return pinyin + "." + ext;
    }

    public static WorkSpace readWorkSpace(File file) {
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new FileInputStream(file));
            return (WorkSpace) ois.readUnshared();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        } finally {
            close(ois);
        }
    }

    public static boolean saveWorkSpace(WorkSpace workSpace, File file) {
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(new FileOutputStream(file));
            oos.writeUnshared(workSpace);
            oos.flush();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        } finally {
            Util.close(oos);
        }
    }

    /**
     * ���ַ�����������ĳ�ֱ����ʽ����ĳ���ļ�����
     *
     * @param content Ҫ���������
     * @param encoding �����ʽ
     * @param file Ҫд����ļ�
     */
    public static boolean saveFile(String content, String encoding, File file) {
        content = content.replace("\r\n", "\n");
        content = content.replace("\n", "\r\n");
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), encoding));
            bw.write(content);
            bw.flush();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        } finally {
            Util.close(bw);
        }
    }

    /**
     * �ѹؼ��ֲ��뵽�������ĵ�����ȥ�������ǰ�ʹ��ڣ���ɾ����ǰ������
     *
     * @param originalContent ԭʼ�ĵ�
     * @param keywords �ؼ����б�
     * @param setting ���ã����ܸ����������ɲ�ͬ��ҳ��
     * @return �µ�����
     */
    public static String insertContent(String originalContent, List<String> keywords, Setting setting) {
        originalContent = replaceBodyKeywords(originalContent, keywords);
        originalContent = replaceTitle(originalContent, getTitle(keywords));
        originalContent = replaceMetaDescription(originalContent, getDescription(keywords));
        originalContent = replaceMetaKeywords(originalContent, getKeywords(keywords));
        originalContent = replaceNavigation(originalContent, getNavigation(keywords));
        if (setting.isInsertFriendLink()) {
            originalContent = insertFriendLinkContent(originalContent);
        }
        originalContent = insertCenterH(setting, originalContent, getCenterH(setting.getHn(), keywords));
        return originalContent;
    }

    private static String insertCenterH(Setting setting, String originalContent, String centerH) {
        StringBuilder sb = new StringBuilder(originalContent);
        if (setting.isHideKeyWord()) {
            centerH = "<div style=\"display:none;\">" + centerH + "</div>";
        }
        if (setting.getKeywordPlace() == Setting.KEYWORD_TOP) {
            Pattern pattern = Pattern.compile("<body[^>]*>", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
            Matcher m = pattern.matcher(originalContent);
            if (m.find()) {
                String back = m.replaceAll("$0\r\n" + centerH + "\r\n");
                return back;
            }
        } else {
            int index = sb.lastIndexOf("</body>");
            if (index != -1) {
                sb.insert(index, centerH);
            }
        }
        return sb.toString();
    }

    private static String insertFriendLinkContent(String originalContent) {
        StringBuilder sb = new StringBuilder(originalContent);
        int index = sb.lastIndexOf("</body>");
        if (index != -1) {
            sb.insert(index, getCurrentFriendLinkContent());
        }
        return sb.toString();
    }

    /**
     * ��body������ֹ��ģ��ڹؼ����б�����Ĺؼ��� ȫ������<strong>�ؼ���</strong>�ı�ǩ Ƕ��Ҳû�й�ϵ
     *
     * @param originalContent
     * @param keywords
     * @return
     */
    private static String replaceBodyKeywords(String originalContent, List<String> keywords) {
        for (String s : keywords) {
            originalContent = originalContent.replace(s, "<strong>" + s + "</strong>");
        }
        return originalContent;
    }

    /**
     * �滻����title,���ԭ���еĻ������ԭ��û�У���ֱ�Ӳ���title�� head���棬���headҲû�У��Ǿ���headһ�����
     *
     * @param originalContent ԭ�����ĵ�����
     * @param newTitle �µ�title������title��ǩ
     * @return �µ�����
     */
    private static String replaceTitle(String originalContent, String newTitle) {
        Matcher m = Pattern.compile("<title>(.*?)</title>", Pattern.CASE_INSENSITIVE | Pattern.DOTALL).matcher(originalContent);
        if (m.find()) {
            System.out.println("find");
            originalContent = m.replaceFirst(newTitle + "\r\n");
        } else {
            System.out.println("not find");
            m = Pattern.compile("(?<=<head>)(.*?)(?=</head>)", Pattern.CASE_INSENSITIVE | Pattern.DOTALL).matcher(originalContent);
            if (m.find()) {
                originalContent = m.replaceFirst(newTitle + "\r\n$1");
            } else {
                m = Pattern.compile("<body[^>]*>(.*?)</body>", Pattern.CASE_INSENSITIVE | Pattern.DOTALL).matcher(originalContent);
                if (m.find()) {
                    originalContent = m.replaceFirst("<head>" + newTitle + "</head>\r\n$0\r\n");
                }
            }
        }
        return originalContent;
    }

    /**
     * �滻ԭ��meta�����description������еĻ������û�У������� ����һ���µļ�¼��head����ȥ
     *
     * @param originalContent ԭ������ҳ����
     * @param meta Ҫ�����meta����
     * @return �µ���ҳ����
     */
    private static String replaceMetaDescription(String originalContent, String meta) {
        Matcher m = Pattern.compile("<META content\\=(.*?)name\\=description>", Pattern.CASE_INSENSITIVE | Pattern.DOTALL).matcher(originalContent);
        if (m.find()) {
            originalContent = m.replaceFirst(meta);
        } else {
            //���ʱ��϶���title�ˣ���Ϊ��һ�����Ǵ���title��
            m = Pattern.compile("<title>(.*?)</title>", Pattern.CASE_INSENSITIVE | Pattern.DOTALL).matcher(originalContent);
            if (m.find()) {
                originalContent = m.replaceAll("$0\r\n" + meta + "\r\n");
            }
        }
        return originalContent;
    }

    /**
     * �滻ԭ��meta�����keywords������еĻ������û�У������� ����һ���µļ�¼��head����ȥ
     *
     * @param originalContent ԭ������ҳ����
     * @param meta Ҫ�����meta����
     * @return �µ���ҳ����
     */
    private static String replaceMetaKeywords(String originalContent, String meta) {
        Matcher m = Pattern.compile("<META content\\=(.*?)name\\=keywords>", Pattern.CASE_INSENSITIVE | Pattern.DOTALL).matcher(originalContent);
        if (m.find()) {
            originalContent = m.replaceAll("\r\n" + meta + "\r\n<META content=" + R.getAuthorMeta() + " name=Author>\r\n");
        } else {
            //���ʱ��϶���title�ˣ���Ϊ��һ�����Ǵ���title��
            m = Pattern.compile("<title>(.*?)</title>", Pattern.CASE_INSENSITIVE | Pattern.DOTALL).matcher(originalContent);
            if (m.find()) {
                originalContent = m.replaceAll("$0\r\n" + meta + "\r\n<META content=" + R.getAuthorMeta() + " name=Author>\r\n");
            }
        }
        return originalContent;
    }

    /**
     * ���м���Ǹ��������ֲ��뵽body����ȥ��û���滻�Ĺ��� ֻ�ǲ����µĵ�body����ǰ�棬���center֮�ڵĶ�����һ��DIV��ס
     * ���ṩid�Ļ����ǾͿ����ҵ����滻�ˡ�
     *
     * @param originalContent ԭ�����ĵ�����
     * @param navi Ҫ����ĵ�������
     * @return �µ�����
     */
    private static String replaceNavigation(String originalContent, String navi) {
        Matcher m = Pattern.compile("(<body[^>]*>)(.*?)(</body>)", Pattern.CASE_INSENSITIVE | Pattern.DOTALL).matcher(originalContent);
        if (m.find()) {
            originalContent = m.replaceFirst("\r\n$1" + navi + "\r\n$2$3\r\n");
        }
        return originalContent;
    }

    /**
     * ����һ���б�Ĺؼ��֣��õ�һ��title������
     *
     * @param keywords �ؼ����б�
     * @return title����
     */
    public static String getTitle(List<String> keywords) {
        StringBuilder sb = new StringBuilder();
        sb.append("<title>").append(getKeywordsByPipe(keywords));
        sb.append("</title>");
        return sb.toString();
    }

    /**
     * �õ�META�����description����
     *
     * @param keywords �ؼ���
     * @return ����
     */
    public static String getDescription(List<String> keywords) {
        StringBuilder sb = new StringBuilder();
        sb.append("<META content=").append(getKeywordsByComma(keywords));
        sb.append(" name=description>");
        return sb.toString();
    }

    /**
     * �õ�META�����keyword����
     *
     * @param keywords �ؼ���
     * @return ����
     */
    public static String getKeywords(List<String> keywords) {
        StringBuilder sb = new StringBuilder();
        sb.append("<META content=").append(getKeywordsByComma(keywords));
        sb.append(" name=keywords>");
        return sb.toString();
    }

    /**
     * ���ص���������
     *
     * @param keywords �ؼ���
     * @return ����
     */
    public static String getNavigation(List<String> keywords) {
        StringBuilder sb = new StringBuilder();
        sb.append("<center>\r\n");
        sb.append("<table width=\"990\" border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\">\r\n");
        sb.append("  <tr>\r\n");
        sb.append("    <td><SPAN class=style11> <a href=\"/\">��վ��ͼ��</a> ");
        for (String s : keywords) {
            sb.append("<a href=\"").append(getPinyin(s)).
                    append(".htm\"><strong>");
            sb.append(s).append("</strong></a> ");
        }
        sb.append("</SPAN></td>\r\n");
        sb.append("  </tr>\r\n");
        sb.append("</table>\r\n");
        sb.append("</center>\r\n");

        return sb.toString();
    }

    /**
     * ����<center><h1>keywors</h1></center> ֮������ݣ�����hn����h1,h2,h3֮��Ŀ��Ըı������
     *
     * @param hn H��
     * @param keywords �ؼ����б�
     * @return ����
     */
    public static String getCenterH(String hn, List<String> keywords) {
        StringBuilder temp = new StringBuilder();
        temp.append("<center>");
        temp.append(getKeyWordsByHnAndPepe(hn, keywords));
        temp.append("</center>");
        return temp.toString();
    }

    private static String getKeyWordsByHnAndPepe(String hn, List<String> keywords) {
        StringBuilder sb = new StringBuilder();
        for (String s : keywords) {
            sb.append("<").append(hn).append(">");
            sb.append(s).append("</").append(hn).append(">");
            sb.append("|");
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    /**
     * ����һ��keywords���ַ����� ����|���ŷָ�����˳��� list������һ����
     *
     * @param keywords һ���б�Ĺؼ���
     * @return һ���µ��ַ���
     */
    public static String getKeywordsByPipe(List<String> keywords) {
        StringBuilder sb = new StringBuilder();
        for (String s : keywords) {
            sb.append(s).append("|");
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    /**
     * ����һ����,�ָ��Ĺؼ����б���ַ��������Ҵ��ַ����ʹ������� list���෴��˳��ġ�
     *
     * @param keywords һ���ؼ��ֵ��б�
     * @return һ���µ��ַ���
     */
    public static String getKeywordsByComma(List<String> keywords) {
        StringBuilder sb = new StringBuilder();
        List<String> temp = new ArrayList<String>(keywords);
        Collections.reverse(temp);
        for (String s : temp) {
            sb.append(s).append(",");
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    public static String getPinyin(String text) {
        return PinyinUtil.String2Alpha(text);
    }

    /**
     * �������ã���ȡ�����������������,Ҳ����ʲôҲ������
     *
     * @return ����������
     */
    private static String getFriendLinkContent() {
        return readUrl(R.getLinkURL(), "GBK");
    }

    public static List<String> searchKeyWords(Setting setting, String searchContent) {
        List<String> list = Util.searchKeyWords(searchContent);
        if (setting.getSortType() != Setting.MO_REN) {
            Collections.sort(list, setting.getSorter());
        }
        int count = 0;
        List<String> tempList = new ArrayList<String>();
        for (int i = 0; i < list.size(); i++) {
            String temp = list.get(i);
            tempList.add(temp);
            count++;
            if (count >= setting.getKeywordCount()) {
                break;
            }
        }
        return tempList;
    }

    /**
     * ���ݴ����������ݣ���http://search.75pp.com/1234567.php?qstr={0} ��վ�ϲ�ѯ�ؼ���
     *
     * @param searchContent Ҫ��ѯ������
     * @return �ؼ����б�
     */
    public static List<String> searchKeyWords(String searchContent) {
        List<String> list = new ArrayList<String>();
        try {
            String url = MessageFormat.format(getSearchKeywordURL(), URLEncoder.encode(searchContent, "gb2312"));
            System.out.println(url);
            String content = readUrl(url, "gb2312");
            Matcher m = Pattern.compile("(?<=<tr><td class=font_9pt align=right width=50>\\d{1,3}.&nbsp;</td><td class=font_9pt align=left width=150>&nbsp;)(.*?)(?=</td><td class=font_9pt align=left width=180><img src=http://www2.baidu.com/images/bar.gif height=\\d+ width=\\d+></td></tr>)").matcher(content);
            while (m.find()) {
                String s = m.group();
                System.out.println(s);
                list.add(s);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    /**
     * ����ĳ��URL��������������
     *
     * @param url URL��ַ
     * @return ��ҳ�������
     */
    public static String readUrl(String url, String encoding) {
        StringBuilder sb = new StringBuilder();
        try {
            HttpClient httpClient = new DefaultHttpClient();
            httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 30000);
            httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 120000);
            HttpGet get = new HttpGet(url);
            get.setHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.1.3) Gecko/20090824 Firefox/3.5.3 GTB5");
            HttpEntity entity = httpClient.execute(get).getEntity();
            sb.append(EntityUtils.toString(entity, encoding));
            httpClient.getConnectionManager().shutdown();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return sb.toString().replace("\r\n", "\n");
    }

    /**
     * ��ȡĳ���ļ���������ݣ���ָ���ı����ʽ�� �˷�����Զ���᷵��null����������Ҳ�� ���ؿ��ַ���
     *
     * @param file Ҫ��ȡ���ļ�
     * @param encoding �Ժ��ֱ����ȡ
     * @return �ļ����ݵ��ַ�����ʽ
     */
    public static String readFile(File file, String encoding) {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(file), encoding));
            char[] buffer = new char[512];
            int length = -1;
            while ((length = br.read(buffer)) != -1) {
                sb.append(buffer, 0, length);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            close(br);
        }
        return sb.toString();
    }

    /**
     * �ر�ĳ��ʵ����Closeable�ӿڵĶ���,Ŀǰ java.io������󲿷��඼ʵ��������ӿڣ��˷���
     * �����׳��κ��쳣������Ҳ��Բ�������null��� ���Դ�null���������ǰ�ȫ��
     *
     * @param closeabe Ҫ�رյĶ���
     */
    public static void close(Closeable closeabe) {
        try {
            if (closeabe != null) {
                closeabe.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * �ж�һ���ַ����Ƿ���һ���յ��ַ���
     *
     * @param content ���ж��ַ�������
     * @return �Ƿ�һ�����ַ���
     */
    public static boolean isEmpty(String content) {
        if (content == null || content.trim().length() == 0) {
            return true;
        } else {
            return false;
        }
    }

    public static List<String> fetchUrl(String site, String keyword) {
        List<String> list = new ArrayList<String>();
        try {
            //���ĵİٶȣ���Ȼ�仯��ҳ�����ݸ�ʽ����Ȼֻ�Ƕ�һ���ո�ҳ�ѣ�FUCK
            String url = "http://www.baidu.com/s?wd=" + URLEncoder.encode("site:" + site + " " + keyword, "gb2312");
            System.out.println("url =" + url);
            String content = Util.readUrl(url, "gb2312");
            Matcher m = Pattern.compile("(?<=<a\\s{1,1000}data-click=\"[^\"]{1,1000}\"\\s{1,1000}href=\")(.*?)(?=\"\\s*target=\"_blank\"\\s*>)", Pattern.DOTALL | Pattern.CASE_INSENSITIVE).matcher(content);
            while (m.find()) {
                String group = m.group();
                if (group.contains(" ")) {
                    continue;
                }
                list.add(group);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    public static String htmlTrim(String str1) {
        //�޳���<html>�ı�ǩ
        str1 = str1.replaceAll("<[Bb][Rr]\\s?/?>", "\n");
        str1 = str1.replaceAll("</?[^>]+>", "");
        return str1;
    }

    /**
     * һ�����ػ�ȡ�ַ����߶ȵķ���
     *
     * @param s �ַ���
     * @param g ����
     * @return �߶�
     */
    public static int getStringHeight(String s, Graphics g) {
        return (int) g.getFontMetrics().getStringBounds(s, g).getHeight();
    }

    /**
     * һ�����ػ�ȡ�ַ�����ȵķ���
     *
     * @param s �ַ���
     * @param g ����
     * @return ���
     */
    public static int getStringWidth(String s, Graphics g) {
        return (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
    }

    /**
     * �Զ���Ļ��ַ����ķ��������ַ��������Ͻǿ�ʼ�� ����JAVA�Ĵ����½ǿ�ʼ�Ļ���
     *
     * @param g ����
     * @param s �ַ���
     * @param x X����
     * @param y Y����
     */
    public static void drawString(Graphics g, String s, int x, int y) {
        FontMetrics fm = g.getFontMetrics();
        int asc = fm.getAscent();
        g.drawString(s, x, y + asc);
    }

    private static void testBaiDuZhiDao() {
        String content = readUrl("http://zhidao.baidu.com/q?word=%D0%C7%D0%C7&lm=0&fr=search&ct=17&pn=0&tn=ikaslist&rn=10", "GBK");
        Matcher m = Pattern.compile("(?<=<table border=0 cellpadding=0 cellspacing=0><tr><td class=f>)(.*?)(?=</table>)", Pattern.DOTALL | Pattern.CASE_INSENSITIVE).matcher(content);
        while (m.find()) {
            System.out.println(m.group());
        }
    }

    public static String translateChineseToEnglish(String input) {
        Translate.setHttpReferrer("http://www.google.com");
        try {
            return Translate.execute(input, Language.CHINESE, Language.ENGLISH);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return input;
    }

    public static String translateEnglishToChinese(String input) {
        Translate.setHttpReferrer("http://www.google.com");
        try {
            return Translate.execute(input, Language.ENGLISH, Language.CHINESE_SIMPLIFIED);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return input;
    }
}
