/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hadeslee.webpage.searcher;

import com.hadeslee.webpage.util.MyBundle;
import com.hadeslee.webpage.util.ReplaceInfo;
import com.hadeslee.webpage.util.Setting;
import com.hadeslee.webpage.util.Util;
import com.hadeslee.webpage.util.Util.MyLink;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * 关键字搜索工具，一个父类，各个不同的网站写不同的子 类去实现
 *
 * @author Hadeslee
 */
public final class MySearcher implements Serializable {

    private static final long serialVersionUID = 20090921L;
    protected List<Article> content = new ArrayList<Article>();//搜到的关键字列表
    protected boolean done;//是否搜索完成
    private boolean canceled;//是否被取消了
    private static Pattern p = Pattern.compile("</?a[^>]*>", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
    protected static final Logger LOG = Logger.getLogger(MySearcher.class.getName());
    private RealSearcher realSearcher;

    public MySearcher(RealSearcher realSearcher) {
        this.realSearcher = realSearcher;
    }

    private void reset() {
        done = false;
        canceled = false;
        content.clear();
    }

    public String toString() {
        return realSearcher.getName();
    }

    /**
     * 取消当前的搜索
     */
    public void cancel() {
        canceled = true;
    }

    public final List<Article> searchKeyWord(String keyword, KeyWordSearcher keyWordSearcher, ReplaceInfo pro, int rest) {
        reset();
        try {
            List<String> urls = Util.fetchUrl(realSearcher.getSite(), keyword);
            out:
            for (String url : urls) {
                if (canceled) {
                    break;
                }
                Article article = realSearcher.readArticle(url);
                if (article != null) {
                    article.setTitle(p.matcher(article.getTitle()).replaceAll(""));
                    article.setContent(p.matcher(article.getContent()).replaceAll("").
                            replace(keyword, "<strong>" + keyword + "</strong>"));
                    if (pro != null) {
                        article.setContent(Util.parseContent(pro, article.getContent()));
                    }
                    //在采集来的内容页面文件中,第一句话,以及最后的结尾处增加一个关键词,并且增加<a href="/">关键词</a>这个链接
                    article.setContent("<a href='/'>" + keyword + "</a>" + article.getContent() + "<a href='/'>" + keyword + "</a>");

                    if (Setting.getInstance().isTranslateToEnglish()) {
                        article.setContent(Util.translateChineseToEnglish(article.getContent()));
                        article.setTitle(Util.translateChineseToEnglish(article.getTitle()));
                    }
                    //此处插入站群的内容
                    int zhanqunCount = Setting.getInstance().getZhanqunCount();
                    if (zhanqunCount > 0) {
                        StringBuilder sb = new StringBuilder(article.getContent());
                        List<MyLink> list = Util.getNextLink();
                        Random random = new Random();
                        for (MyLink link : list) {
                            sb.insert(random.nextInt(sb.length()),
                                    "<a href='" + link.getHref() + "'>" + link.getName() + "</a>");
                        }
                        article.setContent(sb.toString());
                    }
                    if (rest > content.size()) {
                        content.add(article);
                        LOG.info(MyBundle.getString("searched.artitle") + article.getTitle());
                        keyWordSearcher.articleSearched(keyword, article);
                    } else {
                        LOG.info(MyBundle.getString("searched.full"));
                        break out;
                    }

                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            done = true;
        }
        return content;
    }

    public int getFetchCount() {
        return content.size();
    }

    public List<Article> getContent() {
        return content;
    }

    public boolean isDone() {
        return done;
    }
}
