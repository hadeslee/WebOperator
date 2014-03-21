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
 * �ؼ����������ߣ�һ�����࣬������ͬ����վд��ͬ����
 * ��ȥʵ��
 * @author Hadeslee
 */
public abstract class AbstractSearcher implements Serializable {

    private static final long serialVersionUID = 20090921L;
    protected String keyword;//�ؼ���
    protected List<Article> content = new ArrayList<Article>();//�ѵ��Ĺؼ����б�
    protected boolean done;//�Ƿ��������
    private boolean canceled;//�Ƿ�ȡ����
    private static Pattern p = Pattern.compile("</?a[^>]*>", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
    protected static final Logger LOG = Logger.getLogger(AbstractSearcher.class.getName());

    protected AbstractSearcher(String keyword) {
        this.keyword = keyword;
    }

    private void reset() {
        done = false;
        canceled = false;
        content.clear();
    }

    /**
     * ȡ����ǰ������
     */
    public void cancel() {
        canceled = true;
    }

    /**
     *
     * @param url
     * @return
     */
    protected abstract Article readArticle(String url);

    /**
     *
     * @return
     */
    protected abstract String getSite();

    public final List<Article> searchKeyWord(KeyWordSearcher keyWordSearcher, ReplaceInfo pro, int rest) {
        reset();
        try {
            List<String> urls = Util.fetchUrl(getSite(), keyword);
            out:
            for (String url : urls) {
                if (canceled) {
                    break;
                }
                Article article = readArticle(url);
                if (article != null) {
                    article.setTitle(p.matcher(article.getTitle()).replaceAll(""));
                    article.setContent(p.matcher(article.getContent()).replaceAll("").
                            replace(keyword, "<strong>" + keyword + "</strong>"));
                    if (pro != null) {
                        article.setContent(Util.parseContent(pro, article.getContent()));
                    }
                    //�ڲɼ���������ҳ���ļ���,��һ�仰,�Լ����Ľ�β������һ���ؼ���,��������<a href="/">�ؼ���</a>�������
                    article.setContent("<a href='/'>" + keyword + "</a>" + article.getContent() + "<a href='/'>" + keyword + "</a>");

                    if (Setting.getInstance().isTranslateToEnglish()) {
                        article.setContent(Util.translateChineseToEnglish(article.getContent()));
                        article.setTitle(Util.translateChineseToEnglish(article.getTitle()));
                    }
                    //�˴�����վȺ������
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

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public List<Article> getContent() {
        return content;
    }

    public boolean isDone() {
        return done;
    }
}
