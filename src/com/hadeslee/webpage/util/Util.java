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
 * 工具类
 *
 * @author Hadeslee
 */
public final class Util {

    private static final int FILE_NAME_MAX_LENGTH = 10;
    private static String searckKeywordURL = null;
    private static String REPLACE_DATA_FILE = "replace.dat";
    private static String currentFriendLinkContent;//当前友情链接的内容，一开始是读的，后来是可以编辑的
    public static final ClipBoardListener CLIPBOARD_LISTENER = new ClipBoardListener();
    public static volatile boolean DEBUG = false;
    private static List<MyLink> myLinkList;
    private static int currentIndex;

    public static String matcher(String input, String regex) {
        return matcher(input, 0, regex, Pattern.DOTALL | Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
    }

    /**
     * windows 7 专用 获取MAC地址
     *
     * @return
     * @throws Exception
     */
    private static String getWin7MACAddress() throws Exception {

        // 获取本地IP对象
        InetAddress ia = InetAddress.getLocalHost();
        // 获得网络接口对象（即网卡），并得到mac地址，mac地址存在于一个byte数组中。
        byte[] mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();

        // 下面代码是把mac地址拼装成String
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < mac.length; i++) {
            if (i != 0) {
                sb.append("-");
            }
            // mac[i] & 0xFF 是为了把byte转化为正整数
            String s = Integer.toHexString(mac[i] & 0xFF);
            sb.append(s.length() == 1 ? 0 + s : s);
        }

        // 把字符串所有小写字母改为大写成为正规的mac地址并返回
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
     * 为某个对象里面的所有成员变量，只要这个成员变量是 JTextComponent以及它的子类，就为其添加这个监听器
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
     * 为JTextArea添加Ctrl+F的查找功能
     *
     * @param jta 要添加的JTA
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
     * 根据属性和内容做一些处理，比如把一些字替换掉 或者做一些内容的修补动作
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
            //只有两者加起来小于内容的大于才有意义，否则就是空的了
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
     * 替换列表模版的title部份
     *
     * @param source 源内容
     * @return 新的内容
     */
    public static String replaceListTemplateTitle(String source) {
        Matcher m = Pattern.compile("<title>(.*?)</title>", Pattern.DOTALL | Pattern.CASE_INSENSITIVE).matcher(source);
        if (m.find()) {
            return m.replaceAll("<title>{关键词1}|{关键词2}|{关键词3}|{关键词4}|{关键词5}|{关键词6}|{关键词7}|{关键词8}</title>\n"
                    + "<META content={关键词1}，{关键词2}，{关键词3}，{关键词4}，{关键词5}，{关键词6}，{关键词7}，{关键词8} name=keywords>\n"
                    + "<META content={关键词1}，{关键词2}，{关键词3}，{关键词4}，{关键词5}，{关键词6}，{关键词7}，{关键词8} name=description>\n");
        }
        return source;
    }

    /**
     * 替换掉模版列表里面的链接，让它变成： 1. 选择列表页标题1 替换成 {文章标题1} 并且将 链接 href=” 旧的” 改成
     * href=”{文章链接1}” 1. 选择列表页标题2 替换成 {文章标题2} 并且将 链接 href=” 旧的” 改成
     * href=”{文章链接2}” 1. 选择列表页标题3 替换成 {文章标题3} 并且将 链接 href=” 旧的” 改成
     * href=”{文章链接3}” 。。。。。 1. 选择列表页标题10 替换成 {文章标题10} 并且将 链接 href=” 旧的” 改成
     * href=”{文章链接10}” 用如下正则表达式， 1，假设有"号 2，假设为'号 3，假设都没有
     * 1,(<a(?:[^>])+?href=(?:\s)*\")(.+?)(\"[^>]*?>)(\s*content1\s*)(</a>)
     * 2,(<a(?:[^>])+?href=(?:\s)*\')(.+?)(\'[^>]*?>)(\s*content1\s*)(</a>)
     * 3,(<a(?:[^>])+?href=(?:\s)*)(.+?)(\s+[^>]*?>)(\s*content1\s*)(</a>)
     *
     * @param source 源代码
     * @param aName 链接的名字<a href="test.htm">名字</a>
     * @param index 索引，从1开始,构成{文章标题1},{文章标题2}...
     * @return 替换好的内容
     */
    public static String replaceLinkOfListTemplate(String source, String aName, int index) {
        if (Util.isEmpty(aName)) {
            return source;
        }
        Matcher m = Pattern.compile("<a[^>]*>" + aName + "</a>", Pattern.DOTALL | Pattern.CASE_INSENSITIVE).matcher(source);
        if (m.find()) {
            aName = m.group();
        }
        source = source.replace(aName, "<a href=\"{文章链接" + index + "}\" target=\"_blank\">{文章标题" + index + "}</a>");
        return source;
    }

    /**
     * 这个实现如下： href= 后面如果是 http:// 则不处理 src= 后面如果是 http:// 则不处理 将页面的 src=” src=
     * 替换成 src=”../ {因为有一些不规则的写法是 写 src= 不带引号} 将页面的 href=” href= 替换成 href=”../
     * {因为有一些不规则的写法是 写 src= 不带引号} 将页面的 src=” src= 替换成 src=”../ {因为有一些不规则的写法是 写
     * src= 不带引号} 将页面的 background=" background= 替换成background="../
     * background=../ {因为有一些不规则的写法是 写 src= 不带引号}\ TODO 目前没有实现，留给以后实现
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
            //这两者都是绝对地址
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
     * 读取注册信息，如果存在注册文件，则返回之，否则 新建一个，存起来，再返回
     *
     * @return
     */
    public static RegInfo getRegInfo() {
        String windir = System.getProperty("user.home");
        File regFile = new File(windir, "hadeslee.dat");
        regFile.getParentFile().mkdirs();
        RegInfo regInfo = null;
        //如果存在就必须要能读出来
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
                JOptionPane.showMessageDialog(null, "您的系统不能运行此软件！！！");
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
     * 根据title生成某个目录下面的唯一名字， 并且名字又不能太长
     *
     * @param title 标题
     * @param dir 在哪个目录下
     * @return 此目录下唯一的名字，不会和别的名字冲突
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
     * 把字符串的内容以某种编码格式相在某个文件里面
     *
     * @param content 要保存的内容
     * @param encoding 编码格式
     * @param file 要写入的文件
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
     * 把关键字插入到给定的文档里面去，如果以前就存在，就删除以前的内容
     *
     * @param originalContent 原始文档
     * @param keywords 关键字列表
     * @param setting 设置，可能根据设置生成不同的页面
     * @return 新的内容
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
     * 把body里面出现过的，在关键字列表里面的关键字 全部加上<strong>关键字</strong>的标签 嵌套也没有关系
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
     * 替换来的title,如果原来有的话，如果原来没有，则直接插入title到 head里面，如果head也没有，那就连head一起插入
     *
     * @param originalContent 原来的文档内容
     * @param newTitle 新的title，包括title标签
     * @return 新的内容
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
     * 替换原来meta里面的description，如果有的话，如果没有，就重新 插入一条新的记录到head里面去
     *
     * @param originalContent 原来的网页内容
     * @param meta 要插入的meta数据
     * @return 新的网页内容
     */
    private static String replaceMetaDescription(String originalContent, String meta) {
        Matcher m = Pattern.compile("<META content\\=(.*?)name\\=description>", Pattern.CASE_INSENSITIVE | Pattern.DOTALL).matcher(originalContent);
        if (m.find()) {
            originalContent = m.replaceFirst(meta);
        } else {
            //这个时候肯定有title了，因为上一步就是处理title的
            m = Pattern.compile("<title>(.*?)</title>", Pattern.CASE_INSENSITIVE | Pattern.DOTALL).matcher(originalContent);
            if (m.find()) {
                originalContent = m.replaceAll("$0\r\n" + meta + "\r\n");
            }
        }
        return originalContent;
    }

    /**
     * 替换原来meta里面的keywords，如果有的话，如果没有，就重新 插入一条新的记录到head里面去
     *
     * @param originalContent 原来的网页内容
     * @param meta 要插入的meta数据
     * @return 新的网页内容
     */
    private static String replaceMetaKeywords(String originalContent, String meta) {
        Matcher m = Pattern.compile("<META content\\=(.*?)name\\=keywords>", Pattern.CASE_INSENSITIVE | Pattern.DOTALL).matcher(originalContent);
        if (m.find()) {
            originalContent = m.replaceAll("\r\n" + meta + "\r\n<META content=" + R.getAuthorMeta() + " name=Author>\r\n");
        } else {
            //这个时候肯定有title了，因为上一步就是处理title的
            m = Pattern.compile("<title>(.*?)</title>", Pattern.CASE_INSENSITIVE | Pattern.DOTALL).matcher(originalContent);
            if (m.find()) {
                originalContent = m.replaceAll("$0\r\n" + meta + "\r\n<META content=" + R.getAuthorMeta() + " name=Author>\r\n");
            }
        }
        return originalContent;
    }

    /**
     * 把中间的那个导航部分插入到body里面去，没有替换的功能 只是插入新的到body的最前面，如果center之内的东西用一个DIV包住
     * 并提供id的话，那就可以找到并替换了。
     *
     * @param originalContent 原来的文档内容
     * @param navi 要插入的导航内容
     * @return 新的内容
     */
    private static String replaceNavigation(String originalContent, String navi) {
        Matcher m = Pattern.compile("(<body[^>]*>)(.*?)(</body>)", Pattern.CASE_INSENSITIVE | Pattern.DOTALL).matcher(originalContent);
        if (m.find()) {
            originalContent = m.replaceFirst("\r\n$1" + navi + "\r\n$2$3\r\n");
        }
        return originalContent;
    }

    /**
     * 根据一个列表的关键字，得到一个title的内容
     *
     * @param keywords 关键字列表
     * @return title内容
     */
    public static String getTitle(List<String> keywords) {
        StringBuilder sb = new StringBuilder();
        sb.append("<title>").append(getKeywordsByPipe(keywords));
        sb.append("</title>");
        return sb.toString();
    }

    /**
     * 得到META里面的description内容
     *
     * @param keywords 关键字
     * @return 内容
     */
    public static String getDescription(List<String> keywords) {
        StringBuilder sb = new StringBuilder();
        sb.append("<META content=").append(getKeywordsByComma(keywords));
        sb.append(" name=description>");
        return sb.toString();
    }

    /**
     * 得到META里面的keyword内容
     *
     * @param keywords 关键字
     * @return 内容
     */
    public static String getKeywords(List<String> keywords) {
        StringBuilder sb = new StringBuilder();
        sb.append("<META content=").append(getKeywordsByComma(keywords));
        sb.append(" name=keywords>");
        return sb.toString();
    }

    /**
     * 返回导航的内容
     *
     * @param keywords 关键字
     * @return 内容
     */
    public static String getNavigation(List<String> keywords) {
        StringBuilder sb = new StringBuilder();
        sb.append("<center>\r\n");
        sb.append("<table width=\"990\" border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\">\r\n");
        sb.append("  <tr>\r\n");
        sb.append("    <td><SPAN class=style11> <a href=\"/\">网站地图：</a> ");
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
     * 返回<center><h1>keywors</h1></center> 之类的内容，其中hn是是h1,h2,h3之类的可以改变的内容
     *
     * @param hn H几
     * @param keywords 关键字列表
     * @return 内容
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
     * 返回一个keywords的字符口中 ，用|符号分隔开，顺序和 list里面是一样的
     *
     * @param keywords 一个列表的关键字
     * @return 一个新的字符串
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
     * 返回一个用,分隔的关键字列表的字符串，并且此字符串和传进来的 list是相反的顺序的。
     *
     * @param keywords 一个关键字的列表
     * @return 一个新的字符串
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
     * 根据配置，读取友情链接里面的内容,也可能什么也读不到
     *
     * @return 读到的内容
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
     * 根据传进来的内容，从http://search.75pp.com/1234567.php?qstr={0} 网站上查询关键字
     *
     * @param searchContent 要查询的内容
     * @return 关键字列表
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
     * 访问某个URL，返回它的内容
     *
     * @param url URL地址
     * @return 该页面的内容
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
     * 读取某个文件里面的内容，用指定的编码格式， 此方法永远不会返回null，最坏的情况下也是 返回空字符串
     *
     * @param file 要读取的文件
     * @param encoding 以何种编码读取
     * @return 文件内容的字符串形式
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
     * 关闭某个实现了Closeable接口的对象,目前 java.io包里面大部份类都实现了这个接口，此方法
     * 不会抛出任何异常，并且也会对参数进行null检查 所以传null到本方法是安全的
     *
     * @param closeabe 要关闭的对象
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
     * 判断一个字符串是否是一个空的字符串
     *
     * @param content 被判断字符串内容
     * @return 是否一个空字符串
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
            //恶心的百度，竟然变化网页的内容格式，竟然只是多一个空格页已，FUCK
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
        //剔出了<html>的标签
        str1 = str1.replaceAll("<[Bb][Rr]\\s?/?>", "\n");
        str1 = str1.replaceAll("</?[^>]+>", "");
        return str1;
    }

    /**
     * 一个简便地获取字符串高度的方法
     *
     * @param s 字符串
     * @param g 画笔
     * @return 高度
     */
    public static int getStringHeight(String s, Graphics g) {
        return (int) g.getFontMetrics().getStringBounds(s, g).getHeight();
    }

    /**
     * 一个简便地获取字符串宽度的方法
     *
     * @param s 字符串
     * @param g 画笔
     * @return 宽度
     */
    public static int getStringWidth(String s, Graphics g) {
        return (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
    }

    /**
     * 自定义的画字符串的方法，从字符串的左上角开始画 不是JAVA的从左下角开始的画法
     *
     * @param g 画笔
     * @param s 字符串
     * @param x X坐标
     * @param y Y坐标
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
