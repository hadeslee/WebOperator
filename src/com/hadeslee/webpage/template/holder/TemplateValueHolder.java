/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hadeslee.webpage.template.holder;

import com.hadeslee.webpage.template.*;
import java.io.Serializable;

/**
 * 此类是一个接口，统一规范可以用于模版内容的
 * 对象必须要实现的接口
 * @author Hadeslee
 */
public interface TemplateValueHolder extends Serializable {

    /**
     * 根据传入的KEY，得到这个KEY所对应的模版的值
     * @param cls 模版类
     * @param key 变量名
     * @return 对应的值
     */
    public String getTemplateValue(Class<? extends Template> cls, String key);

    /**
     * 根据传入的KEY，以及相同KEY的索引，得到所需要值
     * 因为可能会有同样的名字对应不同的索引，比如
     * {关键词1}，{关键词2}，{关键词3}
     * @param cls 模版类
     * @param key 变量名
     * @param index 对应的索引
     * @return
     */
    public String getTemplateValue(Class<? extends Template> cls, String key, int index);
}
